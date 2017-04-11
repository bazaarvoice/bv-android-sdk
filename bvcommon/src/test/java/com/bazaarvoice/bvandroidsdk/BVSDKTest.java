package com.bazaarvoice.bvandroidsdk;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.bazaarvoice.bvandroidsdk_common.BuildConfig;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.concurrent.ExecutorService;

import okhttp3.OkHttpClient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {BaseShadows.ShadowNetwork.class, BvSdkShadows.BvShadowAsyncTask.class, BaseShadows.ShadowAdIdClientNoLimit.class})
public class BVSDKTest {

    String clientId;
    BazaarEnvironment environment;
    String shopperAdvertisingApiKey;
    String conversationsApiKey;
    String conversationsStoresApiKey;
    String bazaarvoiceApiBaseUrl;
    BVLogLevel bvLogLevel;
    String shopperMarketingApiBaseUrl;
    String notificationConfigUrl;
    String curationsApiKey;
    String locationApiKey;
    String pinApiKey;

    @Mock ExecutorService executorService;
    @Mock AnalyticsManager analyticsManager;
    @Mock BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks;
    @Mock BVAuthenticatedUser bvAuthenticatedUser;
    @Mock AdIdRequestTask adIdRequestTask;
    @Mock BVUserProvidedData bvUserProvidedData;
    @Mock BVPixel bvPixel;
    BVSDK.BVWorkerData bvWorkerData;
    Gson gson;
    Handler handler = new Handler(Looper.getMainLooper());
    HandlerThread bgHandlerThread = new BaseTestUtils.TestHandlerThread();
    BVApiKeys keys;
    BVRootApiUrls rootApiUrls;
    BVConfig bvConfig;
    boolean dryRunAnalytics;

    @Before
    public void setup() {
        initMocks(this);
        clientId = "pretendclient";
        environment = BazaarEnvironment.STAGING;
        shopperAdvertisingApiKey = "foobar-bvtestshopperadvertisingid";
        conversationsApiKey = "bazquux-bvtestconversationsid";
        curationsApiKey = "bazquux-bvtestcurationsid";
        locationApiKey = "bazquux-bvtestlocationid";
        dryRunAnalytics = false;

        bvLogLevel = BVLogLevel.WARNING;
        gson = new Gson();
        shopperAdvertisingApiKey = "/";

        keys = new BVApiKeys(shopperAdvertisingApiKey, conversationsApiKey, conversationsStoresApiKey, curationsApiKey, locationApiKey, pinApiKey);
        rootApiUrls = new BVRootApiUrls(shopperMarketingApiBaseUrl, bazaarvoiceApiBaseUrl, notificationConfigUrl);

        RuntimeEnvironment.getRobolectricPackageManager().addPackage("com.android.vending");
      when(bvUserProvidedData.getApplication()).thenReturn(RuntimeEnvironment.application);

      bvConfig = new BVConfig.Builder()
        .apiKeyConversations(conversationsApiKey)
        .apiKeyConversationsStores(conversationsStoresApiKey)
        .apiKeyCurations(curationsApiKey)
        .apiKeyLocation(locationApiKey)
        .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
        .apiKeyPIN(pinApiKey)
        .clientId(clientId)
        .dryRunAnalytics(dryRunAnalytics)
        .build();

        bvWorkerData = new BVSDK.BVWorkerData(analyticsManager, gson, rootApiUrls, new OkHttpClient(), "bvsdk-android/v"+ BuildConfig.BVSDK_VERSION_NAME, bgHandlerThread.getLooper());
    }

    @Test(expected=IllegalStateException.class)
    public void bvSdkShouldRequireInit() {
        // Trying to use bvsdkCommon before initializing BVSDK should throw exception
        BVSDK.getInstance();
    }

    // region Old Builder

    @Test
    public void bvSdkShouldSetupApplicationContextDeprecated() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();

