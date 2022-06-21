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

import android.os.Looper;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Class used to perform Conversations requests. It is recommended to use
 * BVConversationsClient as a single instance in your app.
 */
public final class BVConversationsClient {
    private final RequestFactory requestFactory;
    private final BVConfig bvConfig;
    private final ConversationsAnalyticsManager conversationsAnalyticsManager;
    private final Looper bgLooper;
    private final Looper uiLooper;
    private final OkHttpClient okHttpClient;
    private final Gson gson;

    /**
     * @deprecated Now use the {@link Builder} which explicitly requires the {@link BVSDK} singleton instance.
     * Builds a instance with the {@link BVSDK} defaults
     */
    public BVConversationsClient() {
        BVConversationsClient client = new BVConversationsClient.Builder(BVSDK.getInstance()).build();
        bvConfig = client.bvConfig;
        requestFactory = client.requestFactory;
        conversationsAnalyticsManager = client.conversationsAnalyticsManager;
        bgLooper = client.bgLooper;
        uiLooper = client.uiLooper;
        okHttpClient = client.okHttpClient;
        gson = client.gson;
    }

    private BVConversationsClient(Builder builder) {
        bvConfig = builder.bvConfig;
        requestFactory = builder.requestFactory;
        conversationsAnalyticsManager = builder.conversationsAnalyticsManager;
        bgLooper = builder.bgLooper;
        uiLooper = builder.uiLooper;
        okHttpClient = builder.okHttpClient;
        gson = builder.gson;
    }

    /**
     * @param request QuestionAndAnswerRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<QuestionAndAnswerRequest, QuestionAndAnswerResponse> prepareCall(QuestionAndAnswerRequest request) {
        return factoryCreateDisplayCall(QuestionAndAnswerResponse.class, request);
    }

    /**
     * @param request ReviewHighlightRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<ReviewHighlightsRequest, ReviewHighlightsResponse> prepareCall(ReviewHighlightsRequest request) {
        return factoryCreateDisplayCall(ReviewHighlightsResponse.class, request);
    }

    /**
     * @param request ProductDisplayPageRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<ProductDisplayPageRequest, ProductDisplayPageResponse> prepareCall(ProductDisplayPageRequest request) {
        return factoryCreateDisplayCall(ProductDisplayPageResponse.class, request);
    }

    public LoadCallDisplay<BulkProductRequest, BulkProductResponse> prepareCall(BulkProductRequest request) {
        return factoryCreateDisplayCall(BulkProductResponse.class, request);
    }

    /**
     * @param request BulkRatingsRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<BulkRatingsRequest, BulkRatingsResponse> prepareCall(BulkRatingsRequest request) {
        return factoryCreateDisplayCall(BulkRatingsResponse.class, request);
    }

    /**
     * @param request BulkStoreRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<BulkStoreRequest, BulkStoreResponse> prepareCall(BulkStoreRequest request) {
        return factoryCreateDisplayCall(BulkStoreResponse.class, request);
    }

    /**
     * @param request ReviewsRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<ReviewsRequest, ReviewResponse> prepareCall(ReviewsRequest request) {
        return factoryCreateDisplayCall(ReviewResponse.class, request);
    }
    /**
     * @param request FeatureRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public LoadCallDisplay<FeaturesRequest, FeaturesResponse> prepareCall(FeaturesRequest request) {
        return factoryCreateDisplayCall(FeaturesResponse.class, request);
    }

    public LoadCallDisplay<StoreReviewsRequest, StoreReviewResponse> prepareCall(StoreReviewsRequest request) {
        return factoryCreateDisplayCall(StoreReviewResponse.class, request);
    }

    public LoadCallDisplay<AuthorsRequest, AuthorsResponse> prepareCall(AuthorsRequest request) {
        return factoryCreateDisplayCall(AuthorsResponse.class, request);
    }

    public LoadCallDisplay<CommentsRequest, CommentsResponse> prepareCall(CommentsRequest request) {
        return factoryCreateDisplayCall(CommentsResponse.class, request);
    }

    public LoadCallSubmission<AnswerSubmissionRequest, AnswerSubmissionResponse> prepareCall(AnswerSubmissionRequest submission) {
        return factoryCreateSubmissionCall(AnswerSubmissionResponse.class, submission);
    }

    public LoadCallSubmission<ReviewSubmissionRequest, ReviewSubmissionResponse> prepareCall(ReviewSubmissionRequest submission) {
        return factoryCreateSubmissionCall(ReviewSubmissionResponse.class, submission);
    }

    public LoadCallSubmission<StoreReviewSubmissionRequest, StoreReviewSubmissionResponse> prepareCall(StoreReviewSubmissionRequest submission) {
        return factoryCreateSubmissionCall(StoreReviewSubmissionResponse.class, submission);
    }

    public LoadCallSubmission<QuestionSubmissionRequest, QuestionSubmissionResponse> prepareCall(QuestionSubmissionRequest submission) {
        return factoryCreateSubmissionCall(QuestionSubmissionResponse.class, submission);
    }

    public LoadCallSubmission<FeedbackSubmissionRequest, FeedbackSubmissionResponse> prepareCall(FeedbackSubmissionRequest submission) {
        return factoryCreateSubmissionCall(FeedbackSubmissionResponse.class, submission);
    }

    public LoadCallSubmission<CommentSubmissionRequest, CommentSubmissionResponse> prepareCall(CommentSubmissionRequest submission) {
        return factoryCreateSubmissionCall(CommentSubmissionResponse.class, submission);
    }

    public LoadCallSubmission<UserAuthenticationStringRequest, UserAuthenticationStringResponse> prepareCall(UserAuthenticationStringRequest request) {
        return factoryCreateSubmissionCall(UserAuthenticationStringResponse.class, request);
    }

    public LoadCallProgressiveSubmission<PhotoUploadRequest, PhotoUploadResponse> prepareCall(PhotoUploadRequest request){
        return factoryCreateProgressiveSubmissionCall(PhotoUploadResponse.class, request);
    }

    public LoadCallProgressiveSubmission<InitiateSubmitRequest, InitiateSubmitResponse> prepareCall(InitiateSubmitRequest request) {
        return factoryCreateProgressiveSubmissionCall(InitiateSubmitResponse.class, request);
    }

    public LoadCallProgressiveSubmission<ProgressiveSubmitRequest, ProgressiveSubmitResponse> prepareCall(ProgressiveSubmitRequest request) {
        return factoryCreateProgressiveSubmissionCall(ProgressiveSubmitResponse.class, request);
    }

    private <RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> LoadCallDisplay<RequestType, ResponseType> factoryCreateDisplayCall(Class<ResponseType> responseTypeClass, RequestType request) {
        final Request okRequest = requestFactory.create(request);
        return new LoadCallDisplay<>(request, responseTypeClass, okHttpClient.newCall(okRequest), conversationsAnalyticsManager, okHttpClient, gson, uiLooper, bgLooper);
    }

    private <RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> LoadCallSubmission<RequestType, ResponseType> factoryCreateSubmissionCall(Class<ResponseType> responseTypeClass, RequestType request) {
        if (request.getAction() == Action.Submit) {
            request.setForcePreview(true);
        }
        return loadCallFromSubmission(responseTypeClass, request);
    }

    private <RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> LoadCallProgressiveSubmission<RequestType, ResponseType> factoryCreateProgressiveSubmissionCall(Class<ResponseType> responseTypeClass, RequestType request) {
        final Request okRequest = requestFactory.create(request);
        return new LoadCallProgressiveSubmission<>(request, responseTypeClass,conversationsAnalyticsManager , uiLooper, bgLooper, okHttpClient, gson,  okHttpClient.newCall(okRequest));
    }


    private <RequestType extends ConversationsSubmissionRequest, ResponseType extends ConversationsResponse> LoadCallSubmission<RequestType, ResponseType> loadCallFromSubmission(Class<ResponseType> responseTypeClass, RequestType request) {
        return new LoadCallSubmission<>(request, responseTypeClass, conversationsAnalyticsManager, requestFactory, bgLooper, uiLooper, okHttpClient, gson);
    }

    public interface DisplayLoader<RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> {
        void loadAsync(LoadCallDisplay<RequestType, ResponseType> call, ConversationsCallback<ResponseType> callback);

        void loadAsync(LoadCallDisplay<RequestType, ResponseType> call, ConversationsDisplayCallback<ResponseType> callback);
    }

    /**
     * Builds a instance with the {@link BVSDK} defaults, and/or
     * optional configuration values.
     */
    public static class Builder {
        private final BVSDK bvsdk;
        private Looper bgLooper;
        private Looper uiLooper;
        private final OkHttpClient okHttpClient;
        private final Gson gson;
        private BVConfig bvConfig;
        private RequestFactory requestFactory;
        private ConversationsAnalyticsManager conversationsAnalyticsManager;
        private FingerprintProvider fingerprintProvider;

