package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Converts user created Request Objects into OkHttp Request Objects.
 * Each user created Request Object will only have the information that
 * the user entered. This Factory will manage adding anything else
 * required including Headers and Query Parameters, as well as taking
 * user inputted data, and formatting it correctly such as POST body
 * media content.
 */
class ProductSentimentsBasicRequestFactory implements ProductSentimentRequestFactory {
    // region Keys
    // region Generic Request Keys
    private static final String kAPI_VERSION = "apiversion";
    private static final String kPASS_KEY = "passkey";
    private static final String kAPP_ID = "_appId";
    private static final String kAPP_VERSION = "_appVersion";
    private static final String kBUILD_NUM = "_buildNumber";
    private static final String kSDK_VERSION = "_bvAndroidSdkVersion";
    private static final String API_VERSION = "5.4";
    private static final String KEY_USER_AGENT = "User-Agent";
    private static final String KEY_AUTHORIZATION = "Authorization";
    // endregion

    // region Generic Display Request Keys
    private static final String kLIMIT = "Limit";
    private static  final String kFEATURES = "feature";
    private static final String kProductId = "productId";
    private static final String kLanguage = "language";
    // endregion


    // region Product Sentiment Request Keys
    private static final String SUMMARISED_FEATURES_ENDPOINT = "sentiment/v1/summarised-features";
    private static final String SUMMARISED_FEATURES_QUOTES_ENDPOINT = "sentiment/v1/summarised-features/";
    private static final String FEATURES_SENTIMENT_ENDPOINT = "sentiment/v1/features";
    private static final String QUOTES_SENTIMENT_ENDPOINT = "sentiment/v1/quotes";
    private static final String EXPRESSIONS_SENTIMENT_ENDPOINT = "sentiment/v1/expressions";
    // endregion

    // region Generic Submit Request Keys
    private static final String kEMBED = "embed";
    private static final String kQUOTES = "quotes";
    // endregion


    // region Instance Fields
    private final BVMobileInfo bvMobileInfo;
    private final String bvRootApiUrl;
    private final String bvReviewHighlightsUrl;
    private final String psApiKey;
    private final String bvSdkUserAgent;
    private final ProductSentimentsFingerprintProvider productSentimentsFingerprintProvider;
    // endregion

    // region Constructor
    ProductSentimentsBasicRequestFactory(BVMobileInfo bvMobileInfo, BVRootApiUrls bvRootApiUrls, BVConfig bvConfig, String bvSdkUserAgent, ProductSentimentsFingerprintProvider productSentimentsFingerprintProvider) {
        this.bvMobileInfo = bvMobileInfo;
        this.bvRootApiUrl = bvRootApiUrls.getBazaarvoiceApiRootUrl();
        this.bvReviewHighlightsUrl = bvRootApiUrls.getBazaarvoiceReviewHighlightApiUrl();
        this.psApiKey = bvConfig.getApiKeyProductSentiments();
        this.bvSdkUserAgent = bvSdkUserAgent;
        this.productSentimentsFingerprintProvider = productSentimentsFingerprintProvider;
    }
    // endregion

    // region API
    public <RequestType extends ProductSentimentsRequest> Request create(RequestType request) {
        if (request instanceof SummarisedFeaturesRequest) {
            return createFromSummarisedFeaturesRequest((SummarisedFeaturesRequest) request);
        } else if (request instanceof SummarisedFeaturesQuotesRequest) {
            return createFromSummarisedFeaturesQuotesRequest((SummarisedFeaturesQuotesRequest) request);
        }else if (request instanceof FeaturesSentimentRequest) {
                return createFromFeaturesSentimentRequest((FeaturesSentimentRequest) request);
        }else if (request instanceof QuotesSentimentRequest) {
            return createFromQuotesSentimentRequest((QuotesSentimentRequest) request);
        }else if (request instanceof ExpressionsSentimentRequest) {
            return createFromExpressionsSentimentRequest((ExpressionsSentimentRequest) request);
        }
        throw new IllegalStateException("Unknown request type: " + request.getClass().getCanonicalName());
    }




