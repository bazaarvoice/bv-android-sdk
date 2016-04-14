/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.di;

import com.bazaarvoice.bvandroidsdk.BVAds;
import com.bazaarvoice.bvandroidsdk.BVSDK;

/**
 * TODO: Description Here
 */
public class DemoAppConfigurationImpl implements DemoAppConfiguration {

    private static DemoAppConfigurationImpl instance;
    private static BVAds bvAds;

    private DemoAppConfigurationImpl() {}

    public static DemoAppConfigurationImpl getInstance() {
        if (instance == null) {
            instance = new DemoAppConfigurationImpl();
        }

        return instance;
    }

    @Override
    public BVAds provideBvAds() {
        if (bvAds == null) {
            bvAds = new BVAds();
            BVSDK.getInstance().setUserAuthString(DemoUserConfigurationImpl.BV_USER_AUTH_STRING);
        }

        return bvAds;
    }

    @Override
    public DemoUserConfiguration provideBvUserComponent() {
        return new DemoUserConfigurationImpl();
    }

}
