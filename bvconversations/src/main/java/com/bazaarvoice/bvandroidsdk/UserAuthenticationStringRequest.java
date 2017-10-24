package com.bazaarvoice.bvandroidsdk;

/**
 * Generates request to retrieve the UAS for a user by providing the
 * {@code bv_authtoken} parsed from intercepting the users confirmation
 */
public class UserAuthenticationStringRequest extends ConversationsSubmissionRequest {
  private static final Builder EMPTY_BUILDER = new Builder(Action.Submit);
  private final String authToken;

  public UserAuthenticationStringRequest(String authToken) {
    super(EMPTY_BUILDER);
    this.authToken = authToken;
  }

  public String getAuthToken() {
    return authToken;
  }

  @Override
  BazaarException getError() {
    return null;
  }

  // TODO: Ideally don't want this request to be forced to inherit Action/Photo/etc. even though it's still submission
  private static class Builder extends ConversationsSubmissionRequest.Builder<Builder> {
    Builder(Action action) {
      super(action);
    }

    @Override
    PhotoUpload.ContentType getPhotoContentType() {
      return null;
    }
  }
}