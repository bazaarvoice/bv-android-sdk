package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

public class ExpressionsSentimentRequest extends ProductSentimentsRequest {
    private final String productId;
    private final String feature;
    private final String limit;

    ExpressionsSentimentRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.feature = builder.feature;
        this.limit = builder.limit;
    }

    String getProductId() {
        return productId;
    }

    public String getFeature() {
        return feature;
    }

    public String getLimit() {
        return limit;
    }


    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ProductSentimentsRequest.Builder<Builder> {

        private String productId;
        private String feature;
        private String limit;

        public Builder(@NonNull String productId, @NonNull String feature) {
            super();
            this.productId = productId;
            this.feature = feature;

        }

        public Builder addLimit(String limit) {
            this.limit = limit;
            return this;
        }

        public ExpressionsSentimentRequest build() {
            return new ExpressionsSentimentRequest(this);
        }
    }
}