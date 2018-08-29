package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {BaseShadows.ShadowNetwork.class})
public class RecommendationsRequestTest extends BVBaseTest {

    String adId = "testAdId";
    int limit = 13;
    int limitTooBig = 123;
    int limitTooSmall = -123;
    String userAuthStr = "123abc";

    @Override
    protected void modifyPropertiesToInitSDK() {
        shopperMarketingApiBaseUrl = "https://bazaarvoiceapibaseurl.com/";
        when(bvAuthenticatedUser.getUserAuthString()).thenReturn(userAuthStr);
    }

    @Test
    public void canFormRequestWithProductId() {
        RecommendationsRequest request = new RecommendationsRequest.Builder(limit)
                .productId("foo")
                .build();
        String actualUrlStr = RecommendationsRequest.toUrlString(BVSDK.getInstance(), adId, request);
        checkUrlStr(actualUrlStr, request);
    }

    @Test
    public void canFormRequestWithCategoryId() {
        RecommendationsRequest request = new RecommendationsRequest.Builder(limit)
                .categoryId("foo")
                .build();
        String actualUrlStr = RecommendationsRequest.toUrlString(BVSDK.getInstance(), adId, request);
        checkUrlStr(actualUrlStr, request);
    }

    private void checkUrlStr(String actualUrlStr, RecommendationsRequest request) {
        assertEquals(expectedRequestUrl(
                shopperMarketingApiBaseUrl,
                adId,
                bvUserProvidedData.getBvConfig().getApiKeyShopperAdvertising(),
                limit,
                bvUserProvidedData.getBvConfig().getClientId(),
                request.getProductId(),
                request.getCategoryId(),
                request.getPageType()), actualUrlStr);
    }

    @Test
    public void canFormRequestWithOnlyLimit() {
        RecommendationsRequest request = new RecommendationsRequest.Builder(limit).build();
        String actualUrlStr = RecommendationsRequest.toUrlString(BVSDK.getInstance(), adId, request);
        checkUrlStr(actualUrlStr, request);
    }

    @Test
    public void canFormRequestWithAllParams() throws ParseException {
        //todo add test for lookback
        List<String> interests = new ArrayList<>();
        interests.add("interest1");
        interests.add("interest2");
        RecommendationsRequest request = new RecommendationsRequest.Builder(limit)
                .pageType(PageType.PRODUCT)
                .requiredCategory("cat123")
                .minAvgRating(2.3)
                .productId("testid")
                .interests(interests)
                .build();
        String actualUrlString = RecommendationsRequest.toUrlString(BVSDK.getInstance(), adId, request);
        assertEquals("https://bazaarvoiceapibaseurl.com/" +
                        "recommendations/" +
                        "magpie_idfa_testAdId?" +
                        "passKey=apiKeyShopperAd&" +
                        "client=pretendcompany&" +
                        "min_avg_rating=2.3&" +
                        "limit=13&" +
                        "pageType=PRODUCT&" +
                        "required_category=cat123&" +
                        "interests=interest1,interest2&" +
                        "product=pretendcompany/testid",
                actualUrlString);
    }

    private String expectedRequestUrl(String baseUrl, String adId, String shopperAdvertisingApiKey, int limit, String clientId, String productId, String categoryId, PageType pageType) {
        String requestUrl = baseUrl + "recommendations/magpie_idfa_" + adId + "?passKey=" + shopperAdvertisingApiKey;
        requestUrl += "&client=" + clientId;
        requestUrl += "&min_avg_rating=0.0";
        requestUrl += "&limit=" + limit;

        if (productId != null && !productId.isEmpty()) {
            requestUrl += "&product=" + clientId + "/" + productId;
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            requestUrl += "&category=" + clientId + "/" + categoryId;
        }

        if (pageType != null) {
            requestUrl += "&pageType=" + pageType.toString();
        }
        return requestUrl;
    }

    @Test(expected = IllegalArgumentException.class)
    public void limitTooSmall() {
        new RecommendationsRequest.Builder(limitTooSmall).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void limitTooBig() {
        new RecommendationsRequest.Builder(limitTooBig).build();
    }

    @Test
    public void productIdAndCategoryIdMutuallyExclusive() {
        try {
            new RecommendationsRequest.Builder(limit)
                    .productId("prod123")
                    .categoryId("cat123");
            fail();
        } catch (IllegalArgumentException expected) { /** expected **/}

        try {
            new RecommendationsRequest.Builder(limit)
                    .categoryId("cat123")
                    .productId("prod123");
            fail();
        } catch (IllegalArgumentException expected) { /** expected **/}
    }

}