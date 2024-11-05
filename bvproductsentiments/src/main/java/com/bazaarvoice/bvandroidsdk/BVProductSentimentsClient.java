/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.os.Looper;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Bazaarvoice ProductSentiments API for getting custom product productsentiments
 */
public class BVProductSentimentsClient {

    private final ProductSentimentRequestFactory productSentimentRequestFactory;
    private final BVConfig bvConfig;
    private final ProductSentimentsAnalyticsManager productSentimentsAnalyticsManager;
    private final Looper bgLooper;
    private final Looper uiLooper;
    private final OkHttpClient okHttpClient;
    private final Gson gson;

    /**
     * @deprecated Now use the {@link Builder} which explicitly requires the {@link BVSDK} singleton instance.
     * Builds a instance with the {@link BVSDK} defaults
     */
    public BVProductSentimentsClient() {
        BVProductSentimentsClient client = new BVProductSentimentsClient.Builder(BVSDK.getInstance()).build();
        bvConfig = client.bvConfig;
        productSentimentRequestFactory = client.productSentimentRequestFactory;
        productSentimentsAnalyticsManager = client.productSentimentsAnalyticsManager;
        bgLooper = client.bgLooper;
        uiLooper = client.uiLooper;
        okHttpClient = client.okHttpClient;
        gson = client.gson;
    }

    private BVProductSentimentsClient(Builder builder) {
        bvConfig = builder.bvConfig;
        productSentimentRequestFactory = builder.productSentimentRequestFactory;
        productSentimentsAnalyticsManager = builder.productSentimentsAnalyticsManager;
        bgLooper = builder.bgLooper;
        uiLooper = builder.uiLooper;
        okHttpClient = builder.okHttpClient;
        gson = builder.gson;
    }
    /**
     * @param request SummarisedFeatureRequest to be sent
     * @return LoadCall object with the request ready to be sent
     */
    public ProductSentimentsLoadCall<SummarisedFeaturesRequest, SummarisedFeaturesResponse> prepareCall(SummarisedFeaturesRequest request) {
        return factoryCreateCall(SummarisedFeaturesResponse.class, request);
    }
    public ProductSentimentsLoadCall<SummarisedFeaturesQuotesRequest, SummarisedFeaturesQuotesResponse> prepareCall(SummarisedFeaturesQuotesRequest request) {
        return factoryCreateCall(SummarisedFeaturesQuotesResponse.class, request);
    }
    public ProductSentimentsLoadCall<FeaturesSentimentRequest, FeaturesSentimentResponse> prepareCall(FeaturesSentimentRequest request) {
        return factoryCreateCall(FeaturesSentimentResponse.class, request);
    }
    public ProductSentimentsLoadCall<QuotesSentimentRequest, QuotesSentimentResponse> prepareCall(QuotesSentimentRequest request) {
        return factoryCreateCall(QuotesSentimentResponse.class, request);
    }
    public ProductSentimentsLoadCall<ExpressionsSentimentRequest, ExpressionsSentimentResponse> prepareCall(ExpressionsSentimentRequest request) {
        return factoryCreateCall(ExpressionsSentimentResponse.class, request);
    }

    private <RequestType extends ProductSentimentsRequest, ResponseType extends ProductSentimentsResponse> ProductSentimentsLoadCall<RequestType, ResponseType> factoryCreateCall(Class<ResponseType> responseTypeClass, RequestType request) {
        final Request okRequest = productSentimentRequestFactory.create(request);
        return new ProductSentimentsLoadCall<>(request, responseTypeClass,productSentimentsAnalyticsManager , uiLooper, bgLooper, okHttpClient, gson,  okHttpClient.newCall(okRequest));
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
        private ProductSentimentRequestFactory productSentimentRequestFactory;
        private ProductSentimentsAnalyticsManager productSentimentsAnalyticsManager;
        private ProductSentimentsFingerprintProvider productSentimentsFingerprintProvider;

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

        public Builder fingerprintProvider(ProductSentimentsFingerprintProvider productSentimentsFingerprintProvider) {
            this.productSentimentsFingerprintProvider = productSentimentsFingerprintProvider;
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

        public BVProductSentimentsClient build() {
            if (bvConfig == null) {
                this.bvConfig = bvsdk.getBvUserProvidedData().getBvConfig();
            }

            if (productSentimentsFingerprintProvider == null) {
                productSentimentsFingerprintProvider = ProductSentimentsFingerprintProvider.EMPTY;
            }

            final BVMobileInfo bvMobileInfo = bvsdk.getBvUserProvidedData().getBvMobileInfo();
            final BVRootApiUrls bvRootApiUrls = bvsdk.getBvWorkerData().getRootApiUrls();
            final String bvSdkUserAgent = bvsdk.getBvWorkerData().getBvSdkUserAgent();
            productSentimentRequestFactory = new ProductSentimentsBasicRequestFactory(bvMobileInfo, bvRootApiUrls, bvConfig, bvSdkUserAgent, productSentimentsFingerprintProvider);

            final String clientId = bvConfig.getClientId();
            final BVPixel bvPixel = bvsdk.getBvPixel();
            productSentimentsAnalyticsManager = new ProductSentimentsAnalyticsManager(bvPixel, clientId);

            return new BVProductSentimentsClient(this);
        }
    }

}
