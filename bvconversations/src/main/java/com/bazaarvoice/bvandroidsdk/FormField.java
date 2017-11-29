package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;

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
  private FormInputType formInputType;

  @SerializedName("Value")
  private String value;

  @SerializedName("MinLength")
  private int minLength;

  @SerializedName("MaxLength")
  private int maxLength;

  @SerializedName("Default")
  private boolean isDefault;

  @NonNull
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

  /**
   * @deprecated Use {@link #getFormInputType()} instead
   *
   * @return
   */
  public String getType() {
    return formInputType.getValue();
  }

  public FormInputType getFormInputType() {
    return formInputType;
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

  public void setFormFieldOptions(List<FormFieldOption> formFieldOptions) {
    this.formFieldOptions = formFieldOptions;
  }

  public void setRequired(boolean required) {
    isRequired = required;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setFormInputType(FormInputType formInputType) {
    this.formInputType = formInputType;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setMinLength(int minLength) {
    this.minLength = minLength;
  }

  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }

  public void setDefault(boolean aDefault) {
    isDefault = aDefault;
  }
}
