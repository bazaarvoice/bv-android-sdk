package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

public class ReviewSummaryRequest extends ConversationsDisplayRequest {
    private final String productId;
    private final String formatType;
    private final String language;

    ReviewSummaryRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.formatType = builder.formatType;
        this.language = builder.language;
    }

    String getProductId() {
        return productId;
    }

    public String getFormatType() {
        return formatType;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ConversationsDisplayRequest.Builder<Builder> {

        private String productId;
        private String formatType;
        private String language;

        public Builder(@NonNull String productId) {
            super();
            this.productId = productId;

        }

        public Builder addFormatType(String formatType) {
            this.formatType = formatType;
            return this;
        }

        public Builder addLanguage(String language) {
            this.language = language;
            return this;
        }

        public ReviewSummaryRequest build() {
            return new ReviewSummaryRequest(this);
        }
    }
}