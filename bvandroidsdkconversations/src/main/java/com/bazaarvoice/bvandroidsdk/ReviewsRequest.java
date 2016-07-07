/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Request used to obtain Reviews for a particular productId
 */
public class ReviewsRequest extends ConversationsRequest{


    public ReviewsRequest(Builder builder) {
        super(builder);
    }
    @Override
    String getEndPoint() {
        return "reviews.json";
    }

    @Override
    BazaarException getError() {
        Builder builder = (Builder) super.getBuilder();

        if (builder.limit < 1 || builder.limit > 20) {
            return new BazaarException(String.format("Invalid `limit` value: Parameter 'limit' has invalid value: %d - must be between 1 and 20.", builder.limit));
        }
        return null;
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {
        Builder builder = (Builder) super.getBuilder();

        queryParams.put(kLIMIT, "" + builder.limit);
        queryParams.put(kOFFSET, "" + builder.offset);
        queryParams.put(kINCLUDE, INCLUDE_ANSWERS);

        if (!builder.sorts.isEmpty()){
            queryParams.put(kSORT, StringUtils.componentsSeparatedBy(builder.sorts, ","));
        }

        if (builder.searchPhrase != null) {
            queryParams.put(kSEARCH, builder.searchPhrase);
        }
    }


    public static final class Builder extends ConversationsRequest.Builder{

        private final List<Sort> sorts;
        private final List<Filter> filters;
        private final int limit;
        private final int offset;
        private String searchPhrase;

        public Builder(@NonNull String productId, int limit, int offset) {
            this.sorts = new ArrayList<>();
            this.filters = new ArrayList<>();
            this.limit = limit;
            this.offset = offset;
            filters.add(new Filter(Filter.Type.ProductId, EqualityOperator.EQ, productId));

        }

        public Builder addSort(ReviewOptions.Sort sort, SortOrder sortOrder) {
            this.sorts.add(new Sort(sort, sortOrder));
            return this;
        }

        public Builder addFilter(ReviewOptions.Filter filter, EqualityOperator equalityOperator, String value) {
            this.filters.add(new Filter(filter, equalityOperator, value));
            return this;
        }

        public Builder includeSearchPhrase(String search) {
            this.searchPhrase = search;
            return this;
        }

        public ReviewsRequest build() {
            return new ReviewsRequest(this);
        }

        @Override
        List<Filter> getFilters() {
            return filters;
        }
    }
}