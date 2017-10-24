package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class UserAuthenticationStringResponse extends ConversationsSubmissionResponse {
  @SerializedName("AuthorSubmissionToken") private String authorSubmissionToken;
  @SerializedName("Authentication") private UasContainer uasContainer;
  private static class UasContainer { @SerializedName("User") private String uas; }

  public String getAuthorSubmissionToken() {
    return authorSubmissionToken;
  }

  public String getUas() {
    return uasContainer != null ? uasContainer.uas : null;
  }
}