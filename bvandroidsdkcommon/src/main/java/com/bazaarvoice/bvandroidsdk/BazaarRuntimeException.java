/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * An exception type used for problems that arise in the use of the
 * Bazaarvoice SDK, during runtime
 */
public class BazaarRuntimeException extends RuntimeException {

    public BazaarRuntimeException(String message) {
        throw new BazaarRuntimeException(message);
    }
}
