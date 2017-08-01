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

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Class used to perform Conversations requests. It is recommended to use
 * BVConversationsClient as a single instance in your app.
 */
public final class BVConversationsClient {
    private static final OkHttpClient okHttpClient = BVSDK.getInstance().getBvWorkerData().getOkHttpClient();
    private static final String CONVERSATIONS_RELATIVE_URL = "data/";
    static final String conversationsBaseUrl = BVSDK.getInstance().getBvWorkerData().getRootApiUrls().getBazaarvoiceApiRootUrl() + CONVERSATIONS_RELATIVE_URL;

    private final RequestFactory requestFactory;
    private final BVConfig bvConfig;
    private final ConversationsAnalyticsManager conversationsAnalyticsManager;

    /**
     * @deprecated Now use the {@link Builder} which explicitly requires the {@link BVSDK} singleton instance.
     *
     * Builds a instance with the {@link BVSDK} defaults
     */
    public BVConversationsClient() {
        BVConversationsClient client = new BVConversationsClient.Builder(BVSDK.getInstance()).build();
        bvConfig = client.bvConfig;
        requestFactory = client.requestFactory;
        conversationsAnalyticsManager = client.conversationsAnalyticsManager;
    }

    private BVConversationsClient(Builder builder) {
        bvConfig = builder.bvConfig;
        requestFactory = builder.requestFactory;
        conversationsAnalyticsManager = builder.conversationsAnalyticsManager;
    }

