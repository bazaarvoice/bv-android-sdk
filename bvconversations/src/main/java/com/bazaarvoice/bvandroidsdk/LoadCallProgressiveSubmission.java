package com.bazaarvoice.bvandroidsdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.checkMain;
import static com.bazaarvoice.bvandroidsdk.internal.Utils.checkNotMain;

public class LoadCallProgressiveSubmission<RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> extends LoadCall<RequestType, ResponseType> {

    private final RequestType request;
    private final ConversationsAnalyticsManager conversationsAnalyticsManager;
    private final ProgressiveSubmissionUiHandler displayUiHandler;
    private final ProgressiveSubmissionWorkerHandler displayWorkerHandler;
    private ConversationsSubmissionCallback<ResponseType> progressiveSubmissionCallback;
    private ConversationsCallback<ResponseType> conversationsCallback;
    public static final String TAG = "BVProgressiveSubmission";

    LoadCallProgressiveSubmission(
            RequestType request,
            Class<ResponseType> responseTypeClass,
            ConversationsAnalyticsManager conversationsAnalyticsManager,
            Looper uiLooper,
            Looper bgLooper,
            OkHttpClient okHttpClient,
            Gson gson,
            Call call) {
        super(request, responseTypeClass, okHttpClient, gson);
        this.call = call;
        this.request = request;
        this.conversationsAnalyticsManager = conversationsAnalyticsManager;
        this.displayUiHandler = new ProgressiveSubmissionUiHandler<>(uiLooper, this);
        this.displayWorkerHandler = new ProgressiveSubmissionWorkerHandler<>(bgLooper, this);
    }

    RequestType getRequest() {
        return request;
    }

    ConversationsAnalyticsManager getConversationsAnalyticsManager() {
        return conversationsAnalyticsManager;
    }

    @Override
    @WorkerThread
    public ResponseType loadSync() throws BazaarException {
        BVSDK.getInstance().bvLogger.v(TAG, "Beginning of sync request");
        checkNotMain();
        return fetch();
    }

    @Override
    @Deprecated
    /**
     * @deprecated Use {@link #loadAsync(ConversationsSubmissionCallback)} instead
     */
    public void loadAsync(ConversationsCallback<ResponseType> conversationsCallback) {
        BVSDK.getInstance().bvLogger.v(TAG, "Beginning of async request");
        checkMain();
        this.conversationsCallback = conversationsCallback;
        displayWorkerHandler.sendEmptyMessage(0);
    }

    @MainThread
    public void loadAsync(@NonNull ConversationsSubmissionCallback<ResponseType> conversationsCallback) {
        BVSDK.getInstance().bvLogger.v(TAG, "Beginning of async request");
        checkMain();
        progressiveSubmissionCallback = conversationsCallback;
        displayWorkerHandler.sendEmptyMessage(0);
    }

    @WorkerThread
    private ResponseType fetch() throws ConversationsException {
        BVSDK.getInstance().bvLogger.v(TAG, "Fetching request");
        ResponseType conversationResponse;
        Response response = null;
        try {
            response = call.execute();
            conversationResponse = deserializeAndCloseResponseV7(response);
            if (conversationResponse != null && !conversationResponse.getHasErrors()) {
                // Route callbacks to Analytics Manager to handle analytics
                conversationsAnalyticsManager.sendSuccessfulConversationsSubmitResponse(request);
            } else {
                List<Error> errors = getErrorsFromResponse(conversationResponse);
                if (conversationResponse instanceof ProgressiveSubmitResponse) {
                    ProgressiveSubmitResponse progressiveSubmitResponse = (ProgressiveSubmitResponse) conversationResponse;
                    if (progressiveSubmitResponse.getData() != null) {
                        throw ConversationsSubmissionException.withRequestErrors(errors, progressiveSubmitResponse.getData().formValidationErrors);
                    }
                }
                throw ConversationsSubmissionException.withRequestErrors(errors, new ArrayList<>());
            }
        } catch (IOException e) {
            throw ConversationsSubmissionException.withNoRequestErrors("Execution of call failed", e);
        } catch (ConversationsSubmissionException e) {
            throw e;
        } catch (Throwable t) {
            throw ConversationsSubmissionException.withNoRequestErrors("Unknown exception", t);
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }

        return conversationResponse;
    }

