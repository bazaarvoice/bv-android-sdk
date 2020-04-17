package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ReviewHighlightsRequest extends ConversationsDisplayRequest {
    private final String productId;

    private ReviewHighlightsRequest(Builder builder) {
        super(builder);
        productId = builder.productId;
    }

    String getProductId() {
        return productId;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ConversationsDisplayRequest.Builder<Builder> {
        private String productId;

        public Builder(@NonNull String productId) {
            super();

            this.productId = productId;
        }
        public ReviewHighlightsRequest build() {
            return new ReviewHighlightsRequest(this);
        }
    }
}