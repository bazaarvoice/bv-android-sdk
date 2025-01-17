package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

public class ReivewSummaryRequest extends ConversationsDisplayRequest {
    private final String productId;
    private final String formatType;

    ReivewSummaryRequest(Builder builder) {
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

        public ReivewSummaryRequest build() {
            return new ReivewSummaryRequest(this);
        }
    }
}