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
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.AnyThread;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.d;
import static com.bazaarvoice.bvandroidsdk.internal.Utils.checkNotMain;
import static com.bazaarvoice.bvandroidsdk.internal.Utils.isMain;

/**
 * Wrapper for a {@link Call} for a Conversations request to
 * submission endpoints
 *
 * @param <RequestType> Type of {@link ConversationsSubmissionRequest}
 * @param <ResponseType> Type of {@link ConversationsResponse}
 */
public final class LoadCallSubmission<RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> extends LoadCall<RequestType, ResponseType> {
    private final RequestType submissionRequest;
    private final ConversationsAnalyticsManager conversationsAnalyticsManager;
    private final RequestFactory requestFactory;
    private final SubmitUiHandler<RequestType, ResponseType> submitUiHandler;
    private final SubmitWorkerHandler<RequestType, ResponseType> submitWorkerHandler;
    ConversationsCallback<ResponseType> submitCallback;
    ConversationsSubmissionCallback<ResponseType> submitV7Callback;

    LoadCallSubmission(
        RequestType submissionRequest,
        Class<ResponseType> responseTypeClass,
        ConversationsAnalyticsManager conversationsAnalyticsManager,
        RequestFactory requestFactory,
        Looper bgLooper,
        Looper uiLooper,
        OkHttpClient okHttpClient,
        Gson gson) {
        super(responseTypeClass, okHttpClient, gson);
        this.submissionRequest = submissionRequest;
        this.conversationsAnalyticsManager = conversationsAnalyticsManager;
        this.requestFactory = requestFactory;
        this.submitUiHandler = new SubmitUiHandler<>(uiLooper, this);
        this.submitWorkerHandler = new SubmitWorkerHandler<>(bgLooper, this);
    }

    // region v6 network call routing

    /**
     * @deprecated Use {@link #loadSubmissionSync()} instead
     *
     * @return Response
     * @throws BazaarException caught errors
     */
    @WorkerThread
    @Override
    public ResponseType loadSync() throws BazaarException {
        checkNotMain();
        return submitFlow(true);
    }

    /**
     * @deprecated Use {@link #loadAsync(ConversationsSubmissionCallback)} instead
     *
     * @param conversationsCallback Callback to be performed
     */
    @Override
    public void loadAsync(final ConversationsCallback<ResponseType> conversationsCallback) {
        submitCallback = conversationsCallback;
        dispatchSubmit();
    }

    /**
     * @deprecated To be replaced
     * <br/><br/>
     * This is a container for the old load async behavior which is not ideal
     * in that it doesn't route 100% of errors to the error block, therefore
     * requiring complicated logic to know when success has happened. This is
     * being maintained to avoid breaking 6.x clients, but will be replaced by
     * the more expected {@link #loadSync()} behavior which routes all errors
     * to the error block.
     *
     * @return Response with expected type
     * @throws BazaarException
     */
    private ResponseType legacyLoadAsyncBehavior() throws BazaarException {
        return submitFlow(false);
    }

    /**
     * Handles the logic of Preview vs Submit, as well as Photo Submission.
     * <br/><br/>
     * If it is Preview mode, then it will simply run, and return. This can be seen
     * as a Form preview request.
     * <br/><br/>
     * If it is Submit mode, then it will first run in Preview mode to validate
     * the form data. If the form data is invalid it will return or throw depending
     * on {@code routeFormErrorsToFailure}. If the form data is valid, then it will
     * submit each of the photos, collecting the metadata associated with each. If any
     * photo submission failed, then an exception will be thrown. If all photo submit
     * requests succeeded, it will add any photo metadata to the request, and submit
     * in Submit mode.
     *
     * @param routeFormErrorsToFailure Behavior that each {@link #loadSync()} and {@link #loadAsync(ConversationsCallback)} were
     *                                 doing differently. Will go away in a future NEXUS_USER, and all errors will route to failure.
     * @return Server JSON response mapped to the corresponding POJO type
     * @throws BazaarException Exception if the request failed to happen, or if the JSON deserialization failed
     */
    private ResponseType submitFlow(boolean routeFormErrorsToFailure) throws BazaarException {
        BazaarException error = submissionRequest.getError();

        if (error != null) {
            // If request contains known errors
            // TODO: Should be preconditions on request builder functions instead
            throw error;
        }

        boolean isPreview = submissionRequest.getAction() == Action.Preview;
        if (isPreview) {
            // If the user chose preview, simply submit request
            return submit();
        } else {
            // Send preview request for user incase we need to see error values, since
            // FormErrors do not return for Action=Submit
            submissionRequest.setForcePreview(true);
            ResponseType previewResponse = submit();
            if (previewResponse.getHasErrors()) {
                if (routeFormErrorsToFailure) {
                    throw new BazaarException("Request has form errors");
                } else {
                    return previewResponse;
                }
            }

            // If it is a valid request, follow through with a full submit
            if (submissionRequest.getPhotoUploads() != null && submissionRequest.getPhotoUploads().size() > 0) {
                // If the user wants to submit photos, submit each of them, collect the metadata,
                // and associate it with the submission request
                List<Photo> photos = postPhotosAndSubmissionSync(submissionRequest.getPhotoUploads());
                submissionRequest.setPhotos(photos);
            }
            // Toggle back to no be force preview anymore
            submissionRequest.setForcePreview(false);
            return submit();
        }
    }

