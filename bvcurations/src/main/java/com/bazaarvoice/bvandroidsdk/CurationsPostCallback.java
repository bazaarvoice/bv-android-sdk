/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * TODO: Describe file here.
 */
public interface CurationsPostCallback {

    void onSuccess(CurationsPostResponse response);

    void onFailure(Throwable throwable);
}