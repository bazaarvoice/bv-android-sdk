package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

class BVPixelDispatcher {
  // region Properties
  private static final String TAG = "BVPixelDispatcher";
  private static final boolean FULL_LOGGING = false;
  private static final String ANALYTICS_THREAD_NAME = TAG;
  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private static final int ENQUEUE_EVENT = 0;
  private static final int DISPATCH_EVENTS = 1;
  private static final int DISPATCH_EVENTS_WITH_DELAY = 2;
  private static final String PATH = "event";

  private final Handler bgHandler;
  private final BvAnalyticsBatch analyticsBatch;
  private final OkHttpClient okHttpClient;
  private final HttpUrl url;
  private final long analyticsDelayMillis;
  private final boolean dryRunAnalytics;

  // endregion

  // region Constructor
  /**
   * @param context Context used to get advertisingId, and UUID
   * @param bgHandlerThread Thread that will manage batching/sending events
   * @param analyticsBatch Maps {@link BVAnalyticsEvent}s to a {@link RequestBody}
   * @param okHttpClient {@link OkHttpClient} instance
   * @param analyticsRootUrl Root endpoint to send events to
   * @param analyticsDelayMillis Time between sending batches
   */
  BVPixelDispatcher(
      Context context,
      HandlerThread bgHandlerThread,
      BvAnalyticsBatch analyticsBatch,
      OkHttpClient okHttpClient,
      String analyticsRootUrl,
      long analyticsDelayMillis,
      boolean dryRunAnalytics) {
    if (!bgHandlerThread.isAlive()) {
      throw new IllegalStateException("Must start bgHandlerThread before building BVPixel");
    }
    this.bgHandler = new BvAnalyticsHandler(bgHandlerThread.getLooper(), this, context.getApplicationContext());
    this.analyticsBatch = analyticsBatch;
    this.okHttpClient = okHttpClient;
    this.url = HttpUrl.parse(analyticsRootUrl)
        .newBuilder()
        .addPathSegment(PATH).build();
    this.analyticsDelayMillis = analyticsDelayMillis;
    this.dryRunAnalytics = dryRunAnalytics;
  }
  // endregion

  // region Helper Classes

  static class BvAnalyticsThread extends HandlerThread {
    BvAnalyticsThread() {
      super(BVAnalyticsUtils.THREAD_PREFIX + ANALYTICS_THREAD_NAME, THREAD_PRIORITY_BACKGROUND);
    }
  }

  private static class BvEnqueueEventPayload {
    private final BVAnalyticsEvent event;
    private final String clientId;

    BvEnqueueEventPayload(BVAnalyticsEvent event, String clientId) {
      this.event = event;
      this.clientId = clientId;
    }

    public BVAnalyticsEvent getEvent() {
      return event;
    }

    public String getClientId() {
      return clientId;
    }
  }

  static class BvAnalyticsHandler extends Handler {
    private final BVPixelDispatcher bvPixelDispatcher;
    private final Context appContext;
    private BVMobileParams bvMobileParams;

