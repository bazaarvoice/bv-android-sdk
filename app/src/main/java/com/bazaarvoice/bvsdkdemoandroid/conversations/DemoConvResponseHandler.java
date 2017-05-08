package com.bazaarvoice.bvsdkdemoandroid.conversations;

import android.util.Log;

import com.bazaarvoice.bvandroidsdk.Action;
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayResponse;
import com.bazaarvoice.bvandroidsdk.ConversationsSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.Error;
import com.bazaarvoice.bvandroidsdk.FieldError;
import com.bazaarvoice.bvandroidsdk.FormData;
import com.bazaarvoice.bvandroidsdk.FormError;
import com.bazaarvoice.bvandroidsdk.FormField;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;

import java.util.List;
import java.util.Map;

public class DemoConvResponseHandler {
  public interface DisplayMessage {
    void onSuccessMessage(String message);
    void onErrorMessage(String errorMessage);
  }

  public <ResultType> void handleDisplaySuccessResponse(ConversationsDisplayResponse<ResultType> response, DisplayMessage displayMessage) {
    if (!response.getHasErrors()) {
      displayMessage.onSuccessMessage("Successful request");
      return;
    }
    List<Error> errors = response.getErrors();
    String genericBvErrorMessage = getGenericBvErrorMessage(errors);
    displayMessage.onErrorMessage(genericBvErrorMessage);
  }

  public void handleSubmissionSuccessResponse(ConversationsSubmissionResponse response, String apiMethodName, Action action, DisplayMessage displayMessage) {
    if (action == Action.Preview) {
      if (response.getHasErrors()) {
        handlePreviewErrors(response, apiMethodName, displayMessage);
      } else {
        handlePreviewSuccess(response, apiMethodName, displayMessage);
      }
    } else if (DemoConstants.SUBMIT_ACTION == Action.Submit) {
      if (response.getHasErrors()) {
        handleSubmitErrors(response, apiMethodName, displayMessage);
      } else {
        handleSubmitSuccess(response, apiMethodName, displayMessage);
      }
    }
  }

  private void handlePreviewErrors(ConversationsSubmissionResponse response, String apiMethodName, DisplayMessage displayMessage) {
    if (response.getFormErrors() != null) {
      String formDataReqs = checkFormDataRequirements(response);
      Log.d("BVCodeDemo", formDataReqs);
      String formErrorMessage = getFormErrorMessage(response.getFormErrors());
      displayMessage.onErrorMessage(formErrorMessage);
    } else if (response.getErrors() != null) {
      String genericBvErrorMessage = getGenericBvErrorMessage(response.getErrors());
      displayMessage.onErrorMessage(genericBvErrorMessage);
    }
  }

  private void handlePreviewSuccess(ConversationsSubmissionResponse response, String apiMethodName, DisplayMessage displayMessage) {
    String successMessage = String.format("Successful %s preview. Resubmit with Action.SUBMIT to complete.", apiMethodName);
    displayMessage.onSuccessMessage(successMessage);
  }

  private void handleSubmitErrors(ConversationsSubmissionResponse response, String apiMethodName, DisplayMessage displayMessage) {
    if (response.getFormErrors() != null) {
      String formErrorMessage = getFormErrorMessage(response.getFormErrors());
      displayMessage.onErrorMessage(formErrorMessage);
    } else if (response.getErrors() != null) {
      String genericBvErrorMessage = getGenericBvErrorMessage(response.getErrors());
      displayMessage.onErrorMessage(genericBvErrorMessage);
    }
  }

  private void handleSubmitSuccess(ConversationsSubmissionResponse response, String apiMethodName, DisplayMessage displayMessage) {
    String successMessage = String.format("Successful %s submission.", apiMethodName);
    displayMessage.onSuccessMessage(successMessage);
  }

  private String getFormErrorMessage(FormError formErrors) {
    StringBuilder formErrorMessage = new StringBuilder("Response has the following form errors. Make sure to parse them from response.getFormData() and add them to your request.\n\n");
    for (Map.Entry<String, FieldError> formErrorEntry : formErrors.getFieldErrorMap().entrySet()) {
      formErrorMessage.append("key: ").append(formErrorEntry.getKey()).append("\n");
      formErrorMessage.append("value: ").append(formErrorEntry.getValue().getMessage());
      formErrorMessage.append("\n\n");
    }
    return formErrorMessage.toString();
  }

  private String getGenericBvErrorMessage(List<Error> errors) {
    StringBuilder genericBvErrorMessage = new StringBuilder("Response has the following errors.\n\n");
    for (com.bazaarvoice.bvandroidsdk.Error error : errors) {
      genericBvErrorMessage.append("code: ").append(error.getCode());
      genericBvErrorMessage.append("message: ").append(error.getMessage());
      genericBvErrorMessage.append("\n\n");
    }
    return genericBvErrorMessage.toString();
  }

  private String checkFormDataRequirements(ConversationsSubmissionResponse response) {
    StringBuilder formFieldsStr = new StringBuilder("The required and optional form fields for this are:\n\n");
    FormData formData = response.getFormData();
    Map<String, FormField> formFieldMap = formData.getFormFieldMap();
    for (Map.Entry<String, FormField> formFieldEntry : formFieldMap.entrySet()) {
      String formKey = formFieldEntry.getKey();
      FormField formField = formFieldEntry.getValue();
      boolean isRequired = formField.isRequired();
      formFieldsStr.append("key: ").append(formKey).append("\n");
      formFieldsStr.append("isRequired: ").append(isRequired);
      formFieldsStr.append("\n\n");
    }
    return formFieldsStr.toString();
  }
}
