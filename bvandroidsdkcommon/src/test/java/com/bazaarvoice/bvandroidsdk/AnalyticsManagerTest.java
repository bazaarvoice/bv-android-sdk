package com.bazaarvoice.bvandroidsdk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class AnalyticsManagerTest {

    String versionName;
    String versionCode;
    String packageName;
    String clientId;
    String uuidTestStr = "0871bbf6-b73e-4841-99f2-5e3d887eaea2";
    UUID uuid;
    @Mock
    AdvertisingIdClient advertisingIdClient;
    OkHttpClient okHttpClient;
    BazaarEnvironment environment;
    @Mock ScheduledExecutorService scheduledExecutorService;
    @Mock ExecutorService immediateExecutorService;
    String shopperAdvertisingApiKey;
    @Mock BVAuthenticatedUser bvAuthenticatedUser;

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
        shopperAdvertisingApiKey = "foobar-bvtestshopperadvertisingid";
        uuid = UUID.fromString(uuidTestStr);

        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(Robolectric.application, clientId)
                .bazaarEnvironment(BazaarEnvironment.STAGING)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();

    }

    ArgumentCaptor<Long> initialDelayCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Long> delayCaptor = ArgumentCaptor.forClass(Long.class);

    @Test
    public void whenCreatedShouldSetupBatchAnalyticsEventEvery10Seconds() {
        // arrange
        Long expectedInitialDelay = 10L;
        Long expectedDelay = 10L;

        // act
        AnalyticsManager subject = new AnalyticsManager(versionName, versionCode, clientId, environment, advertisingIdClient, okHttpClient, immediateExecutorService, scheduledExecutorService, bvAuthenticatedUser, packageName, uuid);

        // assert
        verify(scheduledExecutorService, times(1)).scheduleWithFixedDelay(any(Runnable.class), initialDelayCaptor.capture(), delayCaptor.capture(), any(TimeUnit.class));
        assertEquals(expectedInitialDelay, initialDelayCaptor.getValue());
        assertEquals(expectedDelay, delayCaptor.getValue());
    }

    @Test
    public void whenCreatedShouldGetAdIdInfo() {
        // act
        AnalyticsManager subject = new AnalyticsManager(versionName, versionCode, clientId, environment, advertisingIdClient, okHttpClient, immediateExecutorService, scheduledExecutorService, bvAuthenticatedUser, packageName, uuid);

        verify(advertisingIdClient, times(1)).getAdInfo(any(BVSDK.GetAdInfoCompleteAction.class));
    }

    @Test
    public void whenSendPersonalizationEventShouldProcessImmediately() throws Exception {
        // arrange
        AnalyticsManager subject = new AnalyticsManager(versionName, versionCode, clientId, environment, advertisingIdClient, okHttpClient, immediateExecutorService, scheduledExecutorService, bvAuthenticatedUser, packageName, uuid);

        // act
        subject.sendPersonalizationEvent();

        // assert
        verify(immediateExecutorService, times(1)).execute(any(Runnable.class));
    }

    @After
    public void tearDown() {
        BVSDK.destroy();
    }

}
