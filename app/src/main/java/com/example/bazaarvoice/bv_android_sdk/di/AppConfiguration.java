/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.di;

import com.bazaarvoice.bvandroidsdk.BVAds;
import com.example.bazaarvoice.bv_android_sdk.recommendations.data.BvRepository;

/**
 * TODO: Description Here
 */
public interface AppConfiguration {

    BvRepository provideBvRepository();

    BVAds provideBvAds();

    UserConfiguration provideBvUserComponent();

}
