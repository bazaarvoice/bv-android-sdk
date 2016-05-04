package com.bazaarvoice.bvandroidsdk;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.ExecutorService;

import okhttp3.OkHttpClient;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
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

    ExecutorService executorService = mock(ExecutorService.class);
    AdvertisingIdClient advertisingIdClient = mock(AdvertisingIdClient.class);
    AnalyticsManager analyticsManager = mock(AnalyticsManager.class);
    BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks = mock(BVActivityLifecycleCallbacks.class);
    BVAuthenticatedUser bvAuthenticatedUser = mock(BVAuthenticatedUser.class);
    Gson gson;

    @Before
    public void setup() {
        clientId = "pretendclient";
        environment = BazaarEnvironment.STAGING;
        shopperAdvertisingApiKey = "foobar-bvtestshopperadvertisingid";
        conversationsApiKey = "bazquux-bvtestconversationsid";
        curationsApiKey = "bazquux-bvtestcurationsid";

        bvLogLevel = BVLogLevel.WARNING;
        gson = new Gson();
        shopperAdvertisingApiKey = "/";

        executorService = mock(ExecutorService.class);
        advertisingIdClient = mock(AdvertisingIdClient.class);
        analyticsManager = mock(AnalyticsManager.class);
        bvActivityLifecycleCallbacks = mock(BVActivityLifecycleCallbacks.class);
        bvAuthenticatedUser = mock(BVAuthenticatedUser.class);
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
        BVSDK bvsdk = new BVSDK.Builder(Robolectric.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();

        assertEquals(Robolectric.application.getApplicationContext(), bvsdk.getApplicationContext());
    }

    @Test
    public void bvSdkShouldSetupLogLevel() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(Robolectric.application, clientId)
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
        BVSDK bvsdk = new BVSDK.Builder(Robolectric.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();

        assertEquals(clientId, bvsdk.getClientId());
    }

    @Test
    public void bvSdkShouldSetupShopperProfileEnvironment() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(Robolectric.application, clientId)
                .bazaarEnvironment(environment)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();

        assertEquals(environment, bvsdk.getEnvironment());
    }

    @Test
    public void bvSdkShouldSetupConversationsApiKey() {
        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(Robolectric.application, clientId)
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
            BVSDK bvsdk = new BVSDK.Builder(Robolectric.application, null)
                    .bazaarEnvironment(environment)
                    .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                    .build();

            fail();
        } catch (IllegalStateException exception) {
            // expected to fail here
        }
    }

    @Test
    public void bvSdkConstructorShouldSendAppLaunchedEvent() {
        BVSDK bvsdk = new BVSDK(Robolectric.application, clientId, environment, shopperAdvertisingApiKey, shopperAdvertisingApiKey, curationsApiKey, BVLogLevel.VERBOSE, executorService, executorService, new OkHttpClient(), advertisingIdClient, analyticsManager, bvActivityLifecycleCallbacks, bvAuthenticatedUser, gson, shopperMarketingApiBaseUrl, curationsApiBaseUrl, curationsPostApiBaseUrl);

        verify(analyticsManager, times(1)).sendAppStateEvent(MobileAppLifecycleSchema.AppState.LAUNCHED);
    }

    @Test
    public void bvSdkConstructorShouldSetupBvAuthenticatedUser() {
        BVSDK bvsdk = new BVSDK(Robolectric.application, clientId, environment, shopperAdvertisingApiKey, shopperAdvertisingApiKey, curationsApiKey, BVLogLevel.VERBOSE, executorService, executorService, new OkHttpClient(), advertisingIdClient, analyticsManager, bvActivityLifecycleCallbacks, bvAuthenticatedUser, gson, shopperMarketingApiBaseUrl, curationsApiBaseUrl, curationsPostApiBaseUrl);

        assertEquals(bvAuthenticatedUser, bvsdk.getAuthenticatedUser());
    }

    @Test
    public void bvSdkConstructorShouldSetupAdvertisingIdClient() {
        BVSDK bvsdk = new BVSDK(Robolectric.application, clientId, environment, shopperAdvertisingApiKey, shopperAdvertisingApiKey, curationsApiKey, BVLogLevel.VERBOSE, executorService, executorService, new OkHttpClient(), advertisingIdClient, analyticsManager, bvActivityLifecycleCallbacks, bvAuthenticatedUser, gson, shopperMarketingApiBaseUrl, curationsApiBaseUrl, curationsPostApiBaseUrl);

        assertEquals(advertisingIdClient, bvsdk.getAdvertisingIdClient());
    }

    @Test
    public void bvSdkSetNewUserAuth() {
        BVSDK bvsdk = new BVSDK(Robolectric.application, clientId, environment, shopperAdvertisingApiKey, shopperAdvertisingApiKey, curationsApiKey, BVLogLevel.VERBOSE, executorService, executorService, new OkHttpClient(), advertisingIdClient, analyticsManager, bvActivityLifecycleCallbacks, bvAuthenticatedUser, gson, shopperMarketingApiBaseUrl, curationsApiBaseUrl, curationsPostApiBaseUrl);

        bvsdk.setUserAuthString("newuserauthstr");

        verify(bvAuthenticatedUser).setUserAuthString("newuserauthstr");
        verify(analyticsManager).sendPersonalizationEvent();
    }

    @Test
    public void bvSdkSetNullUserAuthString() {
        BVSDK bvsdk = new BVSDK(Robolectric.application, clientId, environment, shopperAdvertisingApiKey, shopperAdvertisingApiKey, curationsApiKey, BVLogLevel.VERBOSE, executorService, executorService, new OkHttpClient(), advertisingIdClient, analyticsManager, bvActivityLifecycleCallbacks, bvAuthenticatedUser, gson, shopperMarketingApiBaseUrl, curationsApiBaseUrl, curationsPostApiBaseUrl);

        bvsdk.setUserAuthString(null);

        verify(bvAuthenticatedUser, times(0)).setUserAuthString("newuserauthstr");
        verify(analyticsManager, times(0)).sendPersonalizationEvent();
    }

    @After
    public void tearDown() {
        BVSDK.destroy();
    }
    
}