    private Request createFromSummarisedFeaturesRequest(SummarisedFeaturesRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(SUMMARISED_FEATURES_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, bvMobileInfo);
        httpUrlBuilder.addQueryParameter(kProductId, String.valueOf(request.getProductId()));
        if(request.getEmbed()!=null){
            httpUrlBuilder.addQueryParameter(kEMBED,String.valueOf(request.getEmbed()));
        }
        if(request.getLanguage()!=null){
            httpUrlBuilder.addQueryParameter(kLanguage, String.valueOf(request.getLanguage()));
        }
        HttpUrl httpUrl = httpUrlBuilder.build();
        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent,psApiKey);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromSummarisedFeaturesQuotesRequest(SummarisedFeaturesQuotesRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(SUMMARISED_FEATURES_QUOTES_ENDPOINT)
                .addPathSegment(String.valueOf(request.getFeatureId())) // dynamically adding feature ID
                .addPathSegment(kQUOTES);

        addCommonQueryParams(httpUrlBuilder, bvMobileInfo);
        httpUrlBuilder.addQueryParameter(kProductId, String.valueOf(request.getProductId()));
        if(request.getLimit()!=null) {
            httpUrlBuilder.addQueryParameter(kLIMIT, String.valueOf(request.getLimit()));
        }
        if(request.getLanguage()!=null){
            httpUrlBuilder.addQueryParameter(kLanguage, String.valueOf(request.getLanguage()));
        }
        HttpUrl httpUrl = httpUrlBuilder.build();
        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent, psApiKey);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromFeaturesSentimentRequest(FeaturesSentimentRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(FEATURES_SENTIMENT_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, bvMobileInfo);
        httpUrlBuilder.addQueryParameter(kProductId, String.valueOf(request.getProductId()));
        if(request.getLimit()!=null) {
            httpUrlBuilder.addQueryParameter(kLIMIT, String.valueOf(request.getLimit()));
        }
        if(request.getLanguage()!=null){
            httpUrlBuilder.addQueryParameter(kLanguage, String.valueOf(request.getLanguage()));
        }
        HttpUrl httpUrl = httpUrlBuilder.build();
        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent, psApiKey);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromQuotesSentimentRequest(QuotesSentimentRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(QUOTES_SENTIMENT_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, bvMobileInfo);
        httpUrlBuilder.addQueryParameter(kProductId, String.valueOf(request.getProductId()));
        if(request.getLimit()!=null) {
            httpUrlBuilder.addQueryParameter(kLIMIT, String.valueOf(request.getLimit()));
        }
        if(request.getLanguage()!=null){
            httpUrlBuilder.addQueryParameter(kLanguage, String.valueOf(request.getLanguage()));
        }
        HttpUrl httpUrl = httpUrlBuilder.build();
        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent, psApiKey);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromExpressionsSentimentRequest(ExpressionsSentimentRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(EXPRESSIONS_SENTIMENT_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, bvMobileInfo);
        httpUrlBuilder.addQueryParameter(kProductId, String.valueOf(request.getProductId()));
        if(request.getLimit()!=null) {
            httpUrlBuilder.addQueryParameter(kLIMIT, String.valueOf(request.getLimit()));
        }
        httpUrlBuilder.addQueryParameter(kFEATURES, String.valueOf(request.getFeature()));
        HttpUrl httpUrl = httpUrlBuilder.build();
        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent, psApiKey);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    // region Static Helpers
    private static void addCommonQueryParams(HttpUrl.Builder httpUrlBuilder, BVMobileInfo bvMobileInfo) {
        httpUrlBuilder.addQueryParameter(kAPI_VERSION, API_VERSION)
                .addQueryParameter(kAPP_ID, bvMobileInfo.getMobileAppIdentifier())
                .addQueryParameter(kAPP_VERSION, bvMobileInfo.getMobileAppVersion())
                .addQueryParameter(kBUILD_NUM, bvMobileInfo.getMobileAppCode())
                .addQueryParameter(kSDK_VERSION, bvMobileInfo.getBvSdkVersion());
    }

    private static void addCommonHeaders(Headers.Builder headersBuilder, String bvSdkUserAgent, String psApiKey) {
        headersBuilder.add(KEY_USER_AGENT, bvSdkUserAgent);
        if (psApiKey != null && !psApiKey.toString().isEmpty()) {
            headersBuilder.add(KEY_AUTHORIZATION, psApiKey);
        }
    }

    private static void jsonPutSafe(JsonObject json, @NonNull String key, @Nullable Object value){
        if (value != null && !value.toString().isEmpty()) {
            String valueStr = String.valueOf(value);
            json.addProperty(key, valueStr);
        }
    }


    private static void formPutSafe(FormBody.Builder formBodyBuilder, @NonNull String key, @Nullable Object value) {
        if (value != null && !value.toString().isEmpty()) {
            String valueStr = String.valueOf(value);
            formBodyBuilder.add(key, valueStr);
        }
    }


}
