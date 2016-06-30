package com.bazaarvoice.bvandroidsdk;

import android.app.Application;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import okhttp3.OkHttpClient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = {Shadows.ShadowNetwork.class})
public class RecommendationsRequestTest {

    String versionName;
    String versionCode;
    String packageName;
    String uuidTestStr = "0871bbf6-b73e-4841-99f2-5e3d887eaea2";
    UUID uuid;
    OkHttpClient okHttpClient;
    BazaarEnvironment environment;
    @Mock ScheduledExecutorService scheduledExecutorService;
    @Mock ExecutorService immediateExecutorService;
    @Mock BVAuthenticatedUser bvAuthenticatedUser;
    @Mock Application application;
    Gson gson;
    @Mock AnalyticsManager analyticsManager;
    @Mock BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks;
    String adId = "testAdId";
    int limit = 13;
    int limitTooBig = 123;
    int limitTooSmall = -123;
    String shopperAdvertisingApiKey = "fooShopperPasskey";
    String apiKeyCurations = "curationsApiKey";
    String clientId = "testClientId";
    String userAuthStr = "123abc";
    String shopperMarketingApiBaseUrl = "https://example.com";
    String curationsApiUrl = "testurl";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        // arrange
        versionName = "3.0.0";
        versionCode = "56";
        packageName = "com.mypackagename.app";
        clientId = "pretendcompany";
        gson = new Gson();
        okHttpClient = new OkHttpClient();
        environment = BazaarEnvironment.STAGING;
        shopperAdvertisingApiKey = "foobar-bvtestshopperadvertisingid";
        uuid = UUID.fromString(uuidTestStr);

        BVSDK.instance = new BVSDK(RuntimeEnvironment.application, clientId, environment, shopperAdvertisingApiKey, shopperAdvertisingApiKey, apiKeyCurations, BVLogLevel.WARNING, new OkHttpClient(), analyticsManager, bvActivityLifecycleCallbacks, bvAuthenticatedUser, gson, shopperMarketingApiBaseUrl, curationsApiUrl, curationsApiUrl);

        when(bvAuthenticatedUser.getUserAuthString()).thenReturn(userAuthStr);
    }

    @Test
    public void canFormRequestWithProductId() {
        RecommendationsRequest request = new RecommendationsRequest.Builder(limit)
                .productId("foo")
                .build();
        String actualUrlStr = RecommendationsRequest.toUrlString(shopperMarketingApiBaseUrl, adId, shopperAdvertisingApiKey, clientId, request);
        checkUrlStr(actualUrlStr, request);
    }

    @Test
    public void canFormRequestWithCategoryId() {
        RecommendationsRequest request = new RecommendationsRequest.Builder(limit)
                .categoryId("foo")
                .build();
        String actualUrlStr = RecommendationsRequest.toUrlString(shopperMarketingApiBaseUrl, adId, shopperAdvertisingApiKey, clientId, request);
        checkUrlStr(actualUrlStr, request);
    }

    private void checkUrlStr(String actualUrlStr, RecommendationsRequest request) {
        assertEquals(expectedRequestUrl(shopperMarketingApiBaseUrl, adId, shopperAdvertisingApiKey, limit, clientId, request.getProductId(), request.getCategoryId()), actualUrlStr);
    }

    @Test
    public void canFormRequestWithOnlyLimit() {
        RecommendationsRequest request = new RecommendationsRequest.Builder(limit).build();
        String actualUrlStr = RecommendationsRequest.toUrlString(shopperMarketingApiBaseUrl, adId, shopperAdvertisingApiKey, clientId, request);
        checkUrlStr(actualUrlStr, request);
    }

    private String expectedRequestUrl(String baseUrl, String adId, String shopperAdvertisingApiKey, int limit, String clientId, String productId, String categoryId) {
        String requestUrl = baseUrl + "/recommendations/magpie_idfa_" + adId + "?passKey=" + shopperAdvertisingApiKey;
        if (productId != null && !productId.isEmpty()) {
            requestUrl += "&product=" + clientId + "/" + productId;
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            requestUrl += "&category=" + clientId + "/" + categoryId;
        }
        requestUrl += "&limit=" + limit + "&client=" + clientId;
        return requestUrl;
    }

    @Test
    public void limitTooSmall() {
        try {
            new RecommendationsRequest.Builder(limitTooSmall).build();
            fail();
        } catch (IllegalArgumentException expected) { /** expected **/ }
    }

    @Test
    public void limitTooBig() {
        try {
            new RecommendationsRequest.Builder(limitTooBig).build();
            fail();
        } catch (IllegalArgumentException expected) { /** expected **/ }
    }

    @Test
    public void productIdAndCategoryIdMutuallyExclusive() {
        try {
            new RecommendationsRequest.Builder(limit)
                    .productId("prod123")
                    .categoryId("cat123");
            fail();
        } catch (IllegalArgumentException expected) { /** expected **/ }

        try {
            new RecommendationsRequest.Builder(limit)
                    .categoryId("cat123")
                    .productId("prod123");
            fail();
        } catch (IllegalArgumentException expected) { /** expected **/ }
    }

}