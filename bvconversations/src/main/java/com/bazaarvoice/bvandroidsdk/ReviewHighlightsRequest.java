/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

/**
 * Request to get {@link ReviewHighlights}s
 */

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