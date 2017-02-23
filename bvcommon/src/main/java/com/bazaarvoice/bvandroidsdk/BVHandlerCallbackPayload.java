/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

class BVHandlerCallbackPayload {
    private final BVCallback externalCB;
    private final BVHandlerCallback internalCB;
    private final Object payload;
    private final BazaarException error;

    public BVHandlerCallbackPayload(BVHandlerCallback internalCB, BVCallback externalCB, Object payload, BazaarException error) {
        this.payload= payload;
        this.externalCB = externalCB;
        this.internalCB = internalCB;
        this.error = error;
    }

    BVCallback getExternalCB() {
        return externalCB;
    }

    BVHandlerCallback getInternalCB() {
        return internalCB;
    }

    Object getPayload() {
        return payload;
    }

    public BazaarException getError() {
        return error;
    }
}