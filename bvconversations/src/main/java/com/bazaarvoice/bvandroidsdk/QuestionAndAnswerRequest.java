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
 * Request to get {@link Question}s and {@link Answer}s
 * for a particular {@link Product}
 */
public class QuestionAndAnswerRequest extends ConversationsDisplayRequest {
  private final List<Sort> questionSorts, answerSorts;
  private final int limit;
  private final int offset;
  private final String searchPhrase;
  private final String productId;

  private QuestionAndAnswerRequest(Builder builder) {
    super(builder);
    questionSorts = builder.questionSorts;
    answerSorts = builder.answerSorts;
    limit = builder.limit;
    offset = builder.offset;
    searchPhrase = builder.searchPhrase;
    productId = builder.productId;
  }

  String getProductId() {
    return productId;
  }

  List<Sort> getQuestionSorts() {
    return questionSorts;
  }

  List<Sort> getAnswerSorts() {
    return answerSorts;
  }

  int getLimit() {
    return limit;
  }

  int getOffset() {
    return offset;
  }

  String getSearchPhrase() {
    return searchPhrase;
  }

  @Override
  BazaarException getError() {
    if (limit < 1 || limit > 100) {
      return new BazaarException(String.format("Invalid `limit` value: Parameter 'limit' has invalid value: %d - must be between 1 and 100.", limit));
    }
    return null;
  }

  public static final class Builder extends ConversationsDisplayRequest.Builder<Builder> {
    private final List<Sort> questionSorts, answerSorts;
    private final int limit;
    private final int offset;
    private String searchPhrase;
    private String productId;

    public Builder(@NonNull String productId, int limit, int offset) {
      super();
      this.questionSorts = new ArrayList<>();
      this.answerSorts = new ArrayList<>();
      this.limit = limit;
      this.offset = offset;
      this.productId = productId;
      addFilter(new Filter(Filter.Type.ProductId, EqualityOperator.EQ, productId));

    }

    /**
     * @deprecated renamed to explicitly be the Question sorting option
     *
     * @param sort Question Sort Option
     * @param sortOrder Question Sort Order
     * @return Request Builder
     */
    public Builder addSort(QuestionOptions.Sort sort, SortOrder sortOrder) {
      this.questionSorts.add(new Sort(sort, sortOrder));
      return this;
    }

    /**
     * @param sort Question Sort Option
     * @param sortOrder Question Sort Order
     * @return Request Builder
     */
    public Builder addQuestionSort(QuestionOptions.Sort sort, SortOrder sortOrder) {
      this.questionSorts.add(new Sort(sort, sortOrder));
      return this;
    }

    /**
     * @param sort Question Sort Option
     * @param order Question Sort Order
     * @return Request Builder
     */
    public Builder addAnswerSort(@NonNull AnswerOptions.Sort sort, @NonNull SortOrder order) {
      answerSorts.add(new Sort(sort, order));
      return this;
    }

    public Builder addFilter(QuestionOptions.Filter filter, EqualityOperator equalityOperator, String value) {
      addFilter(new Filter(filter, equalityOperator, value));
      return this;
    }

    public Builder includeSearchPhrase(String search) {
      this.searchPhrase = search;
      return this;
    }

    public QuestionAndAnswerRequest build() {
      return new QuestionAndAnswerRequest(this);
    }
  }
}