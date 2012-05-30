package com.bazaarvoice;

/**
 * User: gary
 * Date: 4/17/12
 * Time: 10:46 PM
 */
public class BazaarException extends Exception {
    public BazaarException(String detailMessage) {
        super(detailMessage);
    }

    public BazaarException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
