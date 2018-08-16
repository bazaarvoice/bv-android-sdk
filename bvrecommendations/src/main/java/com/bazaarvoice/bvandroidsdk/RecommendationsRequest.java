/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.annotation.SuppressLint;

/**
 *
 */
public class RecommendationsRequest {
    private static final String RELATIVE_PATH_TEMPLATE = "recommendations/magpie_idfa_%s?passKey=%s%s&limit=%d";
    private static final int MIN_LIMIT = 1, MAX_LIMIT = 100;

    private int limit;
    private String productId;
    private String categoryId;
    private PageType pageType;

    private RecommendationsRequest(Builder builder) {
        limit = builder.limit;
        productId = builder.productId;
        categoryId = builder.categoryId;
        pageType = builder.pageType;
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

    public PageType getPageType() { return pageType; }

    public static final class Builder {
        private int limit;
        private String productId;
        private String categoryId;
        private PageType pageType;

        public Builder(int limit) {
            this.limit = limit;
            if (limit < MIN_LIMIT || limit > MAX_LIMIT) {
                throw new IllegalArgumentException("Limit must be in the range [" + MIN_LIMIT + ", " + MAX_LIMIT + "]");
            }
        }

        public Builder productId(String val) {
            if (categoryId != null) {
                throw new IllegalArgumentException("Can only send productId or categoryId, not both");
            }
            productId = val;
            return this;
        }

        public Builder categoryId(String val) {
            if (productId != null) {
                throw new IllegalArgumentException("Can only send productId or categoryId, not both");
            }
            categoryId = val;
            return this;
        }

        public Builder pageType(PageType val) {
            pageType = val;
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

    @SuppressLint("DefaultLocale")
    private static String generateRelativePath(String adId, String recPasskey, String similarityParams, int limit, String clientId) {
        String relativePath = String.format(RELATIVE_PATH_TEMPLATE, adId, recPasskey, similarityParams, limit);
        if (clientId != null && clientId.length() != 0) {
            relativePath += "&client="+ clientId;
        }
        return relativePath;
    }


    public static String toUrlString(String baseUrlString, String adId, String apiKey, String clientId, RecommendationsRequest request) {

        String similarityParams = generateSimilarityParams(request.getProductId(), request.getCategoryId(), clientId);
        String relativePath = generateRelativePath(adId, apiKey, similarityParams, request.getLimit(), clientId);

        if(request.pageType != null) {
            relativePath += "&pageType=" + request.pageType.toString();
        }

        return baseUrlString + relativePath;

    }
}
