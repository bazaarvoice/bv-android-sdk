/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.di;

import com.bazaarvoice.bvandroidsdk.BVAds;

/**
 * TODO: Description Here
 */
public interface DemoAppConfiguration {

    BVAds provideBvAds();

    DemoUserConfiguration provideBvUserComponent();

}
