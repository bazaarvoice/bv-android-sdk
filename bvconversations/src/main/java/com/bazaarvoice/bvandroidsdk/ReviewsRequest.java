/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Request used to obtain Reviews for a particular productId
 */
public class ReviewsRequest extends ConversationsDisplayRequest {

    private static final String REVIEWS_ENDPOINT = "reviews.json";
    private final String productId;

    private ReviewsRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
    }

    String getProductId() {
        return productId;
    }

    @Override
    String getEndPoint() {
        return REVIEWS_ENDPOINT;
    }

    @Override
    BazaarException getError() {
        Builder builder = (Builder) super.getBuilder();

        if (builder.limit < 1 || builder.limit > 100) {
            return new BazaarException(String.format("Invalid `limit` value: Parameter 'limit' has invalid value: %d - must be between 1 and 100.", builder.limit));
        }
        return null;
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {
        Builder builder = (Builder) super.getBuilder();

        queryParams.put(kLIMIT, "" + builder.limit);
        queryParams.put(kOFFSET, "" + builder.offset);
        queryParams.put(kINCLUDE, INCLUDE_ANSWERS);

        if (!builder.sorts.isEmpty()){
            queryParams.put(kSORT, StringUtils.componentsSeparatedBy(builder.sorts, ","));
        }

        if (builder.searchPhrase != null) {
            queryParams.put(kSEARCH, builder.searchPhrase);
        }
    }


    public static final class Builder extends ReviewDisplayRequestBuilder<ReviewsRequest> {

        public Builder(@NonNull String productId, int limit, int offset) {
            super(productId, limit, offset);
        }

        @Override
        public ReviewsRequest build() {
            return new ReviewsRequest(this);
        }

    }
}