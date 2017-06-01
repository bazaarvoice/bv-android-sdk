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

import okhttp3.HttpUrl;

/**
 * Request to get {@link Author}s
 */
public final class AuthorsRequest extends ConversationsDisplayRequest {
  private static final String ENDPOINT = "data/authors.json";

  private final List<Sort> reviewSorts, questionSorts, answerSorts;
  private final List<Include> includes;
  private final List<IncludeType> statistics;

  private AuthorsRequest(Builder builder) {
    super(builder);
    reviewSorts = builder.reviewSorts;
    questionSorts = builder.questionSorts;
    answerSorts = builder.answerSorts;
    includes = builder.includes;
    statistics = builder.statistics;
  }

  List<Sort> getReviewSorts() {
    return reviewSorts;
  }

  List<Sort> getQuestionSorts() {
    return questionSorts;
  }

  List<Sort> getAnswerSorts() {
    return answerSorts;
  }

  List<Include> getIncludes() {
    return includes;
  }

  List<IncludeType> getStatistics() {
    return statistics;
  }

  @Override
  String getEndPoint() {
    return ENDPOINT;
  }

  @Override
  BazaarException getError() {
    return null;
  }

  @Override
  HttpUrl toHttpUrl() {
    HttpUrl.Builder httpUrlBuilder = super.toHttpUrl().newBuilder();

    if (!reviewSorts.isEmpty()){
      httpUrlBuilder.addQueryParameter(kSORT_REVIEW, StringUtils.componentsSeparatedBy(reviewSorts, ","));
    }

    if (!questionSorts.isEmpty()){
      httpUrlBuilder.addQueryParameter(kSORT_QUESTIONS, StringUtils.componentsSeparatedBy(questionSorts, ","));
    }

    if (!answerSorts.isEmpty()){
      httpUrlBuilder.addQueryParameter(kSORT_ANSWERS, StringUtils.componentsSeparatedBy(answerSorts, ","));
    }

    if (!includes.isEmpty()) {
      httpUrlBuilder.addQueryParameter(kINCLUDE, StringUtils.componentsSeparatedBy(includes, ","));
    }

    for (Include include : includes) {
      httpUrlBuilder.addQueryParameter(include.getLimitParamKey(), String.valueOf(include.getLimit()));
    }

    if (!statistics.isEmpty()) {
      httpUrlBuilder.addQueryParameter(kSTATS, StringUtils.componentsSeparatedBy(statistics, ","));
    }

    return httpUrlBuilder.build();
  }

  public static final class Builder extends ConversationsDisplayRequest.Builder<Builder> {
    private final List<Sort> reviewSorts, questionSorts, answerSorts;
    private final List<Include> includes;
    private final List<IncludeType> statistics;

    public Builder(@NonNull String authorId) {
      super();
      this.reviewSorts = new ArrayList<>();
      this.questionSorts = new ArrayList<>();
      this.answerSorts = new ArrayList<>();
      includes = new ArrayList<>();
      this.statistics = new ArrayList<>();
      addFilter(new Filter(Filter.Type.Id, EqualityOperator.EQ, authorId));
    }

    public Builder addReviewSort(@NonNull ReviewOptions.Sort sort, @NonNull SortOrder order) {
      reviewSorts.add(new Sort(sort, order));
      return this;
    }

    public Builder addQuestionSort(@NonNull QuestionOptions.Sort sort, @NonNull SortOrder order) {
      questionSorts.add(new Sort(sort, order));
      return this;
    }

    public Builder addAnswerSort(@NonNull AnswerOptions.Sort sort, @NonNull SortOrder order) {
      answerSorts.add(new Sort(sort, order));
      return this;
    }

    /**
     * @deprecated This method works, but the input param is technically of the
     * wrong type. See {@link #addIncludeContent(AuthorIncludeType, int)} instead
     *
     * @param type Type to include
     * @param limit Limit of type to include
     * @return this builder object
     */
    public Builder addIncludeContent(@NonNull PDPContentType type, int limit) {
      this.includes.add(new Include(type, limit));
      return this;
    }

    /**
     * @deprecated This method works, but the input param is technically of the
     * wrong type. See {@link #addIncludeStatistics(AuthorIncludeType)} instead
     *
     * @param type Type to include
     * @return this builder object
     */
    public Builder addIncludeStatistics(@NonNull PDPContentType type) {
      this.statistics.add(type);
      return this;
    }

    public Builder addIncludeContent(@NonNull AuthorIncludeType type, int limit) {
      this.includes.add(new Include(type, limit));
      return this;
    }

    public Builder addIncludeStatistics(@NonNull AuthorIncludeType type) {
      this.statistics.add(type);
      return this;
    }

    public AuthorsRequest build() {
      return new AuthorsRequest(this);
    }
  }
}
