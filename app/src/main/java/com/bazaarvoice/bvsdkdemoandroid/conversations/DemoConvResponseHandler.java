package com.bazaarvoice.bvsdkdemoandroid.conversations;

import com.bazaarvoice.bvandroidsdk.Action;
import com.bazaarvoice.bvandroidsdk.ConversationsSubmissionException;
import com.bazaarvoice.bvandroidsdk.Error;
import com.bazaarvoice.bvandroidsdk.FieldError;

public class DemoConvResponseHandler {
  public String getSubmissionSuccessMessage(Action action) {
    if (action == Action.Preview) {
      return "Preview was successful. Now run with Action.Submit to complete";
    } else {
      return "Submitted review successfully";
    }
  }

  public String getSubmissionErrorMessage(ConversationsSubmissionException exception) {
    StringBuilder stringBuilder = new StringBuilder();
    boolean first = true;
    for (Error error : exception.getErrors()) {
      if (!first) {
        stringBuilder.append("; ");
      }
      first = false;
      stringBuilder
          .append("code: ").append(error.getCode()).append(", ")
          .append("message: ").append(error.getMessage());
    }
    for (FieldError fieldError : exception.getFieldErrors()) {
      if (!first) {
        stringBuilder.append("; ");
      }
      first = false;
      stringBuilder
          .append("code: ").append(fieldError.getCode()).append(", ")
          .append("message: ").append(fieldError.getMessage());
    }
    return stringBuilder.toString();
  }
}
