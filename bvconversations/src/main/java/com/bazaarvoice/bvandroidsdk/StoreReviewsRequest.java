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

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Request to get {@link StoreReview}s for a {@link Store}
 */
public class StoreReviewsRequest extends ConversationsDisplayRequest {

    private static final String REVIEWS_ENDPOINT = "reviews.json";
    private final String storeId;

    private StoreReviewsRequest(Builder builder) {
        super(builder);
        this.storeId = builder.productId;
    }

    String getStoreId() {
        return storeId;
    }

    @Override
    String getAPIKey(){
        return BVSDK.getInstance().getBvUserProvidedData().getBvApiKeys().getApiKeyConversationsStores();
    }

    @Override
    String getEndPoint() {
        return REVIEWS_ENDPOINT;
    }

    @Override
    BazaarException getError() {
        Builder builder = (Builder) super.getBuilder();

        if (builder.limit < 1 || builder.limit > 100) {
            return new BazaarException(String.format("Invalid `limit` value: Parameter 'limit' has invalid value: %d - must be between 1 and 100.", builder.limit));
        }
        return null;
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {
        Builder builder = (Builder) super.getBuilder();

        queryParams.put(kLIMIT, "" + builder.limit);
        queryParams.put(kOFFSET, "" + builder.offset);
        queryParams.put(kINCLUDE, "Products");

        if (!builder.sorts.isEmpty()){
            queryParams.put(kSORT, StringUtils.componentsSeparatedBy(builder.sorts, ","));
        }

        if (builder.searchPhrase != null) {
            queryParams.put(kSEARCH, builder.searchPhrase);
        }
    }


    public static final class Builder extends ReviewDisplayRequestBuilder<StoreReviewsRequest> {

        public Builder(@NonNull String storeId, int limit, int offset) {
            super(storeId, limit, offset);
        }

        public StoreReviewsRequest build() {
            return new StoreReviewsRequest(this);
        }


    }
}