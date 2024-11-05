package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

public class QuotesSentimentRequest extends ProductSentimentsRequest {
    private final String productId;
    private final String language;
    private final String limit;

    QuotesSentimentRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.language = builder.language;
        this.limit = builder.limit;
    }

    String getProductId() {
        return productId;
    }

    public String getLanguage() {
        return language;
    }

    public String getLimit() {
        return limit;
    }


    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ProductSentimentsRequest.Builder<QuotesSentimentRequest.Builder> {

        private String productId;
        private String language;
        private String limit;

        public Builder(@NonNull String productId) {
            super();
            this.productId = productId;

        }
        public Builder addLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder addLimit(String limit) {
            this.limit = limit;
            return this;
        }

        public QuotesSentimentRequest build() {
            return new QuotesSentimentRequest(this);
        }
    }
}