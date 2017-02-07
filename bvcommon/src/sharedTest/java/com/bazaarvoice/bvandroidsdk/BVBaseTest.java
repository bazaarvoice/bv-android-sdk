/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.os.Handler;
import android.os.HandlerThread;

import com.bazaarvoice.bvandroidsdk_common.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.util.Scanner;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;

import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {Shadows.ShadowNetwork.class})
public abstract class BVBaseTest {

    MockWebServer server = new MockWebServer();
    OkHttpClient okHttpClient;
    Gson gson = new GsonBuilder().create();

    String versionName;
    String versionCode;
    String packageName;
    String clientId;
    String uuidTestStr = "0871bbf6-b73e-4841-99f2-5e3d887eaea2";
    UUID uuid;

    //API keys
    String curationsApiKey;
    String shopperAdvertisingApiKey;
    String conversationApiKey;
    String conversationStoresApiKey;
    String locationApiKey;
    String pinApiKey;

    //URL roots
    String bazaarvoiceApiBaseUrl;
    String shopperMarketingApiBaseUrl;
    String notificationConfigUrl;

    BazaarEnvironment environment;
    BVLogLevel bvLogLevel;
    String bvSdkVersion;

    AnalyticsManager analyticsManager = mock(AnalyticsManager.class);
    BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks = mock(BVActivityLifecycleCallbacks.class);
    BVAuthenticatedUser bvAuthenticatedUser = mock(BVAuthenticatedUser.class);
    Handler handler = new Handler(RuntimeEnvironment.application.getMainLooper());
    HandlerThread handlerThread = new HandlerThread("");

    @Before
    public void setup() {
        // arrange
        versionName = "3.0.0";
        versionCode = "56";
        packageName = "com.mypackagename.app";
        clientId = "pretendcompany";
        MockitoAnnotations.initMocks(this);
        okHttpClient = new OkHttpClient();
        environment = BazaarEnvironment.STAGING;
        curationsApiKey = "foobar-bvtestcurationskey";
        uuid = UUID.fromString(uuidTestStr);
        bvLogLevel = BVLogLevel.VERBOSE;
        bvSdkVersion = BuildConfig.BVSDK_VERSION_NAME;
        handlerThread.start();

        modifyPropertiesToInitSDK();
        // Builder used to initialize the Bazaarvoice SDKs
        Logger.setLogLevel(bvLogLevel);
        BVApiKeys keys = new BVApiKeys(shopperAdvertisingApiKey, conversationApiKey, conversationStoresApiKey, curationsApiKey, locationApiKey, pinApiKey);
        BVRootApiUrls rootApiUrls = new BVRootApiUrls(shopperMarketingApiBaseUrl, bazaarvoiceApiBaseUrl, notificationConfigUrl);
        BVSDK.singleton = new BVSDK(RuntimeEnvironment.application, clientId, environment, keys, bvLogLevel, new OkHttpClient(), analyticsManager, bvActivityLifecycleCallbacks, bvAuthenticatedUser, gson, rootApiUrls, handler, handlerThread);

        afterInitSdk(BVSDK.getInstance());
    }

    protected void modifyPropertiesToInitSDK() {}

    protected void afterInitSdk(BVSDK bvsdk) {}

    String jsonFileAsString(String fileName) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
        return convertStreamToString(in);
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}