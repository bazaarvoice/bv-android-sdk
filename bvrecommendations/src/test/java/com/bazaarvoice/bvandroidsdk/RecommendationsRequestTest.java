package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {Shadows.ShadowNetwork.class})
public class RecommendationsRequestTest extends BVBaseTest {

    String adId = "testAdId";
    int limit = 13;
    int limitTooBig = 123;
    int limitTooSmall = -123;
    String userAuthStr = "123abc";

    @Override
    void modifyPropertiesToInitSDK() {
        shopperMarketingApiBaseUrl = "/";
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
        String requestUrl = baseUrl + "recommendations/magpie_idfa_" + adId + "?passKey=" + shopperAdvertisingApiKey;
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