    private List<Error> getErrorsFromResponse(ResponseType conversationResponse) {
        List<Error> errors = new ArrayList<>();
        if (conversationResponse != null && conversationResponse.getErrors() != null) {
            errors = conversationResponse.getErrors();
        }
        return errors;
    }

    private void completeWithException(ConversationsSubmissionException exception) {
        BVSDK.getInstance().bvLogger.v(TAG, "Complete with failure");
        if (progressiveSubmissionCallback != null) {
            progressiveSubmissionCallback.onFailure(exception);
            progressiveSubmissionCallback = null;
        } else if (conversationsCallback != null) {
            conversationsCallback.onFailure(exception);
            conversationsCallback = null;
        }
    }

    private void completeWithSuccess(ResponseType response) {
        BVSDK.getInstance().bvLogger.v(TAG, "Complete With Success");
        if (progressiveSubmissionCallback != null) {
            progressiveSubmissionCallback.onSuccess(response);
            progressiveSubmissionCallback = null;
        } else if (conversationsCallback != null) {
            conversationsCallback.onSuccess(response);
            conversationsCallback = null;
        }
    }

    private void dispatchCompleteWithSuccess(ResponseType response) {
        Message message = displayUiHandler.obtainMessage(ProgressiveSubmissionUiHandler.CB_SUCCESS, response);
        displayUiHandler.sendMessage(message);
    }

    private void dispatchError(BazaarException e) {
        Message message = displayUiHandler.obtainMessage(ProgressiveSubmissionUiHandler.CB_FAILURE, e);
        displayUiHandler.sendMessage(message);
    }

    /**
     * Handler that is responsible for communicating to the BVWorker thread to complete background work
     *
     * @param <RequestType>
     * @param <ResponseType>
     */
    private static class ProgressiveSubmissionWorkerHandler<RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> extends Handler {

        LoadCallProgressiveSubmission<RequestType, ResponseType> progressiveSubmissionCallback;

        ProgressiveSubmissionWorkerHandler(Looper looper, LoadCallProgressiveSubmission<RequestType, ResponseType> progressiveSubmissionCallback) {
            super(looper);
            this.progressiveSubmissionCallback = progressiveSubmissionCallback;
        }

        @Override
        public void handleMessage(Message msg) {
            BVSDK.getInstance().bvLogger.v(TAG, "Handle display worker message");
            try {
                ResponseType response = progressiveSubmissionCallback.fetch();
                progressiveSubmissionCallback.dispatchCompleteWithSuccess(response);
            } catch (BazaarException e) {
                progressiveSubmissionCallback.dispatchError(e);
            }

        }
    }

    /**
     * Handler responsible for communicating with the MainThread when work on the WorkerThread is complete.
     */
    private static class ProgressiveSubmissionUiHandler<RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> extends Handler {
        LoadCallProgressiveSubmission<RequestType, ResponseType> loadCallProgressiveSubmission;
        private static final int CB_SUCCESS = 1;
        private static final int CB_FAILURE = 2;

        ProgressiveSubmissionUiHandler(Looper uiLooper, LoadCallProgressiveSubmission<RequestType, ResponseType> loadCallProgressiveSubmission) {
            super(uiLooper);
            this.loadCallProgressiveSubmission = loadCallProgressiveSubmission;
        }

        @Override
        public void handleMessage(Message msg) {
            BVSDK.getInstance().bvLogger.v(TAG, "Handle UI message");
            switch (msg.what) {
                case CB_SUCCESS:
                    ResponseType response = (ResponseType) msg.obj;
                    loadCallProgressiveSubmission.completeWithSuccess(response);
                    break;
                case CB_FAILURE:
                    ConversationsSubmissionException exception = (ConversationsSubmissionException) msg.obj;
                    loadCallProgressiveSubmission.completeWithException(exception);
                    break;
            }
        }
    }

}
