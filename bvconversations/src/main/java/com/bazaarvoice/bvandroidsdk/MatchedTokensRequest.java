package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

public class MatchedTokensRequest extends ConversationsDisplayRequest {
    private final String productId;
    private final String reviewText;

    MatchedTokensRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.reviewText = builder.reviewText;
    }

    String getProductId() {
        return productId;
    }

    String getReviewText() {
        return reviewText;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ConversationsDisplayRequest.Builder<MatchedTokensRequest.Builder> {
        private final String productId;
        private String reviewText;

        public Builder(@NonNull String productId) {
            super();
            this.productId = productId;
        }

        public Builder addReviewText(@NonNull String reviewText) {
            this.reviewText = reviewText;
            return this;
        }

        public MatchedTokensRequest build() {
            return new MatchedTokensRequest(this);
        }
    }
}
