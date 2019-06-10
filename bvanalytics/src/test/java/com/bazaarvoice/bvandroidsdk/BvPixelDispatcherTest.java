package com.bazaarvoice.bvandroidsdk;

import android.os.HandlerThread;

import androidx.test.core.app.ApplicationProvider;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 23, shadows = {BaseShadows.ShadowAdIdClientNoLimit.class})
public class BvPixelDispatcherTest {
  HandlerThread bgHandlerThread;
  BVPixelDispatcher.BvAnalyticsBatch analyticsBatch;
  BvStubData stubData;
  OkHttpClient okHttpClient;
  long delayMillis = 1;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    bgHandlerThread = new BaseTestUtils.TestHandlerThread();
    bgHandlerThread.start();
    okHttpClient = new OkHttpClient();
    analyticsBatch = new BVPixelDispatcher.BvAnalyticsBatch();
    stubData = new BvStubData();
  }

  @Test
  public void shouldQueueUpEvent() throws Exception {
    // arrange
    MockWebServer server = new MockWebServer();
    server.enqueue(stubData.getEmptySuccessResponse());
    server.start();
    HttpUrl baseUrl = server.url(stubData.getRootAnalyticsUrl());
    BVPixelDispatcher subject = createBvPixelDispatcher(baseUrl.toString());

    subject.beginDispatchWithDelay();
    subject.enqueueEvent(stubData.getPageViewEvent(), stubData.getClientId());
    assertEquals(1, analyticsBatch.size());

    server.shutdown();
  }

  @Test
  public void shouldHaveEmptyQueueAfterSending() throws Exception {
    // arrange
    MockWebServer server = new MockWebServer();
    server.enqueue(stubData.getEmptySuccessResponse());
    server.start();
    HttpUrl baseUrl = server.url(stubData.getRootAnalyticsUrl());
    BVPixelDispatcher subject = createBvPixelDispatcher(baseUrl.toString());

    subject.beginDispatchWithDelay();
    subject.enqueueEvent(stubData.getPageViewEvent(), stubData.getClientId());
    ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

    // assert
    RecordedRequest request = server.takeRequest(100, TimeUnit.MILLISECONDS);
    assertEquals(0, analyticsBatch.size());

    server.shutdown();
  }

  @Test
  public void shouldHaveBatchJsonForRequest() throws Exception {
    // arrange
    MockWebServer server = new MockWebServer();
    server.enqueue(stubData.getEmptySuccessResponse());
    server.start();
    HttpUrl baseUrl = server.url(stubData.getRootAnalyticsUrl());
    BVPixelDispatcher subject = createBvPixelDispatcher(baseUrl.toString());

    subject.beginDispatchWithDelay();
    subject.enqueueEvent(stubData.getPageViewEvent(), stubData.getClientId());
    ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

    // assert
    RecordedRequest request = server.takeRequest(100, TimeUnit.MILLISECONDS);
    String requestJsonStr = request.getBody().readUtf8();
    JsonObject requestJson = new JsonParser().parse(requestJsonStr).getAsJsonObject();
    assertTrue(requestJson.has("batch"));

    server.shutdown();
  }

  @Test
  public void shouldFormCorrectAbsoluteUrlForRequest() throws Exception {
    // arrange
    MockWebServer server = new MockWebServer();
    server.enqueue(stubData.getEmptySuccessResponse());
    server.start();
    HttpUrl baseUrl = server.url(stubData.getRootAnalyticsUrl());
    BVPixelDispatcher subject = createBvPixelDispatcher(baseUrl.toString());

    subject.beginDispatchWithDelay();
    subject.enqueueEvent(stubData.getPageViewEvent(), stubData.getClientId());
    ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

    // assert
    RecordedRequest request = server.takeRequest(100, TimeUnit.MILLISECONDS);
    assertEquals(stubData.getRootAnalyticsUrl() + "event", request.getPath());

    server.shutdown();
  }

  private BVPixelDispatcher createBvPixelDispatcher(String baseUrl) {
    return new BVPixelDispatcher(ApplicationProvider.getApplicationContext(), bgHandlerThread, analyticsBatch, okHttpClient, baseUrl, delayMillis, false);
  }
}
