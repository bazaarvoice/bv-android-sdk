package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

public class ReviewTokensRequest extends ConversationsDisplayRequest {
    private final String productId;

    ReviewTokensRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
    }

    String getProductId() {
        return productId;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ConversationsDisplayRequest.Builder<ReviewTokensRequest.Builder> {
        private final String productId;

        public Builder(@NonNull String productId) {
            super();
            this.productId = productId;
        }

        public ReviewTokensRequest build() {
            return new ReviewTokensRequest(this);
        }
    }
}
