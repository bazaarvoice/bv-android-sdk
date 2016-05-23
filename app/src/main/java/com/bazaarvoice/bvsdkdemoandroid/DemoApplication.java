/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid;

import android.app.Application;

import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.bazaarvoice.bvsdkdemoandroid.conversations.questions.DemoQuestionsCache;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.DemoReviewsCache;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.squareup.leakcanary.LeakCanary;

import io.fabric.sdk.android.Fabric;

import static com.bazaarvoice.bvsdkdemoandroid.BuildConfig.DEBUG;
import static com.bazaarvoice.bvsdkdemoandroid.BuildConfig.HAS_CRASHLYTICS_KEY;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        if (HAS_CRASHLYTICS_KEY) {
            Fabric.with(this, crashlyticsKit);
        }

        setupBVSDK();
        if (DEBUG) {
            LeakCanary.install(this);
        }
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
        DemoProductsCache.getInstance().clear();
        DemoReviewsCache.getInstance().clear();
        DemoQuestionsCache.getInstance().clear();
    }
}
