package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

/**
 * Internal class for uploading photos associate with CGC submission
 */
public class VideoUploadRequest extends ConversationsSubmissionRequest {
  private final VideoUpload videoUpload;

  private VideoUploadRequest(Builder builder) {
    super(builder);
    videoUpload = builder.videoUpload;
  }

  @Override
  BazaarException getError() {
    return null;
  }

  public VideoUpload getVideoUpload() {
    return videoUpload;
  }

  public static class Builder extends ConversationsSubmissionRequest.Builder<Builder>{
    private final VideoUpload videoUpload;

    public Builder(@NonNull VideoUpload videoUpload) {
      super(Action.Submit);
      this.videoUpload = videoUpload;
    }

    public VideoUploadRequest build() {
      return new VideoUploadRequest(this);
    }

    @Override
    PhotoUpload.ContentType getPhotoContentType() {
      return null;
    }

    @Override
    VideoUpload.ContentType getVideoContentType() {
      return null;
    }
  }
}
