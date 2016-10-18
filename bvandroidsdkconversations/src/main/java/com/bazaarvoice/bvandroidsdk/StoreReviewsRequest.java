/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Request used to obtain Store Reviews for a particular storeId
 */
public class StoreReviewsRequest extends ConversationsDisplayRequest {

    private static final String REVIEWS_ENDPOINT = "reviews.json";
    private final String storeId;

    private StoreReviewsRequest(Builder builder) {
        super(builder);
        this.storeId = builder.productId;
    }

    String getStoreId() {
        return storeId;
    }

    @Override
    String getAPIKey(){
        return BVSDK.getInstance().getApiKeys().getApiKeyConversationsStores();
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
        queryParams.put(kINCLUDE, "Products");

        if (!builder.sorts.isEmpty()){
            queryParams.put(kSORT, StringUtils.componentsSeparatedBy(builder.sorts, ","));
        }

        if (builder.searchPhrase != null) {
            queryParams.put(kSEARCH, builder.searchPhrase);
        }
    }


    public static final class Builder extends ReviewDisplayRequestBuilder<StoreReviewsRequest> {

        public Builder(@NonNull String storeId, int limit, int offset) {
            super(storeId, limit, offset);
        }

        public StoreReviewsRequest build() {
            return new StoreReviewsRequest(this);
        }


    }
}