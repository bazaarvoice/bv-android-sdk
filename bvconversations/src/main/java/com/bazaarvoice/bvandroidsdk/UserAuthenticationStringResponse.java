package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class UserAuthenticationStringResponse extends ConversationsSubmissionResponse {
  @SerializedName("Authentication") private UasContainer uasContainer;
  private static class UasContainer { @SerializedName("User") private String uas; }

  public String getUas() {
    return uasContainer != null ? uasContainer.uas : null;
  }
}