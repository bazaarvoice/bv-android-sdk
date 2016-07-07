/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.util.Scanner;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;

import static org.mockito.Mockito.mock;

@RunWith(RobolectricGradleTestRunner.class)
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

    //URL roots
    String conversationsApiBaseUrl;
    String curationsApiBaseUrl;
    String curationsPostApiBaseUrl;
    String shopperMarketingApiBaseUrl;

    BazaarEnvironment environment;
    BVLogLevel bvLogLevel;

    AnalyticsManager analyticsManager = mock(AnalyticsManager.class);
    BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks = mock(BVActivityLifecycleCallbacks.class);
    BVAuthenticatedUser bvAuthenticatedUser = mock(BVAuthenticatedUser.class);

    Handler handler = new Handler(Looper.getMainLooper());
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

        modifyPropertiesToInitSDK();
        // Builder used to initialize the Bazaarvoice SDKs
        Logger.setLogLevel(bvLogLevel);
        BVApiKeys keys = new BVApiKeys(shopperAdvertisingApiKey, conversationApiKey, curationsApiKey);
        BVRootApiUrls rootApiUrls = new BVRootApiUrls(shopperMarketingApiBaseUrl, curationsApiBaseUrl, curationsPostApiBaseUrl, conversationsApiBaseUrl);
        BVSDK.singleton = new BVSDK(RuntimeEnvironment.application, clientId, environment, keys, bvLogLevel, new OkHttpClient(), analyticsManager, bvActivityLifecycleCallbacks, bvAuthenticatedUser, gson, rootApiUrls, handler);
    }

    abstract void modifyPropertiesToInitSDK();

    String jsonFileAsString(String fileName) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
        return convertStreamToString(in);
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}