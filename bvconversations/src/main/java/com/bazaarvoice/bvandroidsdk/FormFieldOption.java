package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * The FormFieldOption are values that have been configured for a parent
 * FormField element. For more information, please see the Options section
 * of the
 * <a href="https://developer.bazaarvoice.com/docs/read/conversations_api/tutorials/submission/how_to_build_a_subission_form#fields-element">Bazaarvoice Submission Form documentation.</a>
 */
public class FormFieldOption {
  @SerializedName("Label")
  private String label;

  @SerializedName("Value")
  private String value;

  @SerializedName("Selected")
  private boolean selected;

  public String getLabel() {
    return label;
  }

  public String getValue() {
    return value;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}
