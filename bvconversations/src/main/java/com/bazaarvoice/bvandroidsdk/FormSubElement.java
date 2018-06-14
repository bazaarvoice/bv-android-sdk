package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * An individual field for a submission form
 */
public class FormSubElement {

  @SerializedName("Id")
  private String id;

  @SerializedName("Type")
  private FormSubElementType type;

  public String getId() {
    return id;
  }

  public FormSubElementType getType() {
    return type;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setType(FormSubElementType type) {
    this.type = type;
  }
}
