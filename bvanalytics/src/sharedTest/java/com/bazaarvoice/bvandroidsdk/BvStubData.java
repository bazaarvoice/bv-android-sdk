package com.bazaarvoice.bvandroidsdk;

import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.mockwebserver.MockResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BvStubData {

  public String getTransactionItemName() {
    return "name123";
  }

  public String getImageUrl() {
    return "https://www.example.com/image.png";
  }

  public double getSmallPrice() {
    return 3.50;
  }

  public int getTransactionQuantity() {
    return 1;
  }

  public String getSku() {
    return "sku123";
  }

  public BVTransactionItem getTransactionItem(String sku, String name, String imageUrl, double price, int quantity) {
    return new BVTransactionItem.Builder(sku)
        .setName(name)
        .setImageUrl(imageUrl)
        .setCategory(getCategoryId())
        .setPrice(price)
        .setQuantity(quantity)
        .build();
  }

  public List<BVTransactionItem> getTransactionItems(int num) {
    final List<BVTransactionItem> items = new ArrayList<>();
    for (int i=0; i<num; i++) {
      items.add(getTransactionItem(
          getSku(),
          getTransactionItemName(),
          getImageUrl(),
          getSmallPrice(),
          getTransactionQuantity()));
    }
    return items;
  }

  public String getPiiKey() {
    return "foo";
  }

  public String getNonPiiKey() {
    return "partnerSource";
  }

  public Map<String, Object> getPiiParams() {
    Map<String, Object> otherParams = new HashMap<>();
    // PII
    otherParams.put("email", "foo@bar.com");
    otherParams.put("name", "Jane Doe");
    return otherParams;
  }

  public Map<String, Object> getNonPiiConversionParams() {
    Map<String, Object> otherParams = new HashMap<>();
    // non-PII
    otherParams.put("city", "Austin");
    otherParams.put("state", "TX");
    return otherParams;
  }

  public Map<String, String> getOtherParams(int num) {
    if (num < 2) { throw new IllegalStateException("need more"); }
    Map<String, String> otherParams = new HashMap<>();
    for (int i=0; i<num; i++) {
      otherParams.put("foo" + i, "bar");
    }
    return otherParams;
  }

  public Map<String, Object> getBothPiiAndNonPiiAdditionalParams() {
    Map<String, Object> otherParams = new HashMap<>();
    otherParams.putAll(getPiiParams());
    otherParams.putAll(getNonPiiConversionParams());
    return otherParams;
  }

  public Map<String, String> getOnlyNonPiiParams(int num) {
    Map<String, String> otherParams = new HashMap<>();
    List<String> nonPiiParams = BVAnalyticsUtils.NON_PII_PARAMS;
    for (int i=0; i<num && i<nonPiiParams.size(); i++) {
      otherParams.put(nonPiiParams.get(i), "bar");
    }
    return otherParams;
  }

  public BVTransaction getTransaction() {
    return getTransaction(getTransactionItems(8), getOtherParams(8));
  }

  public BVTransaction getTransaction(List<BVTransactionItem> items, Map<String, String> otherParams) {
    return new BVTransaction.Builder()
        .setOrderId("order123")
        .setCity("Austin")
        .setState("TX")
        .setCountry("USA")
        .setCurrency("USD")
        .setItems(items)
        .setOtherParams(otherParams)
        .setShipping(3.50)
        .setTax(3.50)
        .setTotal(10.50)
        .build();
  }

  public BVTransactionEvent getTransactionEvent() {
    return getTransactionEvent(getTransactionItems(8), getOtherParams(8));
  }

  public BVTransactionEvent getTransactionEvent(List<BVTransactionItem> items, Map<String, String> otherParams) {
    BVTransactionEvent event = new BVTransactionEvent(
        getTransaction(items, otherParams));
    event.setBvMobileParams(getBvMobileParams());
    return event;
  }

  public String getHashedIp() {
    return "testHashedIp";
  }

  public String getUserAgent() {
    return "testUA";
  }

  public String getProductId() {
    return "prod123";
  }

  public String getCategoryId() {
    return "category123";
  }

  public BVEventValues.BVProductType getBvProductTypeReviews() {
    return BVEventValues.BVProductType.CONVERSATIONS_REVIEWS;
  }

  public Map<String, Object> getEmptyAdditionalParams() {
    return new HashMap<>();
  }

  public Map<String, Object> getAdditionalParams() throws JSONException {
    Map<String, Object> map = new HashMap<>();
    map.put("foo1", "foo");
    map.put("foo2", 1);
    map.put("foo3", 3.50);
    map.put("foo4", 12L);
    map.put("foo5", false);
    map.put("foo6", 'b');
    return map;
  }

  public BVAnalyticsEvent getSomeEvent(Map<String, Object> additionalParams) throws JSONException {
    BVFeatureUsedEvent bvFeatureUsedEvent = getWriteReviewFeatureUsedSchema(
        getProductId(),
        additionalParams);
    bvFeatureUsedEvent.setBvMobileParams(getBvMobileParams());
    return bvFeatureUsedEvent;
  }

  public BVEventValues.BVEventSource getBvEventSource() {
    return BVEventValues.BVEventSource.NATIVE_MOBILE_SDK;
  }

  public BVMobileParams getBvMobileParams() {
    BVMobileParams bvMobileParams = mock(BVMobileParams.class);
    BVCommonAnalyticsParams bvCommonAnalyticsParams = getBvCommonAnalyticsParams();
    when(bvMobileParams.getBvCommonAnalyticsParams()).thenReturn(bvCommonAnalyticsParams);
    BVAdvertisingId bvAdvertisingId = getBvAdvertisingId();
    when(bvMobileParams.getBvAdvertisingId()).thenReturn(bvAdvertisingId);
    BVMobileInfo bvMobileInfo = getBvMobileInfo();
    when(bvMobileParams.getMobileInfo()).thenReturn(bvMobileInfo);
    when(bvMobileParams.getSource()).thenReturn(getBvEventSource());
    return bvMobileParams;
  }

  public BVMobileInfo getBvMobileInfo() {
    BVMobileInfo bvMobileInfo = mock(BVMobileInfo.class);
    when(bvMobileInfo.getBvSdkVersion()).thenReturn(getBvSdkVersion());
    when(bvMobileInfo.getMobileOsVersion()).thenReturn(getMobileOsVersion());
    when(bvMobileInfo.getMobileOs()).thenReturn(getMobileOs());
    when(bvMobileInfo.getMobileDeviceName()).thenReturn(getMobileDeviceName());
    when(bvMobileInfo.getMobileAppVersion()).thenReturn(getMobileAppVersion());
    when(bvMobileInfo.getMobileAppIdentifier()).thenReturn(getMobileAppIdentifier());
    when(bvMobileInfo.getMobileAppCode()).thenReturn(getMobileAppCode());
    return bvMobileInfo;
  }

  public String getBvSdkVersion() {
    return "testdkversion2";
  }

  public String getMobileOsVersion() {
    return "android_version_123";
  }

  public String getMobileOs() {
    return "Android";
  }

  public String getMobileDeviceName() {
    return "googley_phone_9000";
  }

  public String getMobileAppVersion() {
    return "1.2.3";
  }

  public String getMobileAppIdentifier() {
    return "com.example.myapp";
  }

  public String getMobileAppCode() {
    return "9001";
  }

  public BVAdvertisingId getBvAdvertisingId() {
    BVAdvertisingId bvAdvertisingId = mock(BVAdvertisingId.class);
    when(bvAdvertisingId.getAdvertisingId()).thenReturn(getAdId());
    return bvAdvertisingId;
  }

  public String getAdId() {
    return BaseShadows.TEST_AD_ID;
  }

  public BVCommonAnalyticsParams getBvCommonAnalyticsParams() {
    BVCommonAnalyticsParams bvCommonAnalyticsParams = mock(BVCommonAnalyticsParams.class);
    when(bvCommonAnalyticsParams.getHashedIp()).thenReturn(getHashedIp());
    when(bvCommonAnalyticsParams.getUserAgent()).thenReturn(getUserAgent());
    return bvCommonAnalyticsParams;
  }

  public String getBrand() {
    return "someBrand";
  }

  public BVFeatureUsedEvent getWriteReviewFeatureUsedSchema(String productId, Map<String, Object> additionalParams) {
    BVFeatureUsedEvent event = new BVFeatureUsedEvent(
        productId,
        BVEventValues.BVProductType.CONVERSATIONS_REVIEWS,
        BVEventValues.BVFeatureUsedEventType.WRITE_REVIEW,
        getBrand());
    event.setAdditionalParams(additionalParams);
    event.setBvMobileParams(getBvMobileParams());
    return event;
  }

  public BVPageViewEvent getReviewsProductPageViewSchema() throws Exception {
    BVPageViewEvent event = new BVPageViewEvent(getProductId(), getBvProductTypeReviews(), getCategoryId());
    event.setAdditionalParams(getAdditionalParams());
    event.setBvMobileParams(getBvMobileParams());
    return event;
  }

  public BVLocationEvent getLocationEventStub(String transition, String locationId, Long duration) {
    return new BVLocationEvent(transition, locationId, duration);
  }

  public BVAnalyticsEvent getEvent() {
    return getPageViewEvent();
  }

  public BVMobileAnalyticsEvent getMobileAnalyticsEvent() {
    return getPageViewEvent();
  }

  public BVPageViewEvent getPageViewEvent() {
    BVPageViewEvent event = new BVPageViewEvent(getProductId(), getBvProductTypeReviews(), getCategoryId());
    event.setBvMobileParams(getBvMobileParams());
    return event;
  }

  public String getUserAuthString() {
    return "newuserauthstr";
  }

  public BVPersonalizationEvent getPersonalizationEvent() {
    BVPersonalizationEvent bvPersonalizationEvent = new BVPersonalizationEvent(getUserAuthString());
    bvPersonalizationEvent.setBvMobileParams(getBvMobileParams());
    return bvPersonalizationEvent;
  }

  public AdvertisingIdClient.Info getAdInfo(boolean limitEnabled) {
    return new AdvertisingIdClient.Info(BaseShadows.TEST_AD_ID, limitEnabled);
  }

  public Future<String> getAdIdFuture(Context context) {
    ExecutorService testExecutor = Executors.newSingleThreadExecutor();
    return testExecutor.submit(new TestAdIdCallable(context));
  }

  public String getBvConversionType() {
    return "conversionType";
  }

  public String getBvConversionValue() {
    return "conversionValue";
  }

  public String getBvConversionLabel() {
    return "conversionLabel";
  }

  public JsonObject getBvConversionOtherParams() {
    return new JsonObject();
  }

  public BVConversionEvent getConversionEvent() {
    BVConversionEvent event = new BVConversionEvent(
        getBvConversionType(), getBvConversionValue(), getBvConversionLabel());
    event.setBvMobileParams(getBvMobileParams());
    return event;
  }

  public String getRootAnalyticsUrl() {
    return "/foo/";
  }

  public MockResponse getEmptySuccessResponse() {
    return new MockResponse()
        .setResponseCode(200)
        .setBody("");
  }

  public String getClientId() {
    return "testClientId";
  }

  public String getRootCategoryId() {
    return "rootCategory123";
  }

  public BVViewedCgcEvent getViewedCgcEvent() {
    BVViewedCgcEvent event = new BVViewedCgcEvent(
        getProductId(),
        BVEventValues.BVProductType.CONVERSATIONS_REVIEWS,
        getRootCategoryId(),
        getCategoryId(),
        getBrand());
    event.setBvMobileParams(getBvMobileParams());
    return event;
  }

  private static class TestAdIdCallable implements Callable<String> {
    private final Context context;

    TestAdIdCallable(Context context) {
      this.context = context;
    }

    @Override
    public String call() throws Exception {
      BVAdvertisingId bvAdId = new BVAdvertisingId(context);
      return bvAdId.getAdvertisingId();
    }
  }
}
