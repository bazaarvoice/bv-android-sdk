package com.bazaarvoice.bvandroidsdk;

interface BVCallback<ResponseType> {
    void onSuccess(ResponseType response);
    void onFailure(BazaarException exception);
}
