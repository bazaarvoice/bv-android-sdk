package com.bazaarvoice.bvandroidsdk;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public abstract class SortableProductRequest extends ConversationsDisplayRequest {
  private final List<Sort> reviewSorts, questionSorts, answerSorts;
  private final List<Filter> reviewFilter, questionFilter,commentFilter,authorFilter;
  private final List<Include> includes;
  private final List<PDPContentType> statistics;
  private boolean incentivizedStats;


  SortableProductRequest(Builder builder) {
    super(builder);
    reviewSorts = builder.reviewSorts;
    questionSorts = builder.questionSorts;
    answerSorts = builder.answerSorts;
    reviewFilter = builder.reviewFilter;
    questionFilter = builder.questionFilter;
    commentFilter=builder.commentFilter;
    authorFilter=builder.authorFilter;
    includes = builder.includes;
    statistics = builder.statistics;
    incentivizedStats = builder.incentivizedStats;
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

  public List<Filter> getReviewFilter() {
    return reviewFilter;
  }

  public List<Filter> getQuestionFilter() {
    return questionFilter;
  }


  Boolean getIncentivizedStats() {
    return incentivizedStats;
  }

  @Override
  BazaarException getError() {
    return null;
  }

  public static abstract class Builder<BuilderType, RequestType> extends ConversationsDisplayRequest.Builder<BuilderType> {
    private final List<Sort> reviewSorts = new ArrayList<>(),
        questionSorts  = new ArrayList<>(),
        answerSorts = new ArrayList<>();
    List<Filter> reviewFilter = new ArrayList<>(),
            questionFilter  = new ArrayList<>(),
            commentFilter = new ArrayList<>(),
            authorFilter=new ArrayList<>();
    private final List<Include> includes = new ArrayList<>();
    private final List<PDPContentType> statistics = new ArrayList<>();
    private boolean incentivizedStats = false;

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

    public BuilderType addReviewFilter(ReviewOptions.Filter filter, EqualityOperator equalityOperator, String value) {
      reviewFilter.add(new Filter(filter, equalityOperator, value));
      return (BuilderType) this;
    }

    public BuilderType addQuestionFilter(QuestionOptions.Filter filter, EqualityOperator equalityOperator, String value) {
      questionFilter.add(new Filter(filter, equalityOperator, value));
      return (BuilderType) this;
    }

    public BuilderType addCommentFilter(CommentOptions.Filter filter, EqualityOperator equalityOperator, String value) {
      commentFilter.add(new Filter(filter, equalityOperator, value));
      return (BuilderType) this;
    }

    public BuilderType addAuthorFilter(AuthorOptions.Filter filter, EqualityOperator equalityOperator, String value) {
      authorFilter.add(new Filter(filter, equalityOperator, value));
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

    public BuilderType addSort(ProductOptions.Sort sort, SortOrder sortOrder) {
      addSort(new Sort(sort, sortOrder));
      return (BuilderType) this;
    }

    public BuilderType addIncentivizedStats(Boolean incentivizedStats){
      this.incentivizedStats = incentivizedStats;
      return (BuilderType) this;
    }

    public abstract RequestType build();
  }
}