    /**
     * @param request QuestionAndAnswerRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<QuestionAndAnswerRequest, QuestionAndAnswerResponse> prepareCall(QuestionAndAnswerRequest request) {
        return factoryCreateDisplayCall(QuestionAndAnswerResponse.class, request, requestFactory, conversationsAnalyticsManager);
    }

    /**
     * @param request ProductDisplayPageRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<ProductDisplayPageRequest, ProductDisplayPageResponse> prepareCall(ProductDisplayPageRequest request) {
        return factoryCreateDisplayCall(ProductDisplayPageResponse.class, request, requestFactory, conversationsAnalyticsManager);
    }

    public LoadCallDisplay<BulkProductRequest, BulkProductResponse> prepareCall(BulkProductRequest request) {
        return factoryCreateDisplayCall(BulkProductResponse.class, request, requestFactory, conversationsAnalyticsManager);
    }

    /**
     * @param request BulkRatingsRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<BulkRatingsRequest, BulkRatingsResponse> prepareCall(BulkRatingsRequest request) {
        return factoryCreateDisplayCall(BulkRatingsResponse.class, request, requestFactory, conversationsAnalyticsManager);
    }

    /**
     * @param request BulkStoreRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<BulkStoreRequest, BulkStoreResponse> prepareCall(BulkStoreRequest request) {
        return factoryCreateDisplayCall(BulkStoreResponse.class, request, requestFactory, conversationsAnalyticsManager);
    }

    /**
     * @param request ReviewsRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<ReviewsRequest, ReviewResponse> prepareCall(ReviewsRequest request) {
        return factoryCreateDisplayCall(ReviewResponse.class, request, requestFactory, conversationsAnalyticsManager);
    }

    public LoadCallDisplay<StoreReviewsRequest, StoreReviewResponse> prepareCall(StoreReviewsRequest request) {
        return factoryCreateDisplayCall(StoreReviewResponse.class, request, requestFactory, conversationsAnalyticsManager);
    }

    public LoadCallDisplay<AuthorsRequest, AuthorsResponse> prepareCall(AuthorsRequest request) {
        return factoryCreateDisplayCall(AuthorsResponse.class, request, requestFactory, conversationsAnalyticsManager);
    }

    public LoadCallDisplay<CommentsRequest, CommentsResponse> prepareCall(CommentsRequest request) {
        return factoryCreateDisplayCall(CommentsResponse.class, request, requestFactory, conversationsAnalyticsManager);
    }

    public interface DisplayLoader<RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> {
        void loadAsync(LoadCallDisplay<RequestType, ResponseType> call, ConversationsCallback<ResponseType> callback);
    }

    public LoadCallSubmission<AnswerSubmissionRequest, AnswerSubmissionResponse> prepareCall(AnswerSubmissionRequest submission) {
        return factoryCreateSubmissionCall(AnswerSubmissionResponse.class, submission, requestFactory, conversationsAnalyticsManager, bvConfig.getApiKeyConversations());
    }

    public LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> prepareCall(ReviewSubmissionRequest submission) {
        return factoryCreateSubmissionCall(ReviewSubmissionResponse.class, submission, requestFactory, conversationsAnalyticsManager, bvConfig.getApiKeyConversations());
    }

    public LoadCallSubmission<StoreReviewSubmissionRequest, StoreReviewSubmissionResponse> prepareCall(StoreReviewSubmissionRequest submission) {
        return factoryCreateSubmissionCall(StoreReviewSubmissionResponse.class, submission, requestFactory, conversationsAnalyticsManager, bvConfig.getApiKeyConversationsStores());
    }

    public LoadCallSubmission<QuestionSubmissionRequest, QuestionSubmissionResponse> prepareCall(QuestionSubmissionRequest submission) {
        return factoryCreateSubmissionCall(QuestionSubmissionResponse.class, submission, requestFactory, conversationsAnalyticsManager, bvConfig.getApiKeyConversations());
    }

    public LoadCallSubmission<FeedbackSubmissionRequest, FeedbackSubmissionResponse> prepareCall(FeedbackSubmissionRequest submission) {
        return factoryCreateSubmissionCall(FeedbackSubmissionResponse.class, submission, requestFactory, conversationsAnalyticsManager, bvConfig.getApiKeyConversations());
    }

    public LoadCallSubmission<CommentSubmissionRequest, CommentSubmissionResponse> prepareCall(CommentSubmissionRequest submission) {
        return factoryCreateSubmissionCall(CommentSubmissionResponse.class, submission, requestFactory, conversationsAnalyticsManager, bvConfig.getApiKeyConversations());
    }

    private static <RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> LoadCallDisplay<RequestType, ResponseType> factoryCreateDisplayCall(Class<ResponseType> responseTypeClass, RequestType request, RequestFactory requestFactory, ConversationsAnalyticsManager conversationsAnalyticsManager) {
        final Request okRequest = requestFactory.create(request);
        return new LoadCallDisplay<RequestType, ResponseType>(request, responseTypeClass, okHttpClient.newCall(okRequest), conversationsAnalyticsManager);
    }

    private static <RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> LoadCallSubmission<RequestType, ResponseType> factoryCreateSubmissionCall(Class<ResponseType> responseTypeClass, RequestType request, RequestFactory requestFactory, ConversationsAnalyticsManager conversationsAnalyticsManager, String apiKey) {
        if (request.getAction() == Action.Submit) {
            request.setForcePreview(true);
        }
        return loadCallFromSubmission(responseTypeClass, request, requestFactory, conversationsAnalyticsManager, apiKey);
    }

    static <RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> LoadCallSubmission<RequestType, ResponseType>   loadCallFromSubmission(Class<ResponseType> responseTypeClass, RequestType request, RequestFactory requestFactory, ConversationsAnalyticsManager conversationsAnalyticsManager, String apiKey) {
        final Request okRequest = requestFactory.create(request);
        return new LoadCallSubmission<RequestType, ResponseType>(request, responseTypeClass, okHttpClient.newCall(okRequest), conversationsAnalyticsManager, requestFactory, apiKey);
    }

    // TODO: Remove as part of photo request refactor
    static <RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> LoadCall<RequestType, ResponseType> reCreateCallWithPhotos(Class<ResponseType> responseTypeClass, RequestType submission, List<Photo> photos, RequestFactory requestFactory, ConversationsAnalyticsManager conversationsAnalyticsManager, String apiKey) {
        submission.setPhotos(photos);
        submission.getBuilder().photoUploads.clear();
        submission.setForcePreview(false);

        return loadCallFromSubmission(responseTypeClass, submission, requestFactory, conversationsAnalyticsManager, apiKey);
    }

    // TODO: Remove as part of photo request refactor
    static <RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> LoadCall<RequestType, ResponseType> reCreateCallNoPreview(Class<ResponseType> responseTypeClass, RequestType submission, RequestFactory requestFactory, ConversationsAnalyticsManager conversationsAnalyticsManager, String apiKey) {
        submission.setForcePreview(false);
        return loadCallFromSubmission(responseTypeClass, submission, requestFactory, conversationsAnalyticsManager, apiKey);
    }

    /**
     * Builds a instance with the {@link BVSDK} defaults, and/or
     * optional configuration values.
     */
    public static class Builder {
        private final BVSDK bvsdk;
        private BVConfig bvConfig;
        private RequestFactory requestFactory;
        private ConversationsAnalyticsManager conversationsAnalyticsManager;

        public Builder(BVSDK bvsdk) {
            this.bvsdk = bvsdk;
        }

        public Builder bvConfig(BVConfig bvConfig) {
            this.bvConfig = bvConfig;
            return this;
        }

        public BVConversationsClient build() {
            if (bvConfig == null) {
                this.bvConfig = bvsdk.getBvUserProvidedData().getBvConfig();
            }

            final BVMobileInfo bvMobileInfo = bvsdk.getBvUserProvidedData().getBvMobileInfo();
            final BVRootApiUrls bvRootApiUrls = bvsdk.getBvWorkerData().getRootApiUrls();
            final String bvSdkUserAgent = bvsdk.getBvWorkerData().getBvSdkUserAgent();
            requestFactory = new RequestFactory(bvMobileInfo, bvRootApiUrls, bvConfig, bvSdkUserAgent);

            final String clientId = bvConfig.getClientId();
            final BVPixel bvPixel = bvsdk.getBvPixel();
            conversationsAnalyticsManager = new ConversationsAnalyticsManager(bvPixel, clientId);

            return new BVConversationsClient(this);
        }
    }
}