/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk;

import android.app.Application;

import com.bazaarvoice.bvandroidsdk.BVLogLevel;
import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;
import com.example.bazaarvoice.bv_android_sdk.di.DemoAppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfiguration;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfigurationImpl;

/**
 * TODO: Description Here
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        setupBVSDK();
//        LeakCanary.install(this);
    }

    /**
     * The Builder pattern used here is necessary to prepare the BVSDKs.
     * The only field required for all SDKs is the application instance passed to
     * the Builder constructor. Other SDKs require api keys to be set here.
     * <ul>
     *     <li>
     *         BVRecommendations and BVAds require {@link com.bazaarvoice.bvandroidsdk.BVSDK.Builder#apiKeyShopperAdvertising(String)}
     *     </li>
     * </ul>
     */
    private void setupBVSDK() {
        DemoUserConfiguration demoUserConfiguration = DemoAppConfigurationImpl.getInstance().provideBvUserComponent();

        BazaarEnvironment bazaarEnvironment = demoUserConfiguration.provideBazaarEnvironment();
        String clientId = demoUserConfiguration.provideBvClientId();
        String shopperAdvertisingApiKey = demoUserConfiguration.provideApiKeyShopperAdvertising();
        String conversationsApiKey = demoUserConfiguration.provideApiKeyConversations();
        String curationsApiKey = demoUserConfiguration.provideApiKeyCurations();

        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(this, clientId)
                .bazaarEnvironment(bazaarEnvironment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .apiKeyConversations(conversationsApiKey)
                .apiKeyCurations(curationsApiKey)
                .logLevel(BVLogLevel.VERBOSE)
                .build();

        // Set user auth string which you may not have until after a user signs in
        BVSDK.getInstance().setUserAuthString(DemoUserConfigurationImpl.BV_USER_AUTH_STRING);
    }
}
