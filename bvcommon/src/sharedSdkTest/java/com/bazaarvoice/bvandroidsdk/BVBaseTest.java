/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk_common.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.internal.io.FileSystem;
import okhttp3.mockwebserver.MockWebServer;
import okio.BufferedSource;
import okio.Okio;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {BaseShadows.ShadowNetwork.class})
public abstract class BVBaseTest {

    MockWebServer server = new MockWebServer();
    OkHttpClient okHttpClient;
    Gson gson = new GsonBuilder().create();

    String versionName;
    String versionCode;
    String packageName;
    String uuidTestStr = "0871bbf6-b73e-4841-99f2-5e3d887eaea2";
    UUID uuid;
    String mobileDeviceName;
    String mobileOs;
    String mobileOsVersion;

    //URL roots
    String bazaarvoiceApiBaseUrl;
    String shopperMarketingApiBaseUrl;
    String notificationConfigUrl;

    BazaarEnvironment environment;
    BVLogger bvLogger;
    String bvSdkVersion;

    @Mock AnalyticsManager analyticsManager;
    @Mock BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks;
    @Mock BVAuthenticatedUser bvAuthenticatedUser;
    @Mock BVPixel bvPixel;
    BVUserProvidedData bvUserProvidedData;
    @Mock BVMobileInfo bvMobileInfo;
    Handler handler = new Handler(RuntimeEnvironment.application.getMainLooper());
    HandlerThread handlerThread = new HandlerThread("");

    @Before
    public void setup() {
        initMocks(this);
        // arrange
        versionName = "3.0.0";
        versionCode = "56";
        packageName = "com.mypackagename.app";
        mobileDeviceName = "googlyphone-5p";
        mobileOs = "android";
        mobileOsVersion = "12.1.3";
        okHttpClient = new OkHttpClient();
        environment = BazaarEnvironment.STAGING;
        BVConfig bvConfig = new BVConfig.Builder()
            .clientId("pretendcompany")
            .apiKeyConversations("apiKeyConv")
            .apiKeyShopperAdvertising("apiKeyShopperAd")
            .apiKeyConversationsStores("apiKeyConvStores")
            .apiKeyCurations("apiKeyCurations")
            .apiKeyLocation("apiKeyLocations")
            .apiKeyPIN("apiKeyPin")
            .dryRunAnalytics(false)
            .build();
        uuid = UUID.fromString(uuidTestStr);
        bvLogger = new BVLogger(BVLogLevel.VERBOSE);
        bvSdkVersion = BuildConfig.BVSDK_VERSION_NAME;
        handlerThread.start();

        when(bvMobileInfo.getBvSdkVersion()).thenReturn(bvSdkVersion);
        when(bvMobileInfo.getMobileAppCode()).thenReturn(versionCode);
        when(bvMobileInfo.getMobileAppIdentifier()).thenReturn(packageName);
        when(bvMobileInfo.getMobileAppVersion()).thenReturn(versionName);
        when(bvMobileInfo.getMobileDeviceName()).thenReturn(mobileDeviceName);
        when(bvMobileInfo.getMobileOs()).thenReturn(mobileOs);
        when(bvMobileInfo.getMobileOsVersion()).thenReturn(mobileOsVersion);
        bvUserProvidedData = new BVUserProvidedData(RuntimeEnvironment.application, bvConfig, bvMobileInfo);

        modifyPropertiesToInitSDK();
        // Builder used to initialize the Bazaarvoice SDKs
        BVRootApiUrls rootApiUrls = new BVRootApiUrls(shopperMarketingApiBaseUrl, bazaarvoiceApiBaseUrl, notificationConfigUrl);
        OkHttpClient okHttpClient = new OkHttpClient();
        BVSDK.BVWorkerData bvWorkerData = new BVSDK.BVWorkerData(analyticsManager, gson, rootApiUrls, okHttpClient, "bvsdk-android/v"+ BuildConfig.BVSDK_VERSION_NAME, handlerThread, handlerThread.getLooper());
        BVSDK.singleton = new BVSDK(bvUserProvidedData, bvLogger, bvActivityLifecycleCallbacks, bvAuthenticatedUser, handler, handlerThread, bvPixel, bvWorkerData, environment);

        afterInitSdk(BVSDK.getInstance());
    }

    protected void modifyPropertiesToInitSDK() {}

    protected void afterInitSdk(BVSDK bvsdk) {}

    static String jsonFileAsString(String fileName) throws IOException {
        File file = new File("src/test/resources/" + fileName);
        return readFile(file);
    }

    /**
     * Reads InputStream and returns a String. It will close stream after usage.
     *
     * @param stream the stream to read
     * @return the string content
     */
    @NonNull
    public static String readFile(@NonNull final InputStream stream) throws IOException {
        try (final BufferedSource source = Okio.buffer(Okio.source(stream))) {
            return source.readUtf8();
        }
    }

    /**
     * Reads file and returns a String.
     *
     * @param file the file to read
     * @return the string content
     */
    @NonNull
    public static String readFile(@NonNull final File file) throws IOException {
        try (final BufferedSource source = Okio.buffer(FileSystem.SYSTEM.source(file))) {
            return source.readUtf8();
        }
    }
}