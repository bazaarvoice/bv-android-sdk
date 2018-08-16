package com.bazaarvoice.bvandroidsdk;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;

import okhttp3.RequestBody;

import static com.bazaarvoice.bvandroidsdk.BvTestUtil.checkJsonContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BvAnalyticsBatchTest {
  @Mock
  BVAdvertisingId bvAdvertisingId;
  @Mock
  BVCommonAnalyticsParams bvCommonAnalyticsParams;
  @Mock
  BVMobileParams bvMobileParams;
  private BvStubData stubData;

  @Before
  public void setup() {
    initMocks(this);
    stubData = new BvStubData();
    when(bvMobileParams.getBvAdvertisingId()).thenReturn(bvAdvertisingId);
    when(bvMobileParams.getBvCommonAnalyticsParams()).thenReturn(bvCommonAnalyticsParams);
    when(bvAdvertisingId.getAdvertisingId()).thenReturn("testAdId");
    when(bvCommonAnalyticsParams.getHashedIp()).thenReturn("testHashedIp");
    when(bvCommonAnalyticsParams.getUserAgent()).thenReturn("testUA");
  }

  @Test
  public void shouldReturnEmpty() throws Exception {
    BVPixelDispatcher.BvAnalyticsBatch batch = new BVPixelDispatcher.BvAnalyticsBatch();
    assertTrue(batch.isEmpty());
  }

  @Test
  public void shouldReturnCorrectSize() throws Exception {
    BVPixelDispatcher.BvAnalyticsBatch batch = new BVPixelDispatcher.BvAnalyticsBatch();
    BVAnalyticsEvent event = mock(BVAnalyticsEvent.class);
    when(event.toRaw()).thenReturn(new HashMap<String, Object>());

    batch.putEvent(event);
    assertEquals(1, batch.size());

    batch.putEvent(event);
    assertEquals(2, batch.size());
  }

  @Test
  public void shouldClear() throws Exception {
    BVPixelDispatcher.BvAnalyticsBatch batch = new BVPixelDispatcher.BvAnalyticsBatch();
    BVAnalyticsEvent event = mock(BVAnalyticsEvent.class);
    when(event.toRaw()).thenReturn(new HashMap<String, Object>());

    batch.putEvent(event);
    batch.clear();

    assertEquals(0, batch.size());
  }


  @Test
  public void getEventBatchString() throws Exception {
    BVPixelDispatcher.BvAnalyticsBatch batch = new BVPixelDispatcher.BvAnalyticsBatch();
    BVPageViewEvent pageViewEvent = stubData.getPageViewEvent();
    batch.putEvent(pageViewEvent);

    RequestBody requestBody = batch.toPostPayload();
    assertNotNull(requestBody);

    String eventBatchJsonStr = batch.getEventBatchJsonString();
    JsonObject jsonObject = new JsonParser().parse(eventBatchJsonStr)
        .getAsJsonObject();
    JsonArray batchArray = jsonObject.getAsJsonArray("batch");
    JsonObject eventJsonObj = batchArray.get(0).getAsJsonObject();
    checkJsonContains(eventJsonObj, "cl", "PageView");
    checkJsonContains(eventJsonObj, "type", "Embedded");
  }

  @Test
  public void getEventBatchStringShouldBeUpdatedAfterClearing() throws Exception {
    BVPixelDispatcher.BvAnalyticsBatch batch = new BVPixelDispatcher.BvAnalyticsBatch();
    BVPageViewEvent pageViewEvent = stubData.getPageViewEvent();
    batch.putEvent(pageViewEvent);

    batch.clear();

    BVPersonalizationEvent bvPersonalizationEvent = stubData.getPersonalizationEvent();
    batch.putEvent(bvPersonalizationEvent);

    String eventBatchJsonStr = batch.getEventBatchJsonString();
    JsonObject jsonObject = new JsonParser().parse(eventBatchJsonStr)
        .getAsJsonObject();
    JsonArray batchArray = jsonObject.getAsJsonArray("batch");
    JsonObject eventJsonObj = batchArray.get(0).getAsJsonObject();
    checkJsonContains(eventJsonObj, "cl", "Personalization");
    checkJsonContains(eventJsonObj, "type", "ProfileMobile");
  }
}
