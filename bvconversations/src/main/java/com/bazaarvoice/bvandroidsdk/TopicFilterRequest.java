package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

public class TopicFilterRequest extends ConversationsDisplayRequest {
    private final String productId;
    private final String language;

    TopicFilterRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.language = builder.language;
    }

    String getProductId() {
        return productId;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ConversationsDisplayRequest.Builder<TopicFilterRequest.Builder> {

        private String productId;
        private String language;

        public Builder(@NonNull String productId) {
            super();
            this.productId = productId;

        }
        public Builder addLanguage(String language) {
            this.language = language;
            return this;
        }

        public TopicFilterRequest build() {
            return new TopicFilterRequest(this);
        }
    }
}
