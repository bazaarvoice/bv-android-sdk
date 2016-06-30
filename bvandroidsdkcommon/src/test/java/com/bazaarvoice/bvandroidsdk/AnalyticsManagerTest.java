package com.bazaarvoice.bvandroidsdk;

import android.app.Application;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = {Shadows.ShadowNetwork.class, Shadows.BvShadowAsyncTask.class, Shadows.ShadowAdIdClient.class})
public class AnalyticsManagerTest {
    String versionName;
    String versionCode;
    String packageName;
    String clientId;
    String uuidTestStr = "0871bbf6-b73e-4841-99f2-5e3d887eaea2";
    UUID uuid;
    OkHttpClient okHttpClient;
    BazaarEnvironment environment;
    @Mock ScheduledExecutorService scheduledExecutorService;
    @Mock ExecutorService immediateExecutorService;
    String shopperAdvertisingApiKey;
    @Mock BVAuthenticatedUser bvAuthenticatedUser;
    @Mock Application application;
    String analyticsUrl;
    AnalyticsManager subject;

    @Before
    public void setup() {
        // arrange
        versionName = "3.0.0";
        versionCode = "56";
        packageName = "com.mypackagename.app";
        clientId = "pretendcompany";
        analyticsUrl = "https://www.example.com";
        MockitoAnnotations.initMocks(this);
        okHttpClient = new OkHttpClient();
        environment = BazaarEnvironment.STAGING;
        shopperAdvertisingApiKey = "foobar-bvtestshopperadvertisingid";
        uuid = UUID.fromString(uuidTestStr);

        // Builder used to initialize the Bazaarvoice SDKs
        BVSDK bvsdk = new BVSDK.Builder(RuntimeEnvironment.application, clientId)
                .bazaarEnvironment(BazaarEnvironment.STAGING)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .build();

        subject = new AnalyticsManager(RuntimeEnvironment.application.getApplicationContext(), versionName, versionCode, clientId, analyticsUrl, okHttpClient, immediateExecutorService, scheduledExecutorService, bvAuthenticatedUser, packageName, uuid);
    }

    ArgumentCaptor<Long> initialDelayCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Long> delayCaptor = ArgumentCaptor.forClass(Long.class);

    @Test
    public void whenCreatedShouldSetupBatchAnalyticsEventEvery10Seconds() {
        // arrange
        Long expectedInitialDelay = 10L;
        Long expectedDelay = 10L;

        // assert
        verify(scheduledExecutorService, times(1)).scheduleWithFixedDelay(any(Runnable.class), initialDelayCaptor.capture(), delayCaptor.capture(), any(TimeUnit.class));
        assertEquals(expectedInitialDelay, initialDelayCaptor.getValue());
        assertEquals(expectedDelay, delayCaptor.getValue());
    }

    @Test
    public void whenSendPersonalizationEventShouldProcessImmediately() throws Exception {
        // arrange
        ShadowApplication.runBackgroundTasks();

        // act
        subject.sendPersonalizationEvent();

        // assert
        verify(immediateExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Mock BvAnalyticsSchema schema;

    @Test
    public void shouldNotSendAnalyticsEventWithPii() {
        // arrange
        when(schema.allowAdId()).thenReturn(false);

        // act
        subject.addMagpieData(schema);

        // assert
        assertFalse(schema.getDataMap().containsKey("advertisingId"));
    }

    @After
    public void tearDown() {
        BVSDK.destroy();
    }

}
