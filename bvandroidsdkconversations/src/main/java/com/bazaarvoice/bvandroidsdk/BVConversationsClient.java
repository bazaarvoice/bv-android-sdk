/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.util.Log;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Class used to perform Conversations requests. It is recommended to BVConversationsClient as a singleton.
 */
public final class BVConversationsClient {
//    private final static String REQUEST

    private static final OkHttpClient okHttpClient = BVSDK.getInstance().getOkHttpClient();
    static final String conversationsBaseUrl = BVSDK.getInstance().getConversationsApiRootUrl();
    private static final MediaType URL_ENCODED = MediaType.parse("application/x-www-form-urlencoded");

    /**
     * @param request QuestionAndAnswerRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<QuestionAndAnswerResponse> prepareCall(QuestionAndAnswerRequest request) {
        return createCall(QuestionAndAnswerResponse.class, request);
    }

    /**
     * @param request ProductDisplayPageRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<ProductDisplayPageResponse> prepareCall(ProductDisplayPageRequest request) {
        return createCall(ProductDisplayPageResponse.class, request);
    }

    /**
     * @param request BulkRatingsRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<BulkRatingsResponse> prepareCall(BulkRatingsRequest request) {
        return createCall(BulkRatingsResponse.class, request);
    }

    /**
     * @param request ReviewsRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<ReviewResponse> prepareCall(ReviewsRequest request) {
        return createCall(ReviewResponse.class, request);
    }

    public LoadCallSubmission<AnswerSubmissionResponse> prepareCall(AnswerSubmission submission) {
        return createCall(AnswerSubmissionResponse.class, submission);
    }

    public LoadCallSubmission<ReviewSubmissionResponse> prepareCall(ReviewSubmission submission) {
        return createCall(ReviewSubmissionResponse.class, submission);
    }

    public LoadCallSubmission<QuestionSubmissionResponse> prepareCall(QuestionSubmission submission) {
        return createCall(QuestionSubmissionResponse.class, submission);
    }

    private <T> LoadCallDisplay<T> createCall(Class<T> c, ConversationsRequest request) {
        String fullUrl = String.format("%s%s?%s", conversationsBaseUrl, request.getEndPoint(), request.getUrlQueryString());
        Logger.d("url", fullUrl);
        Request okRequest = new Request.Builder()
                .url(fullUrl)
                .build();
        return new LoadCallDisplay<T>(request, c, okHttpClient.newCall(okRequest));
    }

    private static <T> LoadCallSubmission<T> createCall(Class<T> c, ConversationsSubmission submission) {
        if (submission.getBuilder().getAction() == Action.Submit) {
            submission.setForcePreview(true);
        }
        return loadCallFromSubmission(c, submission);
    }

    static <T> LoadCallSubmission<T> loadCallFromSubmission(Class<T> c, ConversationsSubmission submission) {
        String fullUrl = String.format("%s%s", conversationsBaseUrl, submission.getEndPoint());
        Log.d("url", fullUrl);

        String json = submission.getUrlQueryString();
        RequestBody body = RequestBody.create(URL_ENCODED, json);
        Request okRequest = new Request.Builder()
                .post(body)
                .addHeader("Content-type", "application/x-www-form-urlencoded")
                .url(fullUrl)
                .build();
        return new LoadCallSubmission<T>(submission, c, okHttpClient.newCall(okRequest));
    }

    static <T> LoadCall<T> reCreateCallWithPhotos(Class<T> c, ConversationsSubmission submission, List<Photo> photos) {
        submission.setPhotos(photos);
        submission.getBuilder().photoUploads.clear();
        submission.setForcePreview(false);

        LoadCall loadCall = loadCallFromSubmission(c, submission);
        return loadCall;
    }

    static <T> LoadCall<T> reCreateCallNoPreview(Class<T> c, ConversationsSubmission submission) {
        submission.setForcePreview(false);
        return loadCallFromSubmission(c, submission);
    }






}