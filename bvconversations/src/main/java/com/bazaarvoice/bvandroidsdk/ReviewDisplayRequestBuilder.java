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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Common options for building a Review Display Request.
 */
public abstract class ReviewDisplayRequestBuilder<BuilderType, RequestType> extends ConversationsDisplayRequest.Builder<BuilderType> {
  protected final String productId;
  protected final List<Sort> sorts;
  protected final int limit;
  protected final int offset;
  protected final ArrayList<PDPContentType> statistics;
  protected String searchPhrase;
  protected Boolean incentivizedStats;
  protected Boolean secondaryratingstats;
  protected Boolean tagStats;
  protected List<ReviewIncludeType> reviewIncludeTypes;
  protected  String feature;
  protected final Map<String, String> contextDataValues;
  protected final List<BVSecondaryRatingFilter> secondaryRatingFilters;
  protected final Map<String, String> tagFilters;
  protected final Map<String, String> additionalFields;

  public ReviewDisplayRequestBuilder(@NonNull String productId, int limit, int offset) {
    super();
    this.sorts = new ArrayList<>();
    this.reviewIncludeTypes = new ArrayList<>();
    this.statistics = new ArrayList<>();
    this.limit = limit;
    this.offset = offset;
    addFilter(new Filter(Filter.Type.ProductId, EqualityOperator.EQ, productId));
    this.productId = productId;
    this.incentivizedStats = false;
    this.secondaryratingstats = false;
    this.tagStats = false;
    this.contextDataValues = new HashMap<>();
    this.secondaryRatingFilters = new ArrayList<>();
    this.tagFilters = new HashMap<>();
    this.additionalFields = new HashMap<>();
  }

  public ReviewDisplayRequestBuilder(ReviewOptions.PrimaryFilter filterBy, String id, int limit, int offset) {
    super();
    this.sorts = new ArrayList<>();
    this.reviewIncludeTypes = new ArrayList<>();
    this.statistics = new ArrayList<>();
    this.limit = limit;
    this.offset = offset;
    this.productId = null;
    Filter filter = new Filter(filterBy, EqualityOperator.EQ, id);
    addFilter(filter);
    this.incentivizedStats = false;
    this.tagStats = false;
    this.feature = new String();
    this.contextDataValues = new HashMap<>();
    this.secondaryRatingFilters =  new ArrayList<>();
    this.tagFilters = new HashMap<>();
    this.additionalFields = new HashMap<>();
  }

  public BuilderType addPDPContentType(PDPContentType pdpContentType) {
    this.statistics.add(pdpContentType);
    return (BuilderType) this;
  }

  public BuilderType addIncentivizedStats(Boolean incentivizedstats) {
    this.incentivizedStats = incentivizedstats;
    return (BuilderType) this;
  }

  public BuilderType addSecondaryRatingStats(Boolean secondaryratingstats) {
    this.secondaryratingstats = secondaryratingstats;
    return (BuilderType) this;
  }
  public BuilderType addTagStats(Boolean tagStats) {
    this.tagStats = tagStats;
    return (BuilderType) this;
  }

  public BuilderType addPDPContentType(PDPContentType... pdpContentTypes) {
    this.statistics.addAll(Arrays.asList(pdpContentTypes));
    return (BuilderType) this;
  }

  public BuilderType addIncludeContent(ReviewIncludeType reviewIncludeType) {
    this.reviewIncludeTypes.add(reviewIncludeType);
    return (BuilderType) this;
  }

  public BuilderType addIncludeContent(ReviewIncludeType... reviewIncludeTypes) {
    this.reviewIncludeTypes.addAll(Arrays.asList(reviewIncludeTypes));
    return (BuilderType) this;
  }

  public BuilderType addSort(ReviewOptions.Sort sort, SortOrder sortOrder) {
    this.sorts.add(new Sort(sort, sortOrder));
    return (BuilderType) this;
  }

  public BuilderType addFilter(ReviewOptions.Filter filter, EqualityOperator equalityOperator, String value) {
    addFilter(new Filter(filter, equalityOperator, value));
    return (BuilderType) this;
  }

  public BuilderType addFilter(ReviewOptions.Filter filter, EqualityOperator equalityOperator, List<String> values) {
    addFilter(new Filter(filter, equalityOperator, values));
    return (BuilderType) this;
  }

  public BuilderType includeSearchPhrase(String search) {
    this.searchPhrase = search;
    return (BuilderType) this;
  }

  public BuilderType addFeature(String feature) {
    this.feature = feature;
    return (BuilderType) this;
  }

  public BuilderType addFilterContextDataValue(String id, String value) {
    contextDataValues.put(id, value);
    return (BuilderType) this;
  }

  public BuilderType addAdditionalFields(String id, String value) {
    additionalFields.put(id, value);
    return (BuilderType) this;
  }

  public BuilderType addSecondaryRatingFilters(String type,  EqualityOperator equalityOperator, String value) {
    secondaryRatingFilters.add(new BVSecondaryRatingFilter(type, equalityOperator, value));
    return (BuilderType) this;
  }

  public BuilderType addFilterTag(String id, String value) {
    tagFilters.put(id, value);
    return (BuilderType) this;
  }

  public abstract RequestType build();

}