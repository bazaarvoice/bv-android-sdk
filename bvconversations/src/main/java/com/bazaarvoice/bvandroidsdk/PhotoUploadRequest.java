package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

/**
 * Internal class for uploading photos associate with CGC submission
 */
public class PhotoUploadRequest extends ConversationsSubmissionRequest {
  private final PhotoUpload photoUpload;

  private PhotoUploadRequest(Builder builder) {
    super(builder);
    photoUpload = builder.photoUpload;
  }

  @Override
  BazaarException getError() {
    return null;
  }

  public PhotoUpload getPhotoUpload() {
    return photoUpload;
  }

  public static class Builder extends ConversationsSubmissionRequest.Builder<Builder>{
    private final PhotoUpload photoUpload;

    public Builder(@NonNull PhotoUpload photoUpload) {
      super(Action.Submit);
      this.photoUpload = photoUpload;
    }

    public PhotoUploadRequest build() {
      return new PhotoUploadRequest(this);
    }

    @Override
    PhotoUpload.ContentType getPhotoContentType() {
      return null;
    }
  }
}
