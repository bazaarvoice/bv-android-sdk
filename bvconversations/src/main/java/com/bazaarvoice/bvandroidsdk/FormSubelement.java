package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * An individual field for a submission form
 */
public class FormSubelement {

  @SerializedName("Id")
  private String id;

  @SerializedName("Type")
  private FormInputType type;

  public String getId() {
    return id;
  }

  public FormInputType getType() {
    return type;
  }
}
