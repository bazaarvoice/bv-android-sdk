package com.bazaarvoice.bvandroidsdk;

import java.util.Collections;
import java.util.List;

public class ConversationsSubmissionException extends ConversationsException {
    private final List<FieldError> fieldErrors;

    public static ConversationsSubmissionException withCallOnMainThread() {
        return new ConversationsSubmissionException(CALL_ON_MAIN_THREAD, Collections.<Error>emptyList(), Collections.<FieldError>emptyList());
    }

    public static ConversationsSubmissionException withRequestErrors(List<Error> errors, List<FieldError> fieldErrors) {
        return new ConversationsSubmissionException(REQUEST_ERROR_MESSAGE, errors, fieldErrors);
    }

    public static ConversationsSubmissionException withNoRequestErrors(String detailMessage) {
        return new ConversationsSubmissionException(detailMessage, Collections.<Error>emptyList(), Collections.<FieldError>emptyList());
    }

    public static ConversationsSubmissionException withNoRequestErrors(String detailMessage, Throwable throwable) {
        return new ConversationsSubmissionException(detailMessage, throwable, Collections.<Error>emptyList(), Collections.<FieldError>emptyList());
    }

    public ConversationsSubmissionException(String message, List<Error> errors, List<FieldError> fieldErrors) {
        super(message, errors);
        this.fieldErrors = fieldErrors;
    }

    public ConversationsSubmissionException(String message, Throwable throwable, List<Error> errors, List<FieldError> fieldErrors) {
        super(message, throwable, errors);
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    @Override
    public String getErrorListMessages() {
        StringBuilder errorListMessages = new StringBuilder(super.getErrorListMessages());
        for (FieldError error : fieldErrors) {
            errorListMessages
                    .append("Error: ")
                    .append(error.getMessage())
                    .append(" Field: ")
                    .append(error.getField())
                    .append(" Code: ")
                    .append(error.getCode());
        }
        return errorListMessages.toString();
    }
}