    /**
     * @return Server JSON response mapped to the corresponding POJO type
     * @throws BazaarException Exception if the request failed to happen, or if the JSON deserialization failed
     */
    private ResponseType submit() throws BazaarException {
        ResponseType conversationResponse = null;
        BazaarException error = null;
        final Request okRequest = requestFactory.create(submissionRequest);
        call = okHttpClient.newCall(okRequest);
        Response response = null;
        try {
            response = call.execute();
            if (!response.isSuccessful()) {
                error = new BazaarException("Unsuccessful response for Conversations with error code: " + response.code());
            } else {
                try {
                    conversationResponse = deserializeAndCloseResponse(response);
                } catch (BazaarException t) {
                    error = t;
                }
            }

            if (error != null) {
                throw error;
            } else {
                conversationsAnalyticsManager.sendSuccessfulConversationsSubmitResponse(submissionRequest);
                return conversationResponse;
            }
        } catch (IOException e) {
            throw new BazaarException("Execution of call failed", e);
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }

    @AnyThread
    private void dispatchSubmit() {
        submitWorkerHandler.sendMessage(submitWorkerHandler.obtainMessage(SubmitWorkerHandler.SUBMIT));
    }

    @WorkerThread
    private void dispatchCompleteWithFailure(BazaarException e) {
        submitUiHandler.sendMessage(submitUiHandler.obtainMessage(SubmitUiHandler.CB_FAILURE, e));
    }

    @WorkerThread
    private void dispatchCompleteWithSuccess(ResponseType response) {
        submitUiHandler.sendMessage(submitUiHandler.obtainMessage(SubmitUiHandler.CB_SUCCESS, response));
    }

    @MainThread
    private void completeWithFailure(BazaarException e) {
        if (submitCallback != null) {
            submitCallback.onFailure(e);
            submitCallback = null;
        }
    }

    @MainThread
    private void completeWithSuccess(ResponseType response) {
        if (submitCallback != null) {
            submitCallback.onSuccess(response);
            submitCallback = null;
        }
    }

    // endregion

    // region v7 network call routing

    /**
     * @return Response
     * @throws ConversationsSubmissionException caught errors or request errors
     */
    @WorkerThread
    public ResponseType loadSubmissionSync() throws ConversationsSubmissionException {
        if (isMain()) {
            throw ConversationsSubmissionException.withCallOnMainThread();
        }
        return submitFlowV7();
    }

    /**
     * @param callback Callback for submission request
     */
    public void loadAsync(final ConversationsSubmissionCallback<ResponseType> callback) {
        this.submitV7Callback = callback;
        dispatchSubmitV7();
    }

    private ResponseType submitFlowV7() throws ConversationsSubmissionException {
        boolean isPreview = submissionRequest.getAction() == Action.Preview;
        boolean isForm = submissionRequest.getAction() == Action.Form;
        if (isPreview || isForm) {
            // If the user chose preview, simply submit request
            return submitV7();
        } else {
            // Send preview request for user incase we need to see error values, since
            // FormErrors do not return for Action=Submit
            submissionRequest.setForcePreview(true);
            ResponseType previewResponse = submitV7();
            if (previewResponse.getHasErrors()) {
                return previewResponse;
            }

            // If it is a valid request, follow through with a full submit
            if (submissionRequest.getPhotoUploads() != null && submissionRequest.getPhotoUploads().size() > 0) {
                // If the user wants to submit photos, submit each of them, collect the metadata,
                // and associate it with the submission request
                try {
                    List<Photo> photos = postPhotosAndSubmissionSync(submissionRequest.getPhotoUploads());
                    submissionRequest.setPhotos(photos);
                } catch (BazaarException e) {
                    e.printStackTrace();
                }
            }
            // Toggle back to no be force preview anymore
            submissionRequest.setForcePreview(false);
            return submitV7();
        }
    }

    private ResponseType submitV7() throws ConversationsSubmissionException {
        ResponseType conversationResponse = null;
        ConversationsSubmissionException exception = null;

        final Request okRequest = requestFactory.create(submissionRequest);
        call = okHttpClient.newCall(okRequest);
        Response response = null;
        try {
            response = call.execute();
            if (!response.isSuccessful()) {
                exception = ConversationsSubmissionException.withNoRequestErrors("Unsuccessful response HTTP error code: " + response.code());
            } else {
                try {
                    conversationResponse = deserializeAndCloseResponseV7(response);
                } catch (ConversationsSubmissionException e) {
                    exception = e;
                }
            }

            if (conversationResponse != null) {
                if (!conversationResponse.getHasErrors()) {
                    conversationsAnalyticsManager.sendSuccessfulConversationsSubmitResponse(submissionRequest);
                } else {
                    List<Error> errors = Collections.emptyList();
                    List<FieldError> fieldErrors = Collections.emptyList();
                    List<FormField> formFields = Collections.emptyList();

                    if (conversationResponse.getErrors() != null) {
                        errors = conversationResponse.getErrors();
                    }
                    if (conversationResponse instanceof ConversationsSubmissionResponse) {
                        final ConversationsSubmissionResponse submissionResponse = ((ConversationsSubmissionResponse)conversationResponse);
                        fieldErrors = submissionResponse.getFieldErrors();
                        formFields = submissionResponse.getFormFields();
                        addFormFieldsToFieldErrors(formFields, fieldErrors);
                    }

                    exception = ConversationsSubmissionException.withRequestErrors(errors, fieldErrors);
                }
            }
        } catch (IOException e) {
            exception = ConversationsSubmissionException.withNoRequestErrors("Execution of call failed", e);
        } catch (Throwable t) {
            exception = ConversationsSubmissionException.withNoRequestErrors("Unknown error", t);
        } finally {
            if (response != null) {
                response.body().close();
            }
        }

        if (exception != null) {
            throw exception;
        }

        return conversationResponse;
    }

    private static void addFormFieldsToFieldErrors(List<FormField> formFields, List<FieldError> fieldErrors) {
        for (FieldError fieldError : fieldErrors) {
            final FormField formField = findFormFieldForError(formFields, fieldError);
            fieldError.setFormField(formField);
        }
    }

    @Nullable
    private static FormField findFormFieldForError(List<FormField> formFields, FieldError fieldError) {
        for (FormField formField : formFields) {
            if (formField.getId().equals(fieldError.getField())) {
                return formField;
            }
        }
        return null;
    }

    @AnyThread
    private void dispatchSubmitV7() {
        submitWorkerHandler.sendMessage(submitWorkerHandler.obtainMessage(SubmitWorkerHandler.SUBMIT_V7));
    }

    @WorkerThread
    private void dispatchCompleteV7WithSuccess(ResponseType response) {
        submitUiHandler.sendMessage(submitUiHandler.obtainMessage(SubmitUiHandler.CB_SUCCESS_V7, response));
    }

    @WorkerThread
    private void dispatchCompleteV7WithFailure(ConversationsSubmissionException exception) {
        submitUiHandler.sendMessage(submitUiHandler.obtainMessage(SubmitUiHandler.CB_FAILURE_V7, exception));
    }

    @MainThread
    private void completeWithSuccessV7(ResponseType response) {
        if (submitV7Callback != null) {
            submitV7Callback.onSuccess(response);
            submitV7Callback = null;
        }
    }

    @MainThread
    private void completeWithFailureV7(ConversationsSubmissionException exception) {
        if (submitV7Callback != null) {
            submitV7Callback.onFailure(exception);
            submitV7Callback = null;
        }
    }

    // endregion

    private List<Photo> postPhotosAndSubmissionSync(List<PhotoUpload> photoUploads) throws BazaarException {
        d("Submission", String.format("Preparing to submit %d photos", photoUploads.size()));
        final List<Photo> photos = new ArrayList<>();

        try {
            for (PhotoUpload upload : photoUploads) {
                final PhotoUploadRequest uploadRequest = new PhotoUploadRequest.Builder(upload).build();
                final Request okRequest = requestFactory.create(uploadRequest);
                Call photoCall = okHttpClient.newCall(okRequest);
                Response response = null;
                try {
                    response = photoCall.execute();
                    Photo photo = deserializePhotoResponse(response);
                    photo.setCaption(upload.getCaption());
                    photos.add(photo);
                } finally {
                    if (response != null) {
                        response.body().close();
                    }
                }
            }
        } catch (Throwable e) {
            throw new BazaarException(e.getMessage());
        }

        return photos;
    }

    private Photo deserializePhotoResponse(Response response) throws BazaarException {
        Reader reader = response.body().charStream();
        PhotoUploadResponse photoUploadResponse = gson.fromJson(reader, PhotoUploadResponse.class);
        response.body().close();
        if (photoUploadResponse.getHasErrors()) {
            throw new BazaarException("Failed to upload image");
        }
        return photoUploadResponse.getPhoto();
    }

    private static class SubmitUiHandler<RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> extends Handler {
        private static final int CB_SUCCESS = 1;
        private static final int CB_FAILURE = 2;
        private static final int CB_SUCCESS_V7 = 3;
        private static final int CB_FAILURE_V7 = 4;

        private final LoadCallSubmission<RequestType, ResponseType> loadCallSubmission;

        public SubmitUiHandler(Looper looper, LoadCallSubmission<RequestType, ResponseType> loadCallSubmission) {
            super(looper);
            this.loadCallSubmission = loadCallSubmission;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CB_SUCCESS: {
                    ResponseType response = (ResponseType) msg.obj;
                    loadCallSubmission.completeWithSuccess(response);
                    break;
                }
                case CB_FAILURE: {
                    BazaarException bazaarException = (BazaarException) msg.obj;
                    loadCallSubmission.completeWithFailure(bazaarException);
                    break;
                }
                case CB_SUCCESS_V7: {
                    ResponseType submissionResponse = (ResponseType) msg.obj;
                    loadCallSubmission.completeWithSuccessV7(submissionResponse);
                    break;
                }
                case CB_FAILURE_V7: {
                    ConversationsSubmissionException exception = (ConversationsSubmissionException) msg.obj;
                    loadCallSubmission.completeWithFailureV7(exception);
                    break;
                }
            }
        }
    }

    private static class SubmitWorkerHandler<RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> extends Handler {
        private static final int SUBMIT = 1;
        private static final int SUBMIT_V7 = 2;
        private final LoadCallSubmission<RequestType, ResponseType> loadCallSubmission;

        public SubmitWorkerHandler(Looper looper, LoadCallSubmission<RequestType, ResponseType> loadCallSubmission) {
            super(looper);
            this.loadCallSubmission = loadCallSubmission;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUBMIT: {
                    try {
                        ResponseType response = loadCallSubmission.legacyLoadAsyncBehavior();
                        loadCallSubmission.dispatchCompleteWithSuccess(response);
                    } catch (BazaarException e) {
                        loadCallSubmission.dispatchCompleteWithFailure(e);
                    }
                    break;
                }
                case SUBMIT_V7: {
                    ResponseType submissionResponse = null;
                    try {
                        submissionResponse = loadCallSubmission.submitFlowV7();
                        loadCallSubmission.dispatchCompleteV7WithSuccess(submissionResponse);
                    } catch (ConversationsSubmissionException e) {
                        loadCallSubmission.dispatchCompleteV7WithFailure(e);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        submitCallback = null;
        submitV7Callback = null;
    }
}
