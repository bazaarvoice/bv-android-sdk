package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Container for form field map
 */
public class FormData {
  @SerializedName("Fields")
  Map<String, FormField> formFieldMap;

  @Nullable
  public Map<String, FormField> getFormFieldMap() {
    return formFieldMap;
  }
}
