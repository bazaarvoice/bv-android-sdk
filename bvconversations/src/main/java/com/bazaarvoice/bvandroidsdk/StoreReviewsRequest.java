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

import okhttp3.HttpUrl;

/**
 * Request to get {@link StoreReview}s for a {@link Store}
 */
public class StoreReviewsRequest extends ConversationsDisplayRequest {

  private static final String REVIEWS_ENDPOINT = "data/reviews.json";
  private final String storeId;
  private final int limit;
  private final int offset;
  private final List<Sort> sorts;
  private final String searchPhrase;

  private StoreReviewsRequest(Builder builder) {
    super(builder);
    storeId = builder.storeId;
    limit = builder.limit;
    offset = builder.offset;
    sorts = builder.sorts;
    searchPhrase = builder.searchPhrase;
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

  public List<Sort> getSorts() {
    return sorts;
  }

  public String getSearchPhrase() {
    return searchPhrase;
  }

  @Override
  String getEndPoint() {
    return REVIEWS_ENDPOINT;
  }

  @Override
  BazaarException getError() {
    if (limit < 1 || limit > 100) {
      return new BazaarException(String.format("Invalid `limit` value: Parameter 'limit' has invalid value: %d - must be between 1 and 100.", limit));
    }
    return null;
  }

  @Override
  protected String getAPIKey(){
    return BVSDK.getInstance().getBvUserProvidedData().getBvApiKeys().getApiKeyConversationsStores();
  }

  @Override
  HttpUrl toHttpUrl() {
    HttpUrl.Builder httpUrlBuilder = super.toHttpUrl().newBuilder();

    httpUrlBuilder.addQueryParameter(kLIMIT, String.valueOf(limit));
    httpUrlBuilder.addQueryParameter(kOFFSET, String.valueOf(offset));
    httpUrlBuilder.addQueryParameter(kINCLUDE, "Products");

    if (!sorts.isEmpty()){
      httpUrlBuilder.addQueryParameter(kSORT, StringUtils.componentsSeparatedBy(sorts, ","));
    }

    if (searchPhrase != null) {
      httpUrlBuilder.addQueryParameter(kSEARCH, searchPhrase);
    }

    return httpUrlBuilder.build();
  }

  public static final class Builder extends ReviewDisplayRequestBuilder<Builder, StoreReviewsRequest> {
    private final String storeId;
    private final int limit;
    private final int offset;

    public Builder(@NonNull String storeId, int limit, int offset) {
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