package com.bazaarvoice.bvandroidsdk;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * An individual field for a submission form
 */
public class FormField {
  @SerializedName(value = "Options", alternate = "options")
  private List<FormFieldOption> formFieldOptions;

  @SerializedName(value = "Required", alternate = "required")
  private boolean isRequired;

  @SerializedName(value = "Id", alternate = "id")
  private String id;

  @SerializedName(value = "Label", alternate = "label")
  private String label;

  @SerializedName(value = "Type", alternate = "type")
  private FormInputType formInputType;

  @SerializedName(value = "Value", alternate = "value")
  private String value;

  @SerializedName(value = "MinLength", alternate = "minLength")
  private int minLength;

  @SerializedName(value = "MaxLength", alternate = "maxLength")
  private int maxLength;

  @SerializedName(value = "Default", alternate = "isDefault")
  private boolean isDefault;

  @SerializedName(value = "autoPopulate")
  private boolean autoPopulate;

  @SerializedName(value = "valuesLabels")
  private Map<String, String> valueLabels;

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

  public boolean isAutoPopulate() {
    return autoPopulate;
  }

  public Map<String, String> getValueLabels() {
    return valueLabels;
  }
}