    BvAnalyticsHandler(Looper looper, BVPixelDispatcher bvPixelDispatcher, Context appContext) {
      super(looper);
      this.bvPixelDispatcher = bvPixelDispatcher;
      this.appContext = appContext;
    }

    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case ENQUEUE_EVENT: {
          BvEnqueueEventPayload payload = (BvEnqueueEventPayload) msg.obj;
          BVAnalyticsEvent event = payload.getEvent();
          if (event instanceof BVMobileAnalyticsEvent) {
            if (bvMobileParams == null) {
              bvMobileParams = new BVMobileParams(appContext, payload.getClientId(), BVEventValues.BVEventSource.NATIVE_MOBILE_SDK);
            }
            BVMobileAnalyticsEvent bvMobileEvent = (BVMobileAnalyticsEvent) event;
            bvMobileEvent.setBvMobileParams(bvMobileParams);
          }
          bvPixelDispatcher.addEventToBatch(event);
          break;
        }
        case DISPATCH_EVENTS: {
          bvPixelDispatcher.sendAnalytics();
          break;
        }
        case DISPATCH_EVENTS_WITH_DELAY: {
          bvPixelDispatcher.sendAnalytics();
          bvPixelDispatcher.sendBatchWithDelay();
          break;
        }
      }
    }
  }

  static class BvAnalyticsBatch {
    private static final String TAG = "BVPixelVerify";
    private final Map<String, Object> eventBatch;
    private List<Map<String, Object>> eventArray;

    public BvAnalyticsBatch() {
      eventBatch = new HashMap<>();
      eventArray = new ArrayList<>();
      eventBatch.put(BVEventKeys.BATCH, eventArray);
      eventBatch.put(BVEventKeys.CommonAnalyticsParams.USER_AGENT, BVEventValues.BVSDK_USER_AGENT);
    }

    public void putEvent(BVAnalyticsEvent event) {
      eventArray.add(event.toRaw());
    }

    public void clear() {
      eventArray.clear();
    }

    public boolean isEmpty() {
      return eventArray.size() == 0;
    }

    public int size() {
      return eventArray.size();
    }

    public String getEventBatchJsonString() {
      return new JSONObject(eventBatch).toString();
    }

    public RequestBody toPostPayload() {
      return RequestBody.create(JSON, getEventBatchJsonString());
    }

    void log(boolean full) {
      Iterator<Map<String, Object>> iterator = eventArray.iterator();
      while (iterator.hasNext()) {
        Map<String, Object> eventJson = iterator.next();
        StringBuilder eventLog = new StringBuilder();
        if (full) {
          for (Map.Entry<String, Object> entry : eventJson.entrySet()) {
            eventLog.append("\t")
                .append(entry.getKey())
                .append(" : ")
                .append(entry.getValue())
                .append("\n");
          }
        } else {
          String basicEventLogTemplate = "type: %1$s, class: %2$s, source: %3$s";
          String basicEventLog = String.format(basicEventLogTemplate,
              eventJson.get("type"), eventJson.get("cl"), eventJson.get("source"));
          eventLog.append(basicEventLog);
          eventLog.append(getSmartExtraInfo(eventJson));
        }
        Log.d(TAG, eventLog.toString());
      }
    }

    private String getSmartExtraInfo(Map<String, Object> eventJson) {
      StringBuilder extraInfo = new StringBuilder();

      if (eventJson.containsKey("name")) {
        extraInfo.append(" - name:")
            .append(eventJson.get("name"));
      }
      if (eventJson.containsKey("transition")) {
        extraInfo.append(" - transition:")
            .append(eventJson.get("transition"));
      }
      if (eventJson.containsKey("durationSecs")) {
        extraInfo.append(" - durationSecs:")
            .append(eventJson.get("durationSecs"));
      }
      if (eventJson.containsKey("locationId")) {
        extraInfo.append(" - locationId:")
            .append(eventJson.get("locationId"));
      }
      if (eventJson.containsKey("appState")) {
        extraInfo.append(" - appState:")
            .append(eventJson.get("appState"));
      }
      if (eventJson.containsKey("bvProduct")) {
        extraInfo.append(" - bvProduct:")
            .append(eventJson.get("bvProduct"));
      }

      return extraInfo.toString();
    }
  }

  // endregion

  // region Helper Methods

  /**
   * Actual sending off of the events
   */
  private void sendAnalytics() {
    Response response = null;
    try {
      if (analyticsBatch.isEmpty()) {
        return;
      }

      analyticsBatch.log(FULL_LOGGING);

      if (dryRunAnalytics) {
        analyticsBatch.clear();
        Log.d("Analytics", "Not sending analytics for dry run");
        return;
      }

      RequestBody body = analyticsBatch.toPostPayload();
      Log.v(TAG, url.toString() + "\n" + analyticsBatch.toString());
      Request request = new Request.Builder()
          .url(url)
          .header("Content-Type", "application/json")
          .header("X-Requested-With", "XMLHttpRequest")
          .header("User-Agent", BVEventValues.BVSDK_USER_AGENT)
          .post(body)
          .build();

      response = okHttpClient.newCall(request).execute();

      if (response.isSuccessful()) {
        Log.d("Analytics", "Successfully posted " + analyticsBatch.size() + " events");
      } else {
        Log.d("Analytics", "Unsuccessfully posted Events: " + response.code() + ", message: " + response.message());
      }
    } catch (IOException e) {
      Log.e(TAG, "Failed to send analytics event", e);
    } finally {
      analyticsBatch.clear();
      if (response != null && response.body() != null) {
        response.body().close();
      }
    }
  }

  /**
   * Adds event to batch
   *
   * @param event Analytics Event object
   */
  private <EventType extends BVAnalyticsEvent> void addEventToBatch(EventType event) {
    analyticsBatch.putEvent(event);
  }

  /**
   * After the configured {@link #analyticsDelayMillis} time
   * send off all queued events
   */
  private void sendBatchWithDelay() {
    bgHandler.sendMessageDelayed(
        bgHandler.obtainMessage(DISPATCH_EVENTS_WITH_DELAY),
        analyticsDelayMillis);
  }

  // endregion

  // region API

  /**
   * API for {@link BVPixel} to enqueue an event
   * @param event
   */
  void enqueueEvent(BVAnalyticsEvent event, String clientId) {
    final BvEnqueueEventPayload payload = new BvEnqueueEventPayload(event, clientId);
    bgHandler.sendMessage(bgHandler.obtainMessage(ENQUEUE_EVENT, payload));
  }

  /**
   * API for {@link BVPixel} to immediately send off
   * all queued events
   */
  void dispatchBatchImmediately() {
    bgHandler.sendMessage(bgHandler.obtainMessage(DISPATCH_EVENTS));
  }

  /**
   * API for {@link BVPixel} to schedule sending off
   * the events
   */
  void beginDispatchWithDelay() {
    sendBatchWithDelay();
  }

  // endregion

}
