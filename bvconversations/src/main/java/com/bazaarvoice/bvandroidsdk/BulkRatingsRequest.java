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

import java.util.List;
import java.util.Map;

/**
 * Request used to obtain statistics such as Ratings on multiple productIds
 */
public class BulkRatingsRequest extends ConversationsDisplayRequest {

    private BulkRatingsRequest(Builder builder) {
        super(builder);
    }

    @Override
    String getEndPoint() {
        return "statistics.json";
    }

    @Override
    BazaarException getError() {
        Builder builder = (Builder) super.getBuilder();

        if (builder.productIds.size() < 1 || builder.productIds.size() > 50) {
            return new BazaarException(String.format("Too many productIds requested: %d. Must be between 1 and 50.", builder.productIds.size()));
        }
        return null;
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {
        Builder builder = (BulkRatingsRequest.Builder) getBuilder();
        queryParams.put(kSTATS, builder.statsType.getKey());
    }

    public static final class Builder extends ConversationsDisplayRequest.Builder {

        private final BulkRatingOptions.StatsType statsType;
        private final List<String> productIds;

        public Builder(@NonNull List<String> productIds, BulkRatingOptions.StatsType statsType) {
            this.productIds = productIds;
            getFilters().add(new Filter(Filter.Type.ProductId, EqualityOperator.EQ, productIds));
            this.statsType = statsType;
        }

        public Builder addFilter(BulkRatingOptions.Filter filter, EqualityOperator equalityOperator, String value) {
            getFilters().add(new Filter(filter, equalityOperator, value));
            return this;
        }

        public BulkRatingsRequest build() {
            return new BulkRatingsRequest(this);
        }
    }
}