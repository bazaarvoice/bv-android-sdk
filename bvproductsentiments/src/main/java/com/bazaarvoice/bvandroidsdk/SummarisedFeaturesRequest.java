package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

public class SummarisedFeaturesRequest extends ProductSentimentsRequest {
    private final String productId;
    private final String language;
    private final String embed;


    SummarisedFeaturesRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.language = builder.language;
        this.embed = builder.embed;
    }

    String getProductId() {
        return productId;
    }

    public String getLanguage() {
        return language;
    }

    public String getEmbed() {
        return embed;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ProductSentimentsRequest.Builder<Builder> {

        private String productId;
        private String language;
        private String embed;

        public Builder(@NonNull String productId) {
            super();
            this.productId = productId;

        }
        public Builder addLanguage(String language) {
            this.language = language;
            return this;
        }
        public Builder addEmbed(String embed) {
            this.embed = embed;
            return this;
        }

        public SummarisedFeaturesRequest build() {
            return new SummarisedFeaturesRequest(this);
        }
    }
}