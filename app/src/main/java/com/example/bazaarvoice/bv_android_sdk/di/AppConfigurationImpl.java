/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.di;

import com.bazaarvoice.bvandroidsdk.BVAds;
import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.example.bazaarvoice.bv_android_sdk.recommendations.data.BvRepository;
import com.example.bazaarvoice.bv_android_sdk.recommendations.data.BvRepositoryImpl;

/**
 * TODO: Description Here
 */
public class AppConfigurationImpl implements AppConfiguration {

    private static AppConfigurationImpl instance;
    private static BVAds bvAds;

    private AppConfigurationImpl() {}

    public static AppConfigurationImpl getInstance() {
        if (instance == null) {
            instance = new AppConfigurationImpl();
        }

        return instance;
    }

    @Override
    public BvRepository provideBvRepository() {
        return new BvRepositoryImpl(provideBvUserComponent().provideBvRecommendations());
    }

    @Override
    public BVAds provideBvAds() {
        if (bvAds == null) {
            bvAds = new BVAds();
            BVSDK.getInstance().setUserAuthString(UserConfigurationImpl.BV_USER_AUTH_STRING);
        }

        return bvAds;
    }

    @Override
    public UserConfiguration provideBvUserComponent() {
        return new UserConfigurationImpl();
    }

}
