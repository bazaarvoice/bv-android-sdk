package com.bazaarvoice.bvandroidsdk;


import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public class Comment extends IncludedContentBase.ProductIncludedContentBase{
  @SerializedName("CID") @Nullable
  private String cid;
  @SerializedName("SourceClient") @Nullable private String sourceClient;
  @SerializedName("ReviewId") @Nullable private String reviewId;
  @SerializedName("CommentText") @Nullable private String commentText;
  @SerializedName("Title") @Nullable private String title;
  @SerializedName("IsSyndicated") private boolean isSyndicated;
  @SerializedName("SyndicationSource") private SyndicatedSource syndicatedSource;

  @Nullable
  public String getCid() {
    return cid;
  }

  @Nullable
  public String getSourceClient() {
    return sourceClient;
  }

  @Nullable
  public String getReviewId() {
    return reviewId;
  }

  @Nullable
  public String getCommentText() {
    return commentText;
  }

  @Nullable
  public String getTitle() {
    return title;
  }

  public boolean isSyndicated() {
    return isSyndicated;
  }

  @Nullable
  public SyndicatedSource getSyndicatedSource() {
    return syndicatedSource;
  }
}
