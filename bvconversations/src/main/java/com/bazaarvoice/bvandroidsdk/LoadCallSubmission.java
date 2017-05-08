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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.d;

/**
 * Wrapper for a {@link Call} for a Conversations request to
 * submission endpoints
 *
 * @param <RequestType> Type of {@link ConversationsSubmissionRequest}
 * @param <ResponseType> Type of {@link ConversationsResponse}
 */
public final class LoadCallSubmission<RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> extends LoadCall<RequestType, ResponseType> {

    private final RequestType submissionRequest;

    private static class SubmissionDelegateCallback<RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> implements Callback {
        private final ConversationsCallback<ResponseType> conversationsCallback;
        private final LoadCallSubmission<RequestType, ResponseType> loadCallSubmission;
        private final RequestType submissionRequest;
        private final Class responseTypeClass;

        public SubmissionDelegateCallback(final ConversationsCallback<ResponseType> conversationsCallback, final LoadCallSubmission<RequestType, ResponseType> loadCallDisplay, final RequestType submissionRequest, final Class responseTypeClass) {
            this.conversationsCallback = conversationsCallback;
            this.loadCallSubmission = loadCallDisplay;
            this.submissionRequest = submissionRequest;
            this.responseTypeClass = responseTypeClass;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            BazaarException bazaarException = new BazaarException("Submission Request Failed", e);
            loadCallSubmission.errorOnMainThread(conversationsCallback, bazaarException);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            ConversationsResponse conversationResponse = null;
            BazaarException error = null;
            try {
                if (!response.isSuccessful()) {
                    error = new BazaarException("Unsuccessful response for Conversations with error code: " + response.code());
                } else {
                    try {
                        conversationResponse = loadCallSubmission.deserializeAndCloseResponse(response);
                    } catch (BazaarException t) {
                        error = t;
                    }
                }

                boolean isPreview = submissionRequest.getBuilder().getAction() == Action.Preview;
                if (error != null) {
                    loadCallSubmission.errorOnMainThread(conversationsCallback, error);
                } else {
                    //if User intended to only Preview or if a Submit has already been previewed we are done and can callback
                    boolean readyToDeliverResult = isPreview || !submissionRequest.getForcePreview();
                    if (readyToDeliverResult) {
                        ConversationsAnalyticsManager convAnalyticsManager = ConversationsAnalyticsManager.getInstance(BVSDK.getInstance());
                        convAnalyticsManager.sendSuccessfulConversationsSubmitResponse(submissionRequest);
                        loadCallSubmission.successOnMainThread(conversationsCallback, conversationResponse);
                    }
                    //We know that a Submit was succesfully previewed so now we Submit for real
                    else {
                        LoadCall newCall = BVConversationsClient.reCreateCallNoPreview(responseTypeClass, submissionRequest);
                        newCall.loadAsync(conversationsCallback);
                    }
                }
            } finally {
                if (response != null) {
                    response.body().close();
                }
            }
        }
    }

    LoadCallSubmission(RequestType submissionRequest, Class<ResponseType> responseTypeClass, Call call) {
        super(responseTypeClass, call);
        this.submissionRequest = submissionRequest;
    }

    RequestType getSubmissionRequest() {
        return submissionRequest;
    }

    @Override
    public ResponseType loadSync() throws BazaarException {
        ConversationsResponse conversationResponse = null;
        BazaarException error = submissionRequest.getError();

        if (error != null) {
            throw error;
        }

        try {
            List<PhotoUpload> photoUploads = submissionRequest.getBuilder().photoUploads;
            if (photoUploads != null && photoUploads.size() > 0 && !submissionRequest.getForcePreview() && submissionRequest.getBuilder().getAction() == Action.Submit) {

                return postPhotosAndSubmissionSync(photoUploads, submissionRequest);

            } else {
                Response response = null;
                try {
                    response = call.execute();
                    conversationResponse = deserializeAndCloseResponse(response);
                    if (conversationResponse.getHasErrors()) {
                        throw new BazaarException(gson.toJson(conversationResponse.getErrors()));
                    }
                    if (submissionRequest.getForcePreview()) {
                        LoadCall loadCall = BVConversationsClient.reCreateCallNoPreview(responseTypeClass, submissionRequest);
                        ResponseType finalResponse = (ResponseType) loadCall.loadSync();
                        ConversationsAnalyticsManager convAnalyticsManager = ConversationsAnalyticsManager.getInstance(BVSDK.getInstance());
                        convAnalyticsManager.sendSuccessfulConversationsSubmitResponse(submissionRequest);
                        return finalResponse;
                    }
                } finally {
                    if (response != null) {
                        response.body().close();
                    }
                }
            }

        } catch (Throwable t) {
            throw new BazaarException(t.getMessage());
        }
        return (ResponseType) conversationResponse;
    }

