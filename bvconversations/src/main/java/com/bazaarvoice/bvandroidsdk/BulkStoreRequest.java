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

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Request used to obtain multiple {@link Store}s.
 */
public class BulkStoreRequest extends ConversationsDisplayRequest {
  private final BulkRatingOptions.StatsType statsType;
  private final List<String> storeIds;
  private final int limit;
  private final int offset;

  private BulkStoreRequest(Builder builder) {
    super(builder);
    statsType = builder.statsType;
    storeIds = builder.storeIds;
    limit = builder.limit;
    offset = builder.offset;
  }

  @Override
  BazaarException getError() {
    if (storeIds != null && (storeIds.size() < 1 || storeIds.size() > 20)) {
      return new BazaarException(String.format("Too many store Ids requested: %d. Must be between 1 and 20.", storeIds.size()));
    }
    return null;
  }

  BulkRatingOptions.StatsType getStatsType() {
    return statsType;
  }

  List<String> getStoreIds() {
    return storeIds;
  }

  int getLimit() {
    return limit;
  }

  int getOffset() {
    return offset;
  }

  public static final class Builder extends ConversationsDisplayRequest.Builder<Builder> {
    private final BulkRatingOptions.StatsType statsType;
    private final List<String> storeIds;
    private int limit;
    private int offset;

    public Builder(@NonNull List<String> storeIds) {
      super();
      this.storeIds = storeIds;
      addFilter(new Filter(Filter.Type.Id, EqualityOperator.EQ, storeIds));
      this.statsType = BulkRatingOptions.StatsType.Reviews;
    }

    public Builder(int limit, int offset) {
      super();
      this.storeIds = null;
      this.limit = limit;
      this.offset = offset;
      this.statsType = BulkRatingOptions.StatsType.Reviews;
    }

    public Builder addFilter(BulkRatingOptions.Filter filter, EqualityOperator equalityOperator, String value) {
      addFilter(new Filter(filter, equalityOperator, value));
      return this;
    }

    public BulkStoreRequest build() {
      return new BulkStoreRequest(this);
    }
  }
}