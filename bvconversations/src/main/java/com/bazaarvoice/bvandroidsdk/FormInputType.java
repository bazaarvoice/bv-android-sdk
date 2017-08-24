package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * The form of data the Conversations API expects to receive for a particular input
 */
public enum FormInputType {
  @SerializedName("BooleanInput") BOOLEAN("BooleanInput"),
  @SerializedName("FileInput") FILE("FileInput"),
  @SerializedName("IntegerInput") INTEGER("IntegerInput"),
  @SerializedName("SelectInput") SELECT("SelectInput"),
  @SerializedName("TextAreaInput") TEXT_AREA("TextAreaInput"),
  @SerializedName("TextInput") TEXT_INPUT("TextInput");

  private final String value;

  FormInputType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
