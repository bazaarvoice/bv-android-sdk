package com.bazaarvoice.bvandroidsdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class ProductSentimentsBasicRequestFactoryTest {
  private ProductSentimentRequestFactory productSentimentRequestFactory;

  public static ProductSentimentsBasicRequestFactory createTestRequestFactory() {
    final BVMobileInfo bvMobileInfo = mock(BVMobileInfo.class);
    when(bvMobileInfo.getBvSdkVersion()).thenReturn("bvsdk7");
    when(bvMobileInfo.getMobileAppCode()).thenReturn("1");
    when(bvMobileInfo.getMobileAppIdentifier()).thenReturn("3.2.1");
    when(bvMobileInfo.getMobileAppVersion()).thenReturn("21");
    when(bvMobileInfo.getMobileDeviceName()).thenReturn("test_device");
    when(bvMobileInfo.getMobileOs()).thenReturn("Android");
    when(bvMobileInfo.getMobileOsVersion()).thenReturn("0.1.2");
    final String rootApiUrl = "https://api.bazaarvoice.com/";
    final BVRootApiUrls bvRootApiUrls = mock(BVRootApiUrls.class);
    when(bvRootApiUrls.getBazaarvoiceApiRootUrl()).thenReturn(rootApiUrl);
    final String convApiKey = "convApiKeyFoo";
    final String storeApiKey = "storeApiKeyBar";
    final String progressiveSubmissionApiKey = "progressiveSubApiKey";
    final String bvSdkUserAgent = "Android-TestDk";
    final BVSDK bvsdk = mock(BVSDK.class);
    final BVConfig bvConfig = new BVConfig.Builder()
            .apiKeyConversations(convApiKey)
            .apiKeyConversationsStores(storeApiKey)
            .apiKeyProgressiveSubmission(progressiveSubmissionApiKey)
            .clientId("yoyomakers")
            .build();
    final ProductSentimentsFingerprintProvider productSentimentsFingerprintProvider = new ProductSentimentsFingerprintProvider() {
      @Override
      public String getFingerprint() {
        return "test_fp";
      }
    };
    return new ProductSentimentsBasicRequestFactory(bvMobileInfo, bvRootApiUrls, bvConfig, bvSdkUserAgent, productSentimentsFingerprintProvider);
  }
  @Before
  public void setUp() throws Exception {
    this.productSentimentRequestFactory = createTestRequestFactory();
  }


  @Test
  public void createSummarisedFeaturesRequest() {
    final SummarisedFeaturesRequest request = new SummarisedFeaturesRequest.Builder("prod1")
            .addLanguage("En_US")
            .addEmbed("quotes")
            .build();
    final Request okRequest = productSentimentRequestFactory.create(request);
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("productId").contains("prod1"));
    assertTrue(url.queryParameterValues("language").contains("En_US"));
    assertTrue(url.queryParameterValues("embed").contains("quotes"));
  }
  @Test
  public void createSummarisedFeaturesQuotesRequest() {
    final SummarisedFeaturesQuotesRequest request = new SummarisedFeaturesQuotesRequest.Builder("prod1","11108")
            .addLanguage("En_US")
            .addLimit("10")
            .build();
    final Request okRequest = productSentimentRequestFactory.create(request);
    final HttpUrl url = okRequest.url();
    assertTrue(url.pathSegments().contains("11108"));
    assertTrue(url.queryParameterValues("productId").contains("prod1"));
    assertTrue(url.queryParameterValues("language").contains("En_US"));
    assertTrue(url.queryParameterValues("Limit").contains("10"));
  }
  @Test
  public void createFeaturesSentimentRequest() {
    final FeaturesSentimentRequest request = new FeaturesSentimentRequest.Builder("prod1")
            .addLanguage("En_US")
            .addLimit("10")
            .build();
    final Request okRequest = productSentimentRequestFactory.create(request);
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("productId").contains("prod1"));
    assertTrue(url.queryParameterValues("language").contains("En_US"));
    assertTrue(url.queryParameterValues("Limit").contains("10"));
  }
  @Test
  public void createQuotesSentimentRequest() {
    final QuotesSentimentRequest request = new QuotesSentimentRequest.Builder("prod1")
            .addLanguage("En_US")
            .addLimit("10")
            .build();
    final Request okRequest = productSentimentRequestFactory.create(request);
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("productId").contains("prod1"));
    assertTrue(url.queryParameterValues("language").contains("En_US"));
    assertTrue(url.queryParameterValues("Limit").contains("10"));
  }
  @Test
  public void createExpressionsSentimentRequest() {
    final ExpressionsSentimentRequest request = new ExpressionsSentimentRequest.Builder("prod1","feature")
            .addLimit("10")
            .build();
    final Request okRequest = productSentimentRequestFactory.create(request);
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("productId").contains("prod1"));
    assertTrue(url.queryParameterValues("feature").contains("feature"));
    assertTrue(url.queryParameterValues("Limit").contains("10"));
  }

}