package com.bazaarvoice.bvandroidsdk;

import java.util.Collections;
import java.util.List;

public class ProductSentimentsException extends BazaarException {
    protected static final String REQUEST_ERROR_MESSAGE = "Request has errors";
    protected static final String CALL_ON_MAIN_THREAD = "Method call should not happen from the main thread.";

    private final List<Error> errors;

    public static ProductSentimentsException withCallOnMainThread() {
        return new ProductSentimentsException(CALL_ON_MAIN_THREAD, Collections.<Error>emptyList());
    }

    public static ProductSentimentsException withRequestErrors(List<Error> errors) {
        return new ProductSentimentsException(REQUEST_ERROR_MESSAGE, errors);
    }

    public static ProductSentimentsException withNoRequestErrors(String detailMessage) {
        return new ProductSentimentsException(detailMessage, Collections.<Error>emptyList());
    }

    public static ProductSentimentsException withNoRequestErrors(String detailMessage, Throwable throwable) {
        return new ProductSentimentsException(detailMessage, throwable, Collections.<Error>emptyList());
    }

    public ProductSentimentsException(String detailMessage, List<Error> errors) {
        super(detailMessage);
        this.errors = errors;
    }

    public ProductSentimentsException(String detailMessage, Throwable throwable, List<Error> errors) {
        super(detailMessage, throwable);
        this.errors = errors;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public String getErrorListMessages() {
        StringBuilder errorListMessages = new StringBuilder("ProductSentiments Exception\n");
        if(errors != null) {
            for (Error error : errors) {
                if(error != null) {
                  //  errorListMessages.append("Error: ").append(error.getMessage()).append(" Code: ").append(error.getCode());
                }
            }
        }
        return errorListMessages.toString();
    }
}
