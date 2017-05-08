package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * An individual field for a submission form
 */
public class FormField {
  @SerializedName("Options")
  private List<FormFieldOption> formFieldOptions;

  @SerializedName("Required")
  private boolean isRequired;

  @SerializedName("Id")
  private String id;

  @SerializedName("Label")
  private String label;

  @SerializedName("Type")
  private String type;

  @SerializedName("Value")
  private String value;

  @SerializedName("MinLength")
  private int minLength;

  @SerializedName("MaxLength")
  private int maxLength;

  @SerializedName("Default")
  private boolean isDefault;

  public List<FormFieldOption> getFormFieldOptions() {
    return formFieldOptions;
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

  public String getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public int getMinLength() {
    return minLength;
  }

  public int getMaxLength() {
    return maxLength;
  }

  public boolean isDefault() {
    return isDefault;
  }
}
