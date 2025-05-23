package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

public class ReviewSummaryRequest extends ConversationsDisplayRequest {
    private final String productId;
    private final String formatType;

    ReviewSummaryRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.formatType = builder.formatType;
    }

    String getProductId() {
        return productId;
    }

    public String getFormatType() {
        return formatType;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ConversationsDisplayRequest.Builder<Builder> {

        private String productId;
        private String formatType;

        public Builder(@NonNull String productId) {
            super();
            this.productId = productId;

        }
        public Builder addFormatType(String formatType) {
            this.formatType = formatType;
            return this;
        }

        public ReviewSummaryRequest build() {
            return new ReviewSummaryRequest(this);
        }
    }
}