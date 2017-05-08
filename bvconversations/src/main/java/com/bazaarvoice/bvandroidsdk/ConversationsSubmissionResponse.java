package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public abstract class ConversationsSubmissionResponse extends ConversationsResponse {
  @SerializedName("Data")
  private FormData formData;

  @SerializedName("FormErrors")
  private FormError formErrors;

  @Nullable
  public FormData getFormData() {
    return formData;
  }

  @Nullable
  public FormError getFormErrors() {
    return formErrors;
  }
}
