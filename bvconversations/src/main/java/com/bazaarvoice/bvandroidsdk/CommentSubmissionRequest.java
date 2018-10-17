package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

/**
 * Request for submitting {@link Comment}s
 */
public class CommentSubmissionRequest extends ConversationsSubmissionRequest {
  private final String reviewId;
  private final String commentText;
  private final String title;

  CommentSubmissionRequest(Builder builder) {
    super(builder);
    this.reviewId = builder.reviewId;
    this.commentText = builder.commentText;
    this.title = builder.title;
  }

  public String getReviewId() {
    return reviewId;
  }

  public String getCommentText() {
    return commentText;
  }

  public String getTitle() {
    return title;
  }

  @Override
  BazaarException getError() {
    return null;
  }

  public static class Builder extends ConversationsSubmissionRequest.Builder<Builder> {
    private final String reviewId, commentText;
    private String title;

    public Builder(@NonNull Action action, @NonNull String reviewId, @NonNull String commentText) {
      super(action);
      this.reviewId = reviewId;
      this.commentText = commentText;
    }

    public Builder title(@NonNull String title) {
      this.title = title;
      return this;
    }

    @Override
    PhotoUpload.ContentType getPhotoContentType() {
      return PhotoUpload.ContentType.COMMENT;
    }

    public CommentSubmissionRequest build() {
      return new CommentSubmissionRequest(this);
    }
  }
}