        assertEquals(RuntimeEnvironment.application.getApplicationContext(), bvsdk.getBvUserProvidedData().getAppContext());
    }

    @Test(expected = IllegalStateException.class)
    public void bvSdkShouldOnlyAllowOneCreationDeprecated() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk2 = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();
    }

    @Test
    public void bvSdkShouldSetupLogLevelDeprecated() {
      BVSDK.destroy();
      BVPixel.destroy();

      // Builder used to initialize the Bazaarvoice SDKs
      BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
          .bazaarEnvironment(environment)
          .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
          .logLevel(BVLogLevel.VERBOSE)
          .build();

      // Should not throw NPE if the BVLogger was set correctly
      bvsdk.getBvLogger().d("foo", "bar");
      bvsdk.getBvLogger().w("baz", "quux");
      bvsdk.getBvLogger().e("blerg", "mazerg");
      bvsdk.getBvLogger().i("erma", "gerd");
    }

    @Test
    public void bvSdkShouldSetupClientIdDeprecated() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();

        assertEquals(clientId, bvsdk.getBvUserProvidedData().getClientId());
    }

    @Test
    public void bvSdkShouldSetupConversationsApiKeyDeprecated() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyConversations(conversationsApiKey)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();

        assertEquals(conversationsApiKey, bvsdk.getBvUserProvidedData().getBvApiKeys().getApiKeyConversations());
    }

    @Test(expected=IllegalArgumentException.class)
    public void bvSdkBuilderShouldRequireValidApplicationDeprecated() {
        // Builder used to initialize the Bazaarvoice SDKs
        new BVSDK.Builder(null, clientId);
    }

    @Test(expected=IllegalArgumentException.class)
    public void bvSdkBuilderShouldRequireValidClientIdDeprecated() {
        String clientId = null;
        new BVSDK.Builder(RuntimeEnvironment.application, clientId);
    }

    private BVSDK createTestBvSdk() {
        return new BVSDK(
            bvUserProvidedData,
            new BVLogger(BVLogLevel.VERBOSE),
            bvActivityLifecycleCallbacks,
            bvAuthenticatedUser,
            handler,
            bgHandlerThread,
            bvPixel,
            bvWorkerData);
    }

    // TODO: Need to verify activity foreground/background as well
    @Test
    public void bvSdkConstructorShouldSetupAppLifecycleAnalytics() throws Exception {
      BVSDK bvsdk = createTestBvSdk();

      ArgumentCaptor<BVMobileAppLifecycleEvent> eventArgumentCaptor = ArgumentCaptor.forClass(BVMobileAppLifecycleEvent.class);
      // Check to see that track was called with a mobile app lifecycle event
      verify(bvPixel).track(eventArgumentCaptor.capture());

      // Check to see that the appState field was set to "launched"
      BVMobileParams bvMobileParams = new BVMobileParams(RuntimeEnvironment.application, clientId);
      BVMobileAppLifecycleEvent capturedEvent = eventArgumentCaptor.getValue();
      capturedEvent.setBvMobileParams(bvMobileParams);
      String actualAppState = (String) capturedEvent.toRaw().get(BVEventKeys.MobileAppLifecycleEvent.APP_STATE);
      assertEquals(BVEventValues.AppState.LAUNCHED.getValue(), actualAppState);
    }

    @Test
    public void bvSdkConstructorShouldSetupBvAuthenticatedUser() {
        BVSDK bvsdk = createTestBvSdk();

        assertEquals(bvAuthenticatedUser, bvsdk.getAuthenticatedUser());
    }

    @Test
    public void bvSdkConstructorShouldSetupAnalyticsManager() {
        BVSDK bvsdk = createTestBvSdk();

        assertEquals(analyticsManager, bvsdk.getBvWorkerData().getAnalyticsManager());
    }

    @Test
    public void bvSdkSetNewUserAuth() {
      BVSDK bvsdk = createTestBvSdk();
      String expectedUserAuthStr = "newuserauthstr";
      bvsdk.setUserAuthString(expectedUserAuthStr);

      verify(bvAuthenticatedUser).setUserAuthString("newuserauthstr");
      verify(bvPixel, times(1)).track(any(BVPersonalizationEvent.class));
    }

    @Test
    public void bvSdkSetNullUserAuthString() {
      BVSDK bvsdk = createTestBvSdk();

      bvsdk.setUserAuthString(null);

      verify(bvAuthenticatedUser, times(0)).setUserAuthString("newuserauthstr");
      verify(bvPixel, times(0)).track(any(BVPersonalizationEvent.class));
    }

    // endregion

    // region New Builder

    @Test
    public void bvSdkShouldSetupApplicationContext() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, environment, bvConfig)
            .build();

        assertEquals(RuntimeEnvironment.application.getApplicationContext(), bvsdk.getBvUserProvidedData().getAppContext());
    }

    @Test(expected = IllegalStateException.class)
    public void bvSdkShouldOnlyAllowOneCreation() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, environment, bvConfig)
            .build();
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk2 = new BVSDK.Builder(RuntimeEnvironment.application, environment, bvConfig)
            .build();
    }

    @Test
    public void bvSdkShouldSetupLogLevel() {
        BVSDK.destroy();
        BVPixel.destroy();

        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, environment, bvConfig)
            .logLevel(BVLogLevel.VERBOSE)
            .build();

        // Should not throw NPE if the BVLogger was set correctly
        bvsdk.getBvLogger().d("foo", "bar");
        bvsdk.getBvLogger().w("baz", "quux");
        bvsdk.getBvLogger().e("blerg", "mazerg");
        bvsdk.getBvLogger().i("erma", "gerd");
    }

    @Test
    public void bvSdkShouldSetupClientId() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, environment, bvConfig)
            .build();

        assertEquals(clientId, bvsdk.getBvUserProvidedData().getClientId());
    }

    @Test
    public void bvSdkShouldSetupConversationsApiKey() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, environment, bvConfig)
            .build();

        assertEquals(conversationsApiKey, bvsdk.getBvUserProvidedData().getBvApiKeys().getApiKeyConversations());
    }

    @Test(expected=IllegalArgumentException.class)
    public void bvSdkBuilderShouldRequireValidApplication() {
        // Builder used to initialize the Bazaarvoice SDKs
        new BVSDK.Builder(null, environment, bvConfig);
    }

    @Test(expected=IllegalStateException.class)
    public void bvSdkBuilderShouldRequireValidClientId() {
        BVConfig badBvConfig = new BVConfig.Builder()
            .apiKeyConversations(conversationsApiKey)
            .apiKeyConversationsStores(conversationsStoresApiKey)
            .apiKeyCurations(curationsApiKey)
            .apiKeyLocation(locationApiKey)
            .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
            .apiKeyPIN(pinApiKey)
            .dryRunAnalytics(dryRunAnalytics)
            .build();
        new BVSDK.Builder(RuntimeEnvironment.application, environment, badBvConfig)
            .build();
    }
    // endregion

    @After
    public void tearDown() {
        BVSDK.destroy();
        BVPixel.destroy();
    }

}
