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
    final List<FormField> formFields;

    if (formData == null || formData.getFormFieldMap() == null) {
      formFields = Collections.emptyList();
    } else {
      final Collection<FormField> formFieldCollection = formData.getFormFieldMap().values();
      formFields = new ArrayList<>(formFieldCollection);
    }

    return formFields;
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
}
