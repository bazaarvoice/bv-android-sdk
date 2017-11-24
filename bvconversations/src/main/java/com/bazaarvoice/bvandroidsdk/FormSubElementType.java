package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * The form of data the Conversations API expects to receive for a particular input
 */
public enum FormSubElementType {
  @SerializedName("Field") FIELD("Field"),
  @SerializedName("Group") GROUP("Group");

  private final String value;

  FormSubElementType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
