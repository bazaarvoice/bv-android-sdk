/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid;

import android.app.Application;

import com.bazaarvoice.bvandroidsdk.BVLogLevel;
import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.bazaarvoice.bvsdkdemoandroid.conversations.questions.DemoQuestionsCache;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.DemoReviewsCache;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;

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

        Stetho.initializeWithDefaults(this);
        setupBVSDK();
        if (DEBUG) {
            LeakCanary.install(this);
        }
    }

    /**
     * The Builder pattern used here is necessary to prepareCall the BVSDKs.
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
        DemoDataUtil demoDataUtil = DemoDataUtil.getInstance(this);
        String clientId = demoConfigUtils.getClientId();
        String shopperAdvertisingApiKey = demoConfigUtils.getShopperAdPasskey();
        String conversationsApiKey = demoConfigUtils.getConversationsPasskey();
        String conversationsStoresApiKey = demoConfigUtils.getConversationsStoresPasskey();
        String curationsApiKey = demoConfigUtils.getCurationsPasskey();
        String locationApiKey = demoConfigUtils.getLocationPasskey();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(new DemoSdkInterceptor(demoConfigUtils, demoDataUtil))
                .build();

        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(this, clientId)
                .bazaarEnvironment(DemoConstants.ENVIRONMENT)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .apiKeyConversations(conversationsApiKey)
                .apiKeyConversationsStores(conversationsStoresApiKey)
                .apiKeyCurations(curationsApiKey)
                .apiKeyLocation(locationApiKey)
                .logLevel(BVLogLevel.VERBOSE)
                .okHttpClient(okHttpClient)
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
