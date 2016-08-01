package com.bazaarvoice.bvandroidsdk;

/**
 * TODO: Describe file here.
 */
interface BVCallback<ResponseType> {
    void onSuccess(ResponseType response);
    void onFailure(BazaarException exception);
}
