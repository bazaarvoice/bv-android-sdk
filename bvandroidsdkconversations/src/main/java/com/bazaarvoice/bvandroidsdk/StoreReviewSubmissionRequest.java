/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

public class StoreReviewSubmissionRequest extends BaseReviewSubmissionRequest {

    StoreReviewSubmissionRequest(Builder builder) {
        super(builder);
    }

    @Override
    protected String getApiKey() {
        return BVSDK.getInstance().getApiKeys().getApiKeyConversationsStores();
    }

    public static class Builder extends BaseReviewSubmissionRequest.Builder<Builder> {
        public Builder(Action action, String productId) {
            super(action, productId);
        }

        public StoreReviewSubmissionRequest build() {
            return new StoreReviewSubmissionRequest(this);
        }
    }
}