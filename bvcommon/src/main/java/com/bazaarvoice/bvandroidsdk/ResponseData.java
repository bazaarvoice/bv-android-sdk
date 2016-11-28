package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 4/4/16.
 */
class ResponseData {
    private boolean didSucceed;
    private Throwable errorThrowable;
    private Object response;

    ResponseData(boolean didSucceed, Throwable errorThrowable, Object response) {
        this.didSucceed = didSucceed;
        this.errorThrowable = errorThrowable;
        this.response = response;
    }

    boolean isDidSucceed() {
        return didSucceed;
    }

    Throwable getErrorThrowable() {
        return errorThrowable;
    }

    Object getResponse() {
        return response;
    }
}
