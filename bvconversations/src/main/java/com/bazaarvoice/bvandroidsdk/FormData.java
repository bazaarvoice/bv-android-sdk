package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import androidx.annotation.Nullable;

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
