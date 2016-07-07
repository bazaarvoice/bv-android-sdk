/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

/**
 * Request used to obtain statistics such as Ratings on multiple productIds
 */
public class BulkRatingsRequest extends ConversationsRequest{

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

    public static final class Builder extends ConversationsRequest.Builder {

        private final BulkRatingOptions.StatsType statsType;
        private final List<String> productIds;

        public Builder(@NonNull  List<String> productIds, BulkRatingOptions.StatsType statsType) {
            this.productIds = productIds;
            filters.add(new Filter(Filter.Type.ProductId, EqualityOperator.EQ, productIds));
            this.statsType = statsType;
        }

        public Builder addFilter(BulkRatingOptions.Filter filter, EqualityOperator equalityOperator, String value) {
            this.filters.add(new Filter(filter, equalityOperator, value));
            return this;
        }

        public BulkRatingsRequest build() {
            return new BulkRatingsRequest(this);
        }

        @Override
        List<Filter> getFilters() {
            return filters;
        }
    }
}