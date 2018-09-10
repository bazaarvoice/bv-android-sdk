/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import okhttp3.HttpUrl;

/**
 * Bazaarvoice Recommendations Request for getting custom product recommendations
 */
public class RecommendationsRequest {
    private static final String RECOMMENDATIONS_PATH = "recommendations";
    private static final int MIN_LIMIT = 1, MAX_LIMIT = 100;

    private int limit;
    private String productId;
    private String categoryId;
    private PageType pageType;
    private List<String> interests;
    private String requiredCategory;
    private double minAvgRating;


    private RecommendationsRequest(Builder builder) {
        limit = builder.limit;
        productId = builder.productId;
        categoryId = builder.categoryId;
        pageType = builder.pageType;
        interests = builder.interest;
        requiredCategory = builder.requiredCategory;
        minAvgRating = builder.minAvgRating;
    }

    public int getLimit() {
        return limit;
    }

    public String getProductId() {
        return productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public PageType getPageType() {
        return pageType;
    }

    public static final class Builder {
        private int limit;
        private String productId;
        private String categoryId;
        private PageType pageType;
        private List<String> interest;
        private String requiredCategory;
        private double minAvgRating;

        /**
         * @param limit Number of recommendations to return.
         */
        public Builder(@IntRange(from = MIN_LIMIT, to = MAX_LIMIT) int limit) {
            this.limit = limit;
            if (limit < MIN_LIMIT || limit > MAX_LIMIT) {
                throw new IllegalArgumentException("Limit must be in the range [" + MIN_LIMIT + ", " + MAX_LIMIT + "]");
            }
        }

        /**
         * Will make recommendations based on the product.
         */
        public Builder productId(@NonNull String val) {
            if (categoryId != null) {
                throw new IllegalArgumentException("Can only send productId or categoryId, not both");
            }
            productId = val;
            return this;
        }

        /**
         * Product recommendations from the set category will be preferred.
         */
        public Builder categoryId(@NonNull String val) {
            if (productId != null) {
                throw new IllegalArgumentException("Can only send productId or categoryId, not both");
            }
            categoryId = val;
            return this;
        }

        /**
         * The type of page the recommendations will be displayed on.
         */
        public Builder pageType(@NonNull PageType val) {
            pageType = val;
            return this;
        }

        /**
         * Filters recommendations to only those that match the interest filter.
         */
        public Builder interests(@NonNull List<String> val) {
            interest = val;
            return this;
        }

        /**
         * Will constrain recommendations to the category
         */
        public Builder requiredCategory(@NonNull String val) {
            requiredCategory = val;
            return this;
        }

        /**
         * Only return recommendations that are >= to supplied rating.
         */
        public Builder minAvgRating(@FloatRange(from = 0.0, to = 5.0) double val) {
            minAvgRating = val;
            return this;
        }

        public RecommendationsRequest build() {
            return new RecommendationsRequest(this);
        }
    }

    private static String generateSimilarityParams(String productId, String categoryId, String clientId) {
        String similarityParams = "";
        //mutually exclusive productId and categoryId. productId before categoryId
        if (productId != null) {
            similarityParams = "&product=" + clientId + "/" + productId;
        } else if (categoryId != null) {
            similarityParams = "&category=" + clientId + "/" + categoryId;
        }

        return similarityParams;
    }

    static @Nullable
    String toUrlString(@NonNull BVSDK bvsdk, String adId, RecommendationsRequest request) {

        String baseUrl = bvsdk.getBvWorkerData().getRootApiUrls().getShopperMarketingApiRootUrl();
        BVUserProvidedData bvUserProvidedData = bvsdk.getBvUserProvidedData();
        String apiKey = bvUserProvidedData.getBvConfig().getApiKeyShopperAdvertising();
        String clientId = bvUserProvidedData.getBvConfig().getClientId();

        String similarityParams = generateSimilarityParams(request.getProductId(), request.getCategoryId(), clientId);
        String interests = buildListsQueryParam(request.interests);

        HttpUrl url = HttpUrl.parse(baseUrl);
        if (url != null) {
            HttpUrl.Builder builder = url.newBuilder();
            builder.addPathSegment(RECOMMENDATIONS_PATH)
                    .addPathSegment("magpie_idfa_" + adId)
                    .addEncodedQueryParameter("passKey", apiKey)
                    .addEncodedQueryParameter("client", clientId)
                    .addEncodedQueryParameter("min_avg_rating", String.valueOf(request.minAvgRating))
                    .addEncodedQueryParameter("limit", String.valueOf(request.limit));
            if (request.pageType != null) {
                builder.addEncodedQueryParameter("pageType", request.pageType.toString());
            }
            if (request.requiredCategory != null) {
                builder.addEncodedQueryParameter("required_category", request.requiredCategory);
            }

            if (request.interests != null) {
                builder.addEncodedQueryParameter("interests", interests);
            }

            return builder.build().toString() + similarityParams;
        }
        return null;
    }

    private static @Nullable String buildListsQueryParam(List<String> items) {
        if (items != null && items.size() > 0) {
            return StringUtils.componentsSeparatedBy(items, ",");
        }

        return null;
    }

}