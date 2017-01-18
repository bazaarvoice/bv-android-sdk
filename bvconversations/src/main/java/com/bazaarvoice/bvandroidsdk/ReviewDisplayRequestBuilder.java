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

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for parameter build for {@link Review}s
 */
public abstract class ReviewDisplayRequestBuilder<RequestType> extends ConversationsDisplayRequest.Builder {

    protected final String productId;
    protected final List<Sort> sorts;
    protected final List<Filter> filters;
    protected final int limit;
    protected final int offset;
    protected String searchPhrase;

    public ReviewDisplayRequestBuilder(@NonNull String productId, int limit, int offset) {
        this.sorts = new ArrayList<>();
        this.filters = new ArrayList<>();
        this.limit = limit;
        this.offset = offset;
        filters.add(new Filter(Filter.Type.ProductId, EqualityOperator.EQ, productId));
        this.productId = productId;
    }

    public ReviewDisplayRequestBuilder<RequestType> addSort(ReviewOptions.Sort sort, SortOrder sortOrder) {
        this.sorts.add(new Sort(sort, sortOrder));
        return this;
    }

    public ReviewDisplayRequestBuilder<RequestType> addFilter(ReviewOptions.Filter filter, EqualityOperator equalityOperator, String value) {
        this.filters.add(new Filter(filter, equalityOperator, value));
        return this;
    }

    public ReviewDisplayRequestBuilder<RequestType> includeSearchPhrase(String search) {
        this.searchPhrase = search;
        return this;
    }

    public abstract RequestType build();

    @Override
    List<Filter> getFilters() {
        return filters;
    }



}
