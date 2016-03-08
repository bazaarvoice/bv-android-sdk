/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk;

import android.app.Application;

import com.bazaarvoice.bvandroidsdk.BVLogLevel;
import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;
import com.example.bazaarvoice.bv_android_sdk.di.AppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.UserConfiguration;

/**
 * TODO: Description Here
 */
public class BVApplication extends Application {

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
        UserConfiguration userConfiguration = AppConfigurationImpl.getInstance().provideBvUserComponent();

        BazaarEnvironment bazaarEnvironment = userConfiguration.provideBazaarEnvironment();
        String clientId = userConfiguration.provideBvClientId();
        String shopperAdvertisingApiKey = userConfiguration.provideApiKeyShopperAdvertising();
        String conversationsApiKey = userConfiguration.provideApiKeyConversations();

        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(this, clientId)
                .bazaarEnvironment(bazaarEnvironment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .apiKeyConversations(conversationsApiKey)
                .logLevel(BVLogLevel.VERBOSE)
                .build();
    }
}