    private ResponseType postPhotosAndSubmissionSync(List<PhotoUpload> photoUploads, ConversationsSubmissionRequest submission) throws BazaarException {
        d("Submission", String.format("Preparing to submit %d photos", photoUploads.size()));
        final List<Photo> photos = new ArrayList<>();

        try {
            for (PhotoUpload upload : photoUploads) {
                Call photoCall = makePhotoCall(upload, submission);
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

        LoadCall loadCall = BVConversationsClient.reCreateCallWithPhotos(responseTypeClass, submission, photos);
        return (ResponseType) loadCall.loadSync();
    }

    private void postPhotosAndSubmissionAsync(final ConversationsCallback<ResponseType> conversationsCallback, final List<PhotoUpload> photoUploads, final ConversationsSubmissionRequest submission) {
        final BVLogger bvLogger = BVSDK.getInstance().getBvLogger();
        bvLogger.d("Submission", String.format("Preparing to submit %d photos", photoUploads.size()));
        final List<Photo> photos = Collections.synchronizedList(new ArrayList<Photo>());

        for (final PhotoUpload upload : photoUploads) {
            Call photoCall = makePhotoCall(upload, submission);
            //async upload all photos
            photoCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    errorOnMainThread(conversationsCallback, new BazaarException(e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        Photo photo = deserializePhotoResponse(response);
                        photo.setCaption(upload.getCaption());
                        photos.add(photo);
                        ConversationsAnalyticsManager convAnalyticsManager = ConversationsAnalyticsManager.getInstance(BVSDK.getInstance());
                        convAnalyticsManager.sendSuccessfulConversationsPhotoUpload(submission);
                        //whenever we have received successful responses for every expect photo upload we can
                        // reconstruct the submissionRequest request with the photo upload response details
                        if (photos.size() == photoUploads.size()) {
                            LoadCall loadCall = BVConversationsClient.reCreateCallWithPhotos(responseTypeClass, submission, photos);
                            loadCall.loadAsync(conversationsCallback);
                        }
                    } catch (BazaarException e) {
                        errorOnMainThread(conversationsCallback, e);
                    } finally {
                        if (response != null) {
                            response.body().close();
                        }
                    }
                }
            });
        }

    }

    private Photo deserializePhotoResponse(Response response) throws BazaarException {
        Reader reader = response.body().charStream();
        PhotoUploadResponse photoUploadResponse = gson.fromJson(reader, PhotoUploadResponse.class);
        response.body().close();
        if (photoUploadResponse.getHasErrors()) {
            throw new BazaarException(gson.toJson(photoUploadResponse.getFormErrors()));
        }
        return photoUploadResponse.getPhoto();
    }

    @Override
    public void loadAsync(final ConversationsCallback<ResponseType> conversationsCallback) {
        BazaarException error = submissionRequest.getError();

        if (error != null) {
            errorOnMainThread(conversationsCallback, error);
            return;
        }

        List<PhotoUpload> photoUploads = submissionRequest.getBuilder().photoUploads;
        //At this point we assume know that since we are submitting and not being forced to preview that it has been previewed already.
        // The request is ready to have its photos uploaded then submitted.
        if (photoUploads != null && photoUploads.size() > 0 && !submissionRequest.getForcePreview() && submissionRequest.getBuilder().getAction() == Action.Submit) {

            postPhotosAndSubmissionAsync(conversationsCallback, photoUploads, submissionRequest);

        } else {
            //At this point we know that the submissionRequest needs to be previewed
            //Either by user asking for preview or being forced to preview before Submit
            //Or it is ready for full submissionRequest (it's had it's photo's uploaded or never had any)
            this.call.enqueue(new SubmissionDelegateCallback<>(conversationsCallback, this, submissionRequest, responseTypeClass));
        }
    }

    private Call makePhotoCall(PhotoUpload upload, ConversationsSubmissionRequest conversationsSubmitRequest) {
        String fullUrl = String.format("%s%s", BVConversationsClient.conversationsBaseUrl, upload.getEndPoint());
        RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(ConversationsRequest.kAPI_VERSION, ConversationsRequest.API_VERSION)
                .addFormDataPart(ConversationsRequest.kPASS_KEY, conversationsSubmitRequest.getApiKey())
                .addFormDataPart(PhotoUpload.kCONTENT_TYPE, upload.getContentType().getKey())
                .addFormDataPart("photo", "photo.png", RequestBody.create(MEDIA_TYPE_PNG, upload.getPhotoFile())).build();

        Request request = new Request.Builder()
                .addHeader("User-Agent", BVSDK.getInstance().getBvWorkerData().getBvSdkUserAgent())
                .url(fullUrl)
                .post(req)
                .build();

        return okHttpClient.newCall(request);
    }
}
