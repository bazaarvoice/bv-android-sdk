package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class ConversationsSubmissionResponse extends ConversationsResponse {
  @SerializedName("Data") private FormData formData;
  @SerializedName("FormErrors") private FormError formErrors;
  @SerializedName("SubmissionId") private String submissionId;

  /**
   * @deprecated Use the null-safe {@link #getFormFields()} instead
   * @return container for form fields
   */
  @Nullable
  public FormData getFormData() {
    return formData;
  }

  /**
   * @return form fields available for submission
   */
  @NonNull
  public List<FormField> getFormFields() {
    if (formData == null || formData.getFormFieldMap() == null) {
      return Collections.emptyList();
    } else if (formData.getFormFieldsOrder() != null) {
        ArrayList<FormField> orderedFields = new ArrayList<>(formData.getFormFieldMap().size());
        for (String groupId : formData.getFormFieldsOrder()) {
          orderedFields.add(formData.getFormFieldMap().get(groupId));
        }
        return orderedFields;
    } else {
      return new ArrayList<>(formData.getFormFieldMap().values());
    }
  }

  /**
   * @return form groups
   */
  @NonNull
  public List<FormGroup> getFormGroups() {
    if (formData == null || formData.getFormGroupsMap() == null) {
      return Collections.emptyList();
    } else if (formData.getFormGroupsOrder() != null) {
      ArrayList<FormGroup> orderedGroups = new ArrayList<>(formData.getFormGroupsMap().size());
      for (String groupId : formData.getFormGroupsOrder()) {
        orderedGroups.add(formData.getFormGroupsMap().get(groupId));
      }
      return orderedGroups;
    } else {
      return new ArrayList<>(formData.getFormGroupsMap().values());
    }
  }

  /**
   * @deprecated Use the null-safe {@link #getFieldErrors()} instead
   * @return form field errors for submission
   */
  @Nullable
  public FormError getFormErrors() {
    return formErrors;
  }

  /**
   * @return form field errors for submission
   */
  @NonNull
  public List<FieldError> getFieldErrors() {
    final List<FieldError> fieldErrors;

    if (formErrors == null || formErrors.getFieldErrorMap() == null) {
      fieldErrors = Collections.emptyList();
    } else {
      final Collection<FieldError> fieldErrorCollection = formErrors.getFieldErrorMap().values();
      fieldErrors = new ArrayList<>(fieldErrorCollection);
    }

    return fieldErrors;
  }

  public String getSubmissionId() {
    return submissionId;
  }
}
