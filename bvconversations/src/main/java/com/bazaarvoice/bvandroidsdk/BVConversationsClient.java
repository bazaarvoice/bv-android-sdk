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

import android.util.Log;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Class used to perform Conversations requests. It is recommended to use
 * BVConversationsClient as a single instance in your app.
 */
public final class BVConversationsClient {
    private static final OkHttpClient okHttpClient = BVSDK.getInstance().getBvWorkerData().getOkHttpClient();
    private static final BVLogger BV_LOGGER = BVSDK.getInstance().getBvLogger();
    private static final String CONVERSATIONS_RELATIVE_URL = "data/";
    static final String conversationsBaseUrl = BVSDK.getInstance().getBvWorkerData().getRootApiUrls().getBazaarvoiceApiRootUrl() + CONVERSATIONS_RELATIVE_URL;
    private static final MediaType URL_ENCODED = MediaType.parse("application/x-www-form-urlencoded");

    /**
     * @param request QuestionAndAnswerRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<QuestionAndAnswerRequest, QuestionAndAnswerResponse> prepareCall(QuestionAndAnswerRequest request) {
        return createCall(QuestionAndAnswerResponse.class, request);
    }

    /**
     * @param request ProductDisplayPageRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<ProductDisplayPageRequest, ProductDisplayPageResponse> prepareCall(ProductDisplayPageRequest request) {
        return createCall(ProductDisplayPageResponse.class, request);
    }

    public LoadCallDisplay<BulkProductRequest, BulkProductResponse> prepareCall(BulkProductRequest request) {
        return createCall(BulkProductResponse.class, request);
    }

    /**
     * @param request BulkRatingsRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<BulkRatingsRequest, BulkRatingsResponse> prepareCall(BulkRatingsRequest request) {
        return createCall(BulkRatingsResponse.class, request);
    }

    /**
     * @param request BulkStoreRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<BulkStoreRequest, BulkStoreResponse> prepareCall(BulkStoreRequest request) {
        return createCall(BulkStoreResponse.class, request);
    }

    /**
     * @param request ReviewsRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<ReviewsRequest, ReviewResponse> prepareCall(ReviewsRequest request) {
        return createCall(ReviewResponse.class, request);
    }

    public LoadCallDisplay<StoreReviewsRequest, StoreReviewResponse> prepareCall(StoreReviewsRequest request) {
        return createCall(StoreReviewResponse.class, request);
    }

    public LoadCallDisplay<AuthorsRequest, AuthorsResponse> prepareCall(AuthorsRequest request) {
        return createCall(AuthorsResponse.class, request);
    }

    public LoadCallDisplay<CommentsRequest, CommentsResponse> prepareCall(CommentsRequest request) {
        return createCall(CommentsResponse.class, request);
    }

    public interface DisplayLoader<RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> {
        void loadAsync(LoadCallDisplay<RequestType, ResponseType> call, ConversationsCallback<ResponseType> callback);
    }

    public LoadCallSubmission<AnswerSubmissionRequest, AnswerSubmissionResponse> prepareCall(AnswerSubmissionRequest submission) {
        return createCall(AnswerSubmissionResponse.class, submission);
    }

    public LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> prepareCall(ReviewSubmissionRequest submission) {
        return createCall(ReviewSubmissionResponse.class, submission);
    }

    public LoadCallSubmission<StoreReviewSubmissionRequest, StoreReviewSubmissionResponse> prepareCall(StoreReviewSubmissionRequest submission) {
        return createCall(StoreReviewSubmissionResponse.class, submission);
    }

    public LoadCallSubmission<QuestionSubmissionRequest, QuestionSubmissionResponse> prepareCall(QuestionSubmissionRequest submission) {
        return createCall(QuestionSubmissionResponse.class, submission);
    }

    public LoadCallSubmission<FeedbackSubmissionRequest, FeedbackSubmissionResponse> prepareCall(FeedbackSubmissionRequest submission) {
        return createCall(FeedbackSubmissionResponse.class, submission);
    }

    private <RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> LoadCallDisplay<RequestType, ResponseType> createCall(Class<ResponseType> responseTypeClass, RequestType request) {
        HttpUrl httpUrl = request.toHttpUrl();
        BV_LOGGER.d("url", httpUrl.toString());
        Request okRequest = new Request.Builder()
                .addHeader("User-Agent", BVSDK.getInstance().getBvWorkerData().getBvSdkUserAgent())
                .url(httpUrl)
                .build();
        return new LoadCallDisplay<RequestType, ResponseType>(request, responseTypeClass, okHttpClient.newCall(okRequest));
    }

    private static <RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> LoadCallSubmission<RequestType, ResponseType> createCall(Class<ResponseType> responseTypeClass, RequestType submission) {
        if (submission.getBuilder().getAction() == Action.Submit) {
            submission.setForcePreview(true);
        }
        return loadCallFromSubmission(responseTypeClass, submission);
    }

    static <RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> LoadCallSubmission<RequestType, ResponseType> loadCallFromSubmission(Class<ResponseType> responseTypeClass, RequestType submission) {
        String fullUrl = String.format("%s%s", conversationsBaseUrl, submission.getEndPoint());
        Log.d("url", fullUrl);

        String json = submission.getUrlQueryString();
        RequestBody body = RequestBody.create(URL_ENCODED, json);
        Request okRequest = new Request.Builder()
                .post(body)
                .addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("User-Agent", BVSDK.getInstance().getBvWorkerData().getBvSdkUserAgent())
                .url(fullUrl)
                .build();
        return new LoadCallSubmission<>(submission, responseTypeClass, okHttpClient.newCall(okRequest));
    }

    static <RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> LoadCall<RequestType, ResponseType> reCreateCallWithPhotos(Class<ResponseType> responseTypeClass, RequestType submission, List<Photo> photos) {
        submission.setPhotos(photos);
        submission.getBuilder().photoUploads.clear();
        submission.setForcePreview(false);

        LoadCall loadCall = loadCallFromSubmission(responseTypeClass, submission);
        return loadCall;
    }

    static <RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> LoadCall<RequestType, ResponseType> reCreateCallNoPreview(Class<ResponseType> responseTypeClass, RequestType submission) {
        submission.setForcePreview(false);
        return loadCallFromSubmission(responseTypeClass, submission);
    }
}