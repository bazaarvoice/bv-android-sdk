package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * The form of data the Conversations API expects to receive for a particular input
 */
public enum FormGroupType {
  @SerializedName("InputGroup") INPUT_GROUP("InputGroup");

  private final String value;

  FormGroupType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
