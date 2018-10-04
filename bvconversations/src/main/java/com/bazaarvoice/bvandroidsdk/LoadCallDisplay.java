/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import androidx.annotation.AnyThread;
import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.checkMain;
import static com.bazaarvoice.bvandroidsdk.internal.Utils.checkNotMain;
import static com.bazaarvoice.bvandroidsdk.internal.Utils.isMain;

/**
 *
 * @param <RequestType>
 * @param <ResponseType>
 */
public final class LoadCallDisplay<RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> extends LoadCall<RequestType, ResponseType> {
    private final RequestType request;
    private final ConversationsAnalyticsManager conversationsAnalyticsManager;
    private final DisplayUiHandler<RequestType, ResponseType> displayUiHandler;
    private final DisplayWorkerHandler<RequestType, ResponseType> displayWorkerHandler;
    ConversationsCallback<ResponseType> displayCallback;
    ConversationsDisplayCallback<ResponseType> displayV7Callback;

    LoadCallDisplay(
        RequestType request,
        Class<ResponseType> responseTypeClass,
        Call call,
        ConversationsAnalyticsManager conversationsAnalyticsManager,
        OkHttpClient okHttpClient,
        Gson gson,
        Looper uiLooper,
        Looper bgLooper) {
        super(responseTypeClass, okHttpClient, gson);
        this.call = call;
        this.request = request;
        this.conversationsAnalyticsManager = conversationsAnalyticsManager;
        this.displayUiHandler = new DisplayUiHandler<>(uiLooper, this);
        this.displayWorkerHandler = new DisplayWorkerHandler<>(bgLooper, this);
    }

    RequestType getRequest() {
        return request;
    }

    ConversationsAnalyticsManager getConversationsAnalyticsManager() {
        return conversationsAnalyticsManager;
    }

    // region v6 network call routing

    private ResponseType fetch() throws BazaarException {
        ResponseType conversationResponse;
        Response response = null;
        try {
            response = call.execute();
            conversationResponse = deserializeAndCloseResponse(response);
            // Route callbacks to Analytics Manager to handle any analytics that are associated
            // with a successful display response
            conversationsAnalyticsManager.sendSuccessfulConversationsDisplayResponse(conversationResponse);
        } catch (IOException e) {
            throw new BazaarException("Execution of call failed", e);
        } catch (BazaarException e) {
            throw e;
        } catch (Throwable t) {
            throw new BazaarException("Unknown exception", t);
        } finally {
            if (response != null) {
                response.body().close();
            }
        }

        return conversationResponse;
    }

    /**
     * @deprecated Use {@link #loadDisplaySync()} instead
     *
     * @return Response
     * @throws BazaarException caught errors
     */
    @Override
    public ResponseType loadSync() throws BazaarException {
        checkNotMain();
        return fetch();
    }

    /**
     * @deprecated Use {@link #loadAsync(ConversationsDisplayCallback)} instead
     *
     * @param conversationsCallback Callback to be performed
     */
    @Override
    public void loadAsync(final ConversationsCallback<ResponseType> conversationsCallback) {
        checkMain();
        displayCallback = conversationsCallback;
        dispatchFetch();
    }

    @AnyThread
    private void dispatchFetch() {
        displayWorkerHandler.sendMessage(displayWorkerHandler.obtainMessage(DisplayWorkerHandler.FETCH));
    }

    @WorkerThread
    private void dispatchCompleteWithFailure(BazaarException e) {
        displayUiHandler.sendMessage(displayUiHandler.obtainMessage(LoadCallDisplay.DisplayUiHandler.CB_FAILURE, e));
    }

    @WorkerThread
    private void dispatchCompleteWithSuccess(ResponseType response) {
        displayUiHandler.sendMessage(displayUiHandler.obtainMessage(LoadCallDisplay.DisplayUiHandler.CB_SUCCESS, response));
    }

    @MainThread
    private void completeWithFailure(BazaarException e) {
        if (displayCallback != null) {
            displayCallback.onFailure(e);
            displayCallback = null;
        }
    }

    @MainThread
    private void completeWithSuccess(ResponseType response) {
        if (displayCallback != null) {
            displayCallback.onSuccess(response);
            displayCallback = null;
        }
    }

    // endregion

    // region v7 network call routing

