/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

public class ReviewSubmissionRequest extends BaseReviewSubmissionRequest {

    ReviewSubmissionRequest(Builder builder) {
        super(builder);
    }

    @Override
    protected String getApiKey() {
        return BVSDK.getInstance().getApiKeys().getApiKeyConversations();
    }

    public static class Builder extends BaseReviewSubmissionRequest.Builder<Builder> {
        public Builder(Action action, String productId) {
            super(action, productId);
        }

        public ReviewSubmissionRequest build() {
            return new ReviewSubmissionRequest(this);
        }
    }
}