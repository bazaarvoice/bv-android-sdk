package com.bazaarvoice.bvandroidsdk;

/**
 * Internal helper for data required to submit a video
 */
class VideoSubmissionData {
  private final String videoUrl;
  private final String videoCaption;

  public VideoSubmissionData(String videoUrl, String videoCaption) {
    this.videoUrl = videoUrl;
    this.videoCaption = videoCaption;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public String getVideoCaption() {
    return videoCaption;
  }
}