    private ResponseType fetchV7() throws ConversationsException {
        ResponseType conversationResponse;
        Response response = null;
        try {
            response = call.execute();
            conversationResponse = deserializeAndCloseResponseV7(response);
            if (conversationResponse != null && !conversationResponse.getHasErrors()) {
                // Route callbacks to Analytics Manager to handle any analytics that are associated
                // with a successful display response
                conversationsAnalyticsManager.sendSuccessfulConversationsDisplayResponse(conversationResponse);
            } else {
                List<Error> errors = Collections.emptyList();
                if (conversationResponse != null && conversationResponse.getErrors() != null) {
                    errors = conversationResponse.getErrors();
                }
                throw ConversationsException.withRequestErrors(errors);
            }
        } catch (IOException e) {
            throw ConversationsException.withNoRequestErrors("Execution of call failed", e);
        } catch (ConversationsException e) {
            throw e;
        } catch (Throwable t) {
            throw ConversationsException.withNoRequestErrors("Unknown exception", t);
        } finally {
            if (response != null) {
                response.body().close();
            }
        }

        return conversationResponse;
    }

    /**
     * @return Response
     * @throws ConversationsException caught errors or request errors
     */
    @WorkerThread
    public ResponseType loadDisplaySync() throws ConversationsException {
        if (isMain()) {
            throw ConversationsException.withCallOnMainThread();
        }
        return fetchV7();
    }

    /**
     * @param callback Callback for display request
     */
    public void loadAsync(final ConversationsDisplayCallback<ResponseType> callback) {
        checkMain();
        displayV7Callback = callback;
        dispatchFetchV7();
    }

    @AnyThread
    private void dispatchFetchV7() {
        displayWorkerHandler.sendMessage(displayWorkerHandler.obtainMessage(DisplayWorkerHandler.FETCH_V7));
    }

    @WorkerThread
    private void dispatchCompleteWithFailureV7(ConversationsException e) {
        displayUiHandler.sendMessage(displayUiHandler.obtainMessage(DisplayUiHandler.CB_FAILURE_V7, e));
    }

    @WorkerThread
    private void dispatchCompleteWithSuccessV7(ResponseType response) {
        displayUiHandler.sendMessage(displayUiHandler.obtainMessage(DisplayUiHandler.CB_SUCCESS_V7, response));
    }

    @MainThread
    private void completeWithFailureV7(ConversationsException e) {
        if (displayV7Callback != null) {
            displayV7Callback.onFailure(e);
            displayV7Callback = null;
        }
    }

    @MainThread
    private void completeWithSuccessV7(ResponseType response) {
        if (displayV7Callback != null) {
            displayV7Callback.onSuccess(response);
            displayV7Callback = null;
        }
    }

    // endregion

    @Override
    public void cancel() {
        super.cancel();
        displayCallback = null;
    }

    private static class DisplayUiHandler<RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> extends Handler {
        private static final int CB_SUCCESS = 1;
        private static final int CB_FAILURE = 2;
        private static final int CB_SUCCESS_V7 = 3;
        private static final int CB_FAILURE_V7 = 4;

        private final LoadCallDisplay<RequestType, ResponseType> loadCallDisplay;

        public DisplayUiHandler(Looper looper, LoadCallDisplay<RequestType, ResponseType> loadCallDisplay) {
            super(looper);
            this.loadCallDisplay = loadCallDisplay;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CB_SUCCESS: {
                    ResponseType response = (ResponseType) msg.obj;
                    loadCallDisplay.completeWithSuccess(response);
                    break;
                }
                case CB_FAILURE: {
                    BazaarException bazaarException = (BazaarException) msg.obj;
                    loadCallDisplay.completeWithFailure(bazaarException);
                    break;
                }
                case CB_SUCCESS_V7: {
                    ResponseType response = (ResponseType) msg.obj;
                    loadCallDisplay.completeWithSuccessV7(response);
                    break;
                }
                case CB_FAILURE_V7: {
                    ConversationsException exception = (ConversationsException) msg.obj;
                    loadCallDisplay.completeWithFailureV7(exception);
                }
            }
        }
    }

    private static class DisplayWorkerHandler<RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> extends Handler {
        private static final int FETCH = 1;
        private static final int FETCH_V7 = 2;
        private final LoadCallDisplay<RequestType, ResponseType> loadCallDisplay;

        public DisplayWorkerHandler(Looper looper, LoadCallDisplay<RequestType, ResponseType> loadCallDisplay) {
            super(looper);
            this.loadCallDisplay = loadCallDisplay;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FETCH: {
                    try {
                        ResponseType response = loadCallDisplay.fetch();
                        loadCallDisplay.dispatchCompleteWithSuccess(response);
                    } catch (BazaarException e) {
                        loadCallDisplay.dispatchCompleteWithFailure(e);
                    }
                    break;
                }
                case FETCH_V7: {
                    try {
                        ResponseType response = loadCallDisplay.fetchV7();
                        loadCallDisplay.dispatchCompleteWithSuccessV7(response);
                    } catch (ConversationsException e) {
                        loadCallDisplay.dispatchCompleteWithFailureV7(e);
                    }
                }
            }
        }
    }
}