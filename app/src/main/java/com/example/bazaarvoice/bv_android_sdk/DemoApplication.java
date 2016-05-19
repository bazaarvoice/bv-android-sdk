/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk;

import android.app.Application;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.example.bazaarvoice.bv_android_sdk.recommendations.DemoProductsCache;
import com.example.bazaarvoice.bv_android_sdk.utils.DemoConfigUtils;

import java.util.Collections;

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
        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(this);
        String clientId = demoConfigUtils.getClientId();
        String shopperAdvertisingApiKey = demoConfigUtils.getShopperAdPasskey();
        String conversationsApiKey = demoConfigUtils.getConversationsPasskey();
        String curationsApiKey = demoConfigUtils.getCurationsPasskey();

        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(this, clientId)
                .bazaarEnvironment(DemoConstants.ENVIRONMENT)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .apiKeyConversations(conversationsApiKey)
                .apiKeyCurations(curationsApiKey)
                .logLevel(DemoConstants.LOG_LEVEL)
                .build();

        // Set user auth string which you may not have until after a user signs in
        BVSDK.getInstance().setUserAuthString(DemoConstants.BV_USER_AUTH_STRING);
    }

    public static void cleanUp() {
        DemoProductsCache.putBvProducts(Collections.<BVProduct>emptyList());
    }
}
