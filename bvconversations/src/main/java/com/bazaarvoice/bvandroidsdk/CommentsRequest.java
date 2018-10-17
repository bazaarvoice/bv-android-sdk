package com.bazaarvoice.bvandroidsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public class CommentsRequest extends ConversationsDisplayRequest {
  private final int limit, offset;
  private final List<Sort> sorts;
  private final List<CommentIncludeType> includeTypes;
  private final Map<CommentIncludeType, Integer> includeTypeLimitMap;

  CommentsRequest(Builder builder) {
    super(builder);
    this.limit = builder.limit;
    this.offset = builder.offset;
    this.sorts = builder.sorts;
    this.includeTypes = builder.includeTypes;
    this.includeTypeLimitMap = builder.includeTypeLimitMap;
  }

  @Override
  BazaarException getError() {
    return null;
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

  List<CommentIncludeType> getIncludeTypes() {
    return includeTypes;
  }

  Map<CommentIncludeType, Integer> getIncludeTypeLimitMap() {
    return includeTypeLimitMap;
  }

  public static class Builder extends ConversationsDisplayRequest.Builder<Builder> {
    private final int limit, offset;
    private final List<Sort> sorts;
    private final List<CommentIncludeType> includeTypes;
    private final Map<CommentIncludeType, Integer> includeTypeLimitMap;

    private Builder(@NonNull Filter filter, @IntRange(from = 0) int limit, @IntRange(from = 0) int offset) {
      addFilter(filter);
      this.limit = limit;
      this.offset = offset;
      this.sorts = new ArrayList<>();
      includeTypes = new ArrayList<>();
      includeTypeLimitMap = new HashMap<>();
    }

    public Builder(@NonNull String reviewId, @IntRange(from = 0) int limit, @IntRange(from = 0) int offset) {
      this(new Filter(CommentOptions.Filter.REVIEW_ID, EqualityOperator.EQ, reviewId), limit, offset);
    }

    public Builder(@NonNull String commentId) {
      this(new Filter(CommentOptions.Filter.ID, EqualityOperator.EQ, commentId), 1, 0);
    }

    public Builder addSort(@NonNull CommentOptions.Sort sort, @NonNull SortOrder sortOrder) {
      this.sorts.add(new Sort(sort, sortOrder));
      return this;
    }

    public Builder addFilter(@NonNull CommentOptions.Filter filter, @NonNull EqualityOperator equalityOperator, @NonNull String value) {
      addFilter(new Filter(filter, equalityOperator, value));
      return this;
    }

    public Builder addIncludeContent(@NonNull CommentIncludeType commentIncludeType, int limit) {
      includeTypes.add(commentIncludeType);
      includeTypeLimitMap.put(commentIncludeType, limit);
      return this;
    }

    public CommentsRequest build() {
      return new CommentsRequest(this);
    }
  }
}
