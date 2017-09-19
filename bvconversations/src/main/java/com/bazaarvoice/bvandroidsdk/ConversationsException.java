package com.bazaarvoice.bvandroidsdk;

import java.util.Collections;
import java.util.List;

public class ConversationsException extends BazaarException {
  protected static final String REQUEST_ERROR_MESSAGE = "Request has errors";
  protected static final String CALL_ON_MAIN_THREAD = "Method call should not happen from the main thread.";

  private final List<Error> errors;

  public static ConversationsException withCallOnMainThread() {
    return new ConversationsException(CALL_ON_MAIN_THREAD, Collections.<Error>emptyList());
  }

  public static ConversationsException withRequestErrors(List<Error> errors) {
    return new ConversationsException(REQUEST_ERROR_MESSAGE, errors);
  }

  public static ConversationsException withNoRequestErrors(String detailMessage) {
    return new ConversationsException(detailMessage, Collections.<Error>emptyList());
  }

  public static ConversationsException withNoRequestErrors(String detailMessage, Throwable throwable) {
    return new ConversationsException(detailMessage, throwable, Collections.<Error>emptyList());
  }

  public ConversationsException(String detailMessage, List<Error> errors) {
    super(detailMessage);
    this.errors = errors;
  }

  public ConversationsException(String detailMessage, Throwable throwable, List<Error> errors) {
    super(detailMessage, throwable);
    this.errors = errors;
  }

  public List<Error> getErrors() {
    return errors;
  }
}
