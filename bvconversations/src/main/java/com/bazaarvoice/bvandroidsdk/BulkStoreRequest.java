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
 * Request used to obtain multiple {@link Store}s.
 */
public class BulkStoreRequest extends ConversationsDisplayRequest {

    private BulkStoreRequest(Builder builder) {
        super(builder);
    }

    @Override
    String getEndPoint() {
        return "products.json";
    }

    @Override
    BazaarException getError() {
        Builder builder = (Builder) super.getBuilder();

        if (builder.storeIds != null && (builder.storeIds.size() < 1 || builder.storeIds.size() > 20)) {
            return new BazaarException(String.format("Too many store Ids requested: %d. Must be between 1 and 20.", builder.storeIds.size()));
        }
        return null;
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {
        Builder builder = (BulkStoreRequest.Builder) getBuilder();

        if (builder.getLimit() != null){
            queryParams.put(kLIMIT, builder.getLimit().intValue());
            queryParams.put(kOFFSET, builder.getOffset().intValue());
        }

        queryParams.put(kSTATS, builder.statsType.getKey());
    }

    @Override
    String getAPIKey(){
        return BVSDK.getInstance().getApiKeys().getApiKeyConversationsStores();
    }

    public static final class Builder extends ConversationsDisplayRequest.Builder {

        private final BulkRatingOptions.StatsType statsType;
        private final List<String> storeIds;
        private Integer limit;
        private Integer offset;

        public Builder(@NonNull  List<String> storeIds) {
            this.storeIds = storeIds;
            this.limit = this.offset = null;
            getFilters().add(new Filter(Filter.Type.Id, EqualityOperator.EQ, storeIds));
            this.statsType = BulkRatingOptions.StatsType.Reviews;
        }

        public Builder(Integer limit, Integer offset) {
            this.storeIds = null;
            this.limit = new Integer(limit);
            this.offset = new Integer(offset);
            this.statsType = BulkRatingOptions.StatsType.Reviews;
        }

        public Builder addFilter(BulkRatingOptions.Filter filter, EqualityOperator equalityOperator, String value) {
            getFilters().add(new Filter(filter, equalityOperator, value));
            return this;
        }

        public BulkStoreRequest build() {
            return new BulkStoreRequest(this);
        }

        public Integer getLimit() {
            return limit;
        }

        public Integer getOffset() {
            return offset;
        }
    }
}