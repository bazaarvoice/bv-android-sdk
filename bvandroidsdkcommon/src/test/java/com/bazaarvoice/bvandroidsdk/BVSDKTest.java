package com.bazaarvoice.bvandroidsdk;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.concurrent.ExecutorService;

import okhttp3.OkHttpClient;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = {Shadows.ShadowNetwork.class, Shadows.BvShadowAsyncTask.class, Shadows.ShadowAdIdClient.class})
public class BVSDKTest {

    String clientId;
    BazaarEnvironment environment;
    String shopperAdvertisingApiKey;
    String conversationsApiKey;
    BVLogLevel bvLogLevel;
    String shopperMarketingApiBaseUrl;
    String curationsApiBaseUrl;
    String curationsPostApiBaseUrl;
    String curationsApiKey;

    @Mock ExecutorService executorService;
    @Mock AnalyticsManager analyticsManager;
    @Mock BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks;
    @Mock BVAuthenticatedUser bvAuthenticatedUser;
    @Mock AdIdRequestTask adIdRequestTask;
    Gson gson;

    @Before
    public void setup() {
        initMocks(this);
        clientId = "pretendclient";
        environment = BazaarEnvironment.STAGING;
        shopperAdvertisingApiKey = "foobar-bvtestshopperadvertisingid";
        conversationsApiKey = "bazquux-bvtestconversationsid";
        curationsApiKey = "bazquux-bvtestcurationsid";

        bvLogLevel = BVLogLevel.WARNING;
        gson = new Gson();
        shopperAdvertisingApiKey = "/";

        executorService = mock(ExecutorService.class);
        analyticsManager = mock(AnalyticsManager.class);
        bvActivityLifecycleCallbacks = mock(BVActivityLifecycleCallbacks.class);
        bvAuthenticatedUser = mock(BVAuthenticatedUser.class);

        RuntimeEnvironment.getRobolectricPackageManager().addPackage("com.android.vending");
    }

    @Test
    public void bvSdkShouldRequireInit() {
        // Trying to use bvsdkCommon before initializing BVSDK should throw exception
        try {
            BVSDK.getInstance();
        } catch (IllegalStateException e) {
            // Should get here
            return;
        }

        // Should not get here
        fail();
    }

    @Test
    public void bvSdkShouldSetupApplicationContext() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();

        assertEquals(RuntimeEnvironment.application.getApplicationContext(), bvsdk.getApplicationContext());
    }

    @Test
    public void bvSdkShouldSetupLogLevel() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .logLevel(BVLogLevel.VERBOSE)
                .build();

        // Should not throw NPE if the logger was set correctly
        Logger.d("foo", "bar");
        Logger.w("baz", "quux");
        Logger.e("blerg", "mazerg");
        Logger.i("erma", "gerd");
    }

    @Test
    public void bvSdkShouldSetupClientId() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();

        assertEquals(clientId, bvsdk.getClientId());
    }

    @Test
    public void bvSdkShouldSetupShopperProfileEnvironment() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();

        assertEquals(environment, bvsdk.getEnvironment());
        ShadowApplication.runBackgroundTasks();
    }

    @Test
    public void bvSdkShouldSetupConversationsApiKey() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyConversations(conversationsApiKey)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();

        assertEquals(conversationsApiKey, bvsdk.getApiKeyConversations());
    }

    @Test
    public void bvSdkBuilderShouldRequireValidApplication() {
        try {
            // Builder used to initialize the Bazaarvoice SDKs
            BVSDK bvsdk = new BVSDK.Builder(null, clientId)
                    .bazaarEnvironment(environment)
                    .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                    .build();

            fail();
        } catch (IllegalStateException exception) {
            // expected to fail here
        }
    }

    @Test
    public void bvSdkBuilderShouldRequireValidClientId() {
        try {
            // Builder used to initialize the Bazaarvoice SDKs
            BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, null)
                    .bazaarEnvironment(environment)
                    .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                    .build();

            fail();
        } catch (IllegalStateException exception) {
            // expected to fail here
        }
    }

    private BVSDK createTestBvSdk() {
        return new BVSDK(RuntimeEnvironment.application, clientId, environment, shopperAdvertisingApiKey, shopperAdvertisingApiKey, curationsApiKey, BVLogLevel.VERBOSE, new OkHttpClient(), analyticsManager, bvActivityLifecycleCallbacks, bvAuthenticatedUser, gson, shopperMarketingApiBaseUrl, curationsApiBaseUrl, curationsPostApiBaseUrl);
    }

    @Test
    public void bvSdkConstructorShouldSendAppLaunchedEvent() {
        BVSDK bvsdk = createTestBvSdk();

        verify(analyticsManager, times(1)).enqueueAppStateEvent(MobileAppLifecycleSchema.AppState.LAUNCHED);
    }

    @Test
    public void bvSdkConstructorShouldSetupBvAuthenticatedUser() {
        BVSDK bvsdk = createTestBvSdk();

        assertEquals(bvAuthenticatedUser, bvsdk.getAuthenticatedUser());
    }

    @Test
    public void bvSdkConstructorShouldSetupAnalyticsManager() {
        BVSDK bvsdk = createTestBvSdk();

        assertEquals(analyticsManager, bvsdk.getAnalyticsManager());
    }

    @Test
    public void bvSdkSetNewUserAuth() {
        BVSDK bvsdk = createTestBvSdk();

        bvsdk.setUserAuthString("newuserauthstr");

        verify(bvAuthenticatedUser).setUserAuthString("newuserauthstr");
        verify(analyticsManager).dispatchSendPersonalizationEvent();
    }

    @Test
    public void bvSdkSetNullUserAuthString() {
        BVSDK bvsdk = createTestBvSdk();

        bvsdk.setUserAuthString(null);

        verify(bvAuthenticatedUser, times(0)).setUserAuthString("newuserauthstr");
        verify(analyticsManager, times(0)).dispatchSendPersonalizationEvent();
    }

    @After
    public void tearDown() {
        BVSDK.destroy();
    }
    
}
