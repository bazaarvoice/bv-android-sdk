package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.mapPutSafe;

/**
 * Request for submitting {@link Comment}s
 */
public class CommentSubmissionRequest extends ConversationsSubmissionRequest {
  private static final String KEY_REVIEW_ID = "ReviewId";
  private static final String KEY_COMMENT_TEXT = "CommentText";
  private static final String KEY_TITLE = "Title";

  private static final String ENDPOINT = "submitreviewcomment.json";

  private final String reviewId;
  private final String commentText;
  private final String title;
  private final String apiKey;

  CommentSubmissionRequest(Builder builder) {
    super(builder);
    this.reviewId = builder.reviewId;
    this.commentText = builder.commentText;
    this.title = builder.title;
    apiKey = BVSDK.getInstance().getBvUserProvidedData().getBvApiKeys().getApiKeyConversations(); // TODO: Inject
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
  String getEndPoint() {
    return ENDPOINT;
  }

  @Override
  BazaarException getError() {
    return null;
  }

  @Override
  protected String getApiKey() {
    return apiKey;
  }

  @Override
  void addRequestQueryParams(Map<String, Object> queryParams) {
    mapPutSafe(queryParams, KEY_REVIEW_ID, reviewId);
    mapPutSafe(queryParams, KEY_COMMENT_TEXT, commentText);
    mapPutSafe(queryParams, KEY_TITLE, title);
  }

  public static class Builder extends ConversationsSubmissionRequest.Builder<Builder> {
    private final String reviewId, commentText;
    private String title;

    public Builder(Action action, String reviewId, String commentText) {
      super(action);
      this.reviewId = reviewId;
      this.commentText = commentText;
    }

    public Builder title(String title) {
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
