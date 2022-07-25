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

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/**
 * Request to get {@link StoreReview}s for a {@link Store}
 */
public class StoreReviewsRequest extends ConversationsDisplayRequest {
  private final String storeId;
  private final int limit;
  private final int offset;
  private final List<Sort> sorts;
  private final List<RelevancySort> relevancySorts;
  private final String searchPhrase;
  private final List<ReviewIncludeType> reviewIncludeTypes;

  private StoreReviewsRequest(@NonNull Builder builder) {
    super(builder);
    storeId = builder.storeId;
    limit = builder.limit;
    offset = builder.offset;
    sorts = builder.sorts;
    relevancySorts = builder.relevancySorts;
    searchPhrase = builder.searchPhrase;
    this.reviewIncludeTypes = builder.reviewIncludeTypes;
  }

  String getStoreId() {
    return storeId;
  }

  int getLimit() {
    return limit;
  }

  int getOffset() {
    return offset;
  }

  List<Sort> getSorts() {
    return sorts;
  }

  List<RelevancySort> getRelevancySorts() {
    return relevancySorts;
  }

  String getSearchPhrase() {
    return searchPhrase;
  }

  List<ReviewIncludeType> getReviewIncludeTypes() {
    return reviewIncludeTypes;
  }

  @Override
  BazaarException getError() {
    if (limit < 1 || limit > 100) {
      return new BazaarException(String.format("Invalid `limit` value: Parameter 'limit' has invalid value: %d - must be between 1 and 100.", limit));
    }
    return null;
  }

  public static final class Builder extends ReviewDisplayRequestBuilder<Builder, StoreReviewsRequest> {
    private final String storeId;
    private final int limit;
    private final int offset;

    public Builder(@NonNull String storeId, @IntRange(from = 0) int limit, @IntRange(from = 0) int offset) {
      super(storeId, limit, offset);
      this.storeId = storeId;
      this.limit = limit;
      this.offset = offset;
    }

    public StoreReviewsRequest build() {
      return new StoreReviewsRequest(this);
    }
  }
}