/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.util.Log;

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

/**
 * TODO: Describe file here.
 */
public final class LoadCallSubmission<T> extends LoadCall<T> {

    private final ConversationsSubmission submission;

    LoadCallSubmission(ConversationsSubmission submission, Class<T> c, Call call) {
        super(c, call);
        this.submission = submission;
    }

    @Override
    public T loadSync() throws BazaarException {
        ConversationsResponseBase conversationResponse = null;
        BazaarException error = submission.getError();

        if (error != null) {
            throw error;
        }

        try {
            List<PhotoUpload> photoUploads = submission.getBuilder().photoUploads;
            if (photoUploads != null && photoUploads.size() > 0 && !submission.getForcePreview() && submission.getBuilder().getAction() == Action.Submit) {

                return postPhotosAndSubmissionSync(photoUploads, submission);

            } else {
                Response response = call.execute();
                conversationResponse = deserializeAndCloseResponse(response);
                if (conversationResponse.getHasErrors()) {
                    throw new BazaarException(gson.toJson(conversationResponse.getErrors()));
                }
                if (submission.getForcePreview()) {
                    LoadCall loadCall = BVConversationsClient.reCreateCallNoPreview(c, submission);
                    return (T) loadCall.loadSync();
                }
            }

        } catch (Throwable t) {
            throw new BazaarException(t.getMessage());
        }
        return (T) conversationResponse;
    }

    private T postPhotosAndSubmissionSync(List<PhotoUpload> photoUploads, ConversationsSubmission submission) throws BazaarException {
        Log.d("Submission", String.format("Preparing to submit %d photos", photoUploads.size()));
        final List<Photo> photos = new ArrayList<>();

        try {
            for (PhotoUpload upload : photoUploads) {
                Call photoCall = makePhotoCall(upload);
                Response response = photoCall.execute();
                Photo photo = deserializePhotoResponse(response);
                photo.setCaption(upload.getCaption());
                photos.add(photo);
            }
        } catch (Throwable e) {
            throw new BazaarException(e.getMessage());
        }

        LoadCall loadCall = BVConversationsClient.reCreateCallWithPhotos(c, submission, photos);
        return (T) loadCall.loadSync();
    }

    private void postPhotosAndSubmissionAsync(final ConversationsCallback<T> conversationsCallback, final List<PhotoUpload> photoUploads, final ConversationsSubmission submission) {
        Logger.d("Submission", String.format("Preparing to submit %d photos", photoUploads.size()));
        final List<Photo> photos = Collections.synchronizedList(new ArrayList<Photo>());

        for (final PhotoUpload upload : photoUploads) {
            Call photoCall = makePhotoCall(upload);
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
                        //whenever we have received successful responses for every expect photo upload we can
                        // reconstruct the submission request with the photo upload response details
                        if (photos.size() == photoUploads.size()) {
                            LoadCall loadCall = BVConversationsClient.reCreateCallWithPhotos(c, submission, photos);
                            loadCall.loadAsync(conversationsCallback);
                        }
                    } catch (BazaarException e) {
                        errorOnMainThread(conversationsCallback, e);
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
            throw new BazaarException(gson.toJson(photoUploadResponse.getErrors()));
        }
        return photoUploadResponse.getPhoto();
    }

    @Override
    public void loadAsync(final ConversationsCallback<T> conversationsCallback) {
        BazaarException error = submission.getError();

        if (error != null) {
            errorOnMainThread(conversationsCallback, error);
            return;
        }

        List<PhotoUpload> photoUploads = submission.getBuilder().photoUploads;
        //At this point we assume know that since we are submitting and not being forced to preview that it has been previewed already.
        // The request is ready to have it's photos uploaded then submitted.
        if (photoUploads != null && photoUploads.size() > 0 && !submission.getForcePreview() && submission.getBuilder().getAction() == Action.Submit) {

            postPhotosAndSubmissionAsync(conversationsCallback, photoUploads, submission);

        } else {
            //At this point we know that the submission needs to be previewed
            //Either by user asking for preview or being forced to preview before Submit
            //Or it is ready for full submission (it's had it's photo's uploaded or never had any)
            this.call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) {
                    ConversationsResponseBase conversationResponse = null;
                    BazaarException error = null;
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
                        errorOnMainThread(conversationsCallback, error);
                    }else {
                        //if User intended to only Preview or if a Submit has already been previewed we are done and can callback
                        if (submission.getBuilder().getAction() == Action.Preview || (!submission.getForcePreview() && submission.getBuilder().getAction() == Action.Submit)) {
                            successOnMainThread(conversationsCallback, conversationResponse);
                        }
                        //We know that a Submit was succesfully previewed so now we Submit for real
                        else {
                            LoadCall newCall = BVConversationsClient.reCreateCallNoPreview(c, submission);
                            newCall.loadAsync(conversationsCallback);
                        }
                    }
                }
            });
        }
    }

    private Call makePhotoCall(PhotoUpload upload) {
        String fullUrl = String.format("%s%s", BVConversationsClient.conversationsBaseUrl, upload.getEndPoint());
        RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(ConversationsBase.kAPI_VERSION, ConversationsBase.API_VERSION)
                .addFormDataPart(ConversationsBase.kPASS_KEY, BVSDK.getInstance().getApiKeyConversations())
                .addFormDataPart(PhotoUpload.kCONTENT_TYPE, upload.getContentType().getKey())
                .addFormDataPart("photo", "photo.png", RequestBody.create(MEDIA_TYPE_PNG, upload.getPhotoFile())).build();

        Request request = new Request.Builder()
                .url(fullUrl)
                .post(req)
                .build();

        return okHttpClient.newCall(request);
    }
}
