package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * An individual field for a submission form
 */
public class FormGroup {
  @SerializedName("SubElements")
  private List<FormSubelement> subelements;

  @SerializedName("Required")
  private boolean isRequired;

  @SerializedName("Id")
  private String id;

  @SerializedName("Label")
  private String label;

  @SerializedName("Type")
  private FormInputType type;


  public List<FormSubelement> getSubelements() {
    return subelements;
  }

  public boolean isRequired() {
    return isRequired;
  }

  public String getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }

  public FormInputType getType() {
    return type;
  }
}
