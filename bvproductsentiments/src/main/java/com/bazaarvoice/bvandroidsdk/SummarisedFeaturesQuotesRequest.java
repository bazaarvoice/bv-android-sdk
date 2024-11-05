package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

public class SummarisedFeaturesQuotesRequest extends ProductSentimentsRequest {
    private final String productId;
    private final String featureId;
    private final String language;
    private final String limit;

    SummarisedFeaturesQuotesRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.featureId = builder.featureId;
        this.language = builder.language;
        this.limit = builder.limit;
    }

    String getProductId() {
        return productId;
    }

    public String getFeatureId() {
        return featureId;
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

    public static final class Builder extends ProductSentimentsRequest.Builder<Builder> {

        private String productId;
        private String featureId;
        private String language;
        private String limit;

        public Builder(@NonNull String productId,@NonNull String featureId) {
            super();
            this.productId = productId;
            this.featureId = featureId;

        }
        public Builder addLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder addLimit(String limit) {
            this.limit = limit;
            return this;
        }

        public SummarisedFeaturesQuotesRequest build() {
            return new SummarisedFeaturesQuotesRequest(this);
        }
    }
}