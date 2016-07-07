package com.bazaarvoice.bvandroidsdk;

/**
 * TODO: Describe file here.
 */
interface BVCallback<T> {
    void onSuccess(T response);
    void onFailure(BazaarException exception);
}
