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

import java.util.ArrayList;
import java.util.List;

/**
 * Common options for a Conversations display request
 */
abstract class ConversationsDisplayRequest extends ConversationsRequest {
  private final List<Filter> filters;
  private final List<QueryPair> extraParams;

  ConversationsDisplayRequest(Builder builder) {
    filters = builder.filters;
    extraParams = builder.extraParams;
  }

  List<Filter> getFilters() {
    return filters;
  }

  List<QueryPair> getExtraParams() {
    return extraParams;
  }

  static class QueryPair {
    private final String key, value;

    public QueryPair(String key, String value) {
      this.key = key;
      this.value = value;
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }
  }

  static abstract class Builder<BuilderType> {
    private final List<Filter> filters;
    private final List<QueryPair> extraParams;

    public Builder() {
      filters = new ArrayList<>();
      extraParams = new ArrayList<>();
    }

    protected void addFilter(Filter filter) {
      filters.add(filter);
    }

    /**
     * This method is not related to the Review Submission specific
     * {@link BaseReviewBuilder#addAdditionalField(String, String)}.
     * May want to deprecate this function, and come up with a common
     * name for display and submission that is not 'addAdditionalField'.
     * <br/><br/>
     * This method adds extra user provided query parameters to a
     * display request, and will be urlencoded.
     *
     * @param key Custom non-encoded url query param key
     * @param value Custom non-encoded url query param value
     * @return The Builder
     */
    public BuilderType addAdditionalField(String key, String value) {
      extraParams.add(new QueryPair(key, value));
      return (BuilderType) this;
    }
  }
}