        public Builder(BVSDK bvsdk) {
            this.bvsdk = bvsdk;
            final BVSDK.BVWorkerData bvWorkerData = bvsdk.getBvWorkerData();
            this.bgLooper = bvWorkerData.getBackgroundLooper();
            this.uiLooper = Looper.getMainLooper();
            this.okHttpClient = bvWorkerData.getOkHttpClient();
            this.gson = bvWorkerData.getGson();
        }

        public Builder bvConfig(BVConfig bvConfig) {
            this.bvConfig = bvConfig;
            return this;
        }

        public Builder fingerprintProvider(FingerprintProvider fingerprintProvider) {
            this.fingerprintProvider = fingerprintProvider;
            return this;
        }

        Builder bgLooper(Looper bgLooper) {
            this.bgLooper = bgLooper;
            return this;
        }

        Builder uiLooper(Looper uiLooper) {
            this.uiLooper = uiLooper;
            return this;
        }

        public BVConversationsClient build() {
            if (bvConfig == null) {
                this.bvConfig = bvsdk.getBvUserProvidedData().getBvConfig();
            }

            if (fingerprintProvider == null) {
                fingerprintProvider = FingerprintProvider.EMPTY;
            }

            final BVMobileInfo bvMobileInfo = bvsdk.getBvUserProvidedData().getBvMobileInfo();
            final BVRootApiUrls bvRootApiUrls = bvsdk.getBvWorkerData().getRootApiUrls();
            final String bvSdkUserAgent = bvsdk.getBvWorkerData().getBvSdkUserAgent();
            requestFactory = new BasicRequestFactory(bvMobileInfo, bvRootApiUrls, bvConfig, bvSdkUserAgent, fingerprintProvider);

            final String clientId = bvConfig.getClientId();
            final BVPixel bvPixel = bvsdk.getBvPixel();
            conversationsAnalyticsManager = new ConversationsAnalyticsManager(bvPixel, clientId);

            return new BVConversationsClient(this);
        }
    }
}