package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

public abstract class SortableProductRequest extends ConversationsDisplayRequest {
  private final List<Sort> reviewSorts, questionSorts, answerSorts;
  private final List<Include> includes;
  private final List<PDPContentType> statistics;

  SortableProductRequest(Builder builder) {
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

  List<PDPContentType> getStatistics() {
    return statistics;
  }

  @Override
  public String getEndPoint() {
    return "data/products.json";
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

  public static abstract class Builder<BuilderType, RequestType> extends ConversationsDisplayRequest.Builder<BuilderType> {
    private final List<Sort> reviewSorts = new ArrayList<>(),
        questionSorts  = new ArrayList<>(),
        answerSorts = new ArrayList<>();
    private final List<Include> includes = new ArrayList<>();
    private final List<PDPContentType> statistics = new ArrayList<>();

    Builder() {
      super();
    }

    public BuilderType addReviewSort(ReviewOptions.Sort sort, SortOrder order) {
      reviewSorts.add(new Sort(sort, order));
      return (BuilderType) this;
    }

    public BuilderType addQuestionSort(QuestionOptions.Sort sort, SortOrder order) {
      questionSorts.add(new Sort(sort, order));
      return (BuilderType) this;
    }

    /**
     * @deprecated Including answers is not supported on product calls.
     * @param sort Answer Sort options
     * @param order Sort Order
     * @return this builder
     */
    public BuilderType addAnswerSort(AnswerOptions.Sort sort, SortOrder order) {
      answerSorts.add(new Sort(sort, order));
      return (BuilderType) this;
    }

    /**
     * Type of social content to inlcude with the product request.
     * NOTE: PDPContentType is only supported for statistics, not for Includes.
     *
     * @param type Type of CGC to include
     * @param limit Max number of items to include
     * @return this builder
     */
    public BuilderType addIncludeContent(PDPContentType type, @Nullable Integer limit) {
      this.includes.add(new Include(type, limit));
      return (BuilderType) this;
    }

    public BuilderType addIncludeStatistics(PDPContentType type) {
      this.statistics.add(type);
      return (BuilderType) this;
    }

    public BuilderType addFilter(ProductOptions.Filter filter, EqualityOperator equalityOperator, String value) {
      addFilter(new Filter(filter, equalityOperator, value));
      return (BuilderType) this;
    }

    public abstract RequestType build();
  }
}
