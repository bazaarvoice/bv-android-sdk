package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.warnShouldNotBeEmpty;

public class BVPixel {
  // region Properties

  private static BVPixel singleton;

  private static final String ANALYTICS_ROOT_URL_PRODUCTION = "https://network.bazaarvoice.com/";
  private static final String ANALYTICS_ROOT_URL_STAGING = "https://network-stg.bazaarvoice.com/";
  private static final int ANALYTICS_DELAY_SECONDS = 10;
  private final BVPixelDispatcher bvPixelDispatcher;
  private final String defaultClientId;

  // endregion

  // region Constructor

  BVPixel(BVPixelDispatcher bvPixelDispatcher, String defaultClientId) {
    this.defaultClientId = defaultClientId;
    this.bvPixelDispatcher = bvPixelDispatcher;
    // Start the polling dispatch
    this.bvPixelDispatcher.beginDispatchWithDelay();
  }

  // endregion

  // region Public API

  public static Builder builder(Context context, String clientId, boolean isStaging) {
    return new BVPixel.Builder(context, clientId, isStaging);
  }

  /**
   * @return Singleton instance with settings provided in the {@link Builder}
   */
  public static BVPixel getInstance() {
    confirmBvPixelCreated();
    return singleton;
  }

  /**
   * Adds the event to a batch, that will be later dispatched
   *
   * @param event Event instance
   * @param <EventType> Type of event to send
   */
  public <EventType extends BVAnalyticsEvent> void track(EventType event) {
    trackEventForClient(event, defaultClientId);
  }

  public <EventType extends BVAnalyticsEvent> void trackEventForClient(EventType event, String clientId) {
    bvPixelDispatcher.enqueueEvent(event, clientId);

    if (shouldDispatchImmediately(event)) {
      bvPixelDispatcher.dispatchBatchImmediately();
    }
  }

  public static class Builder {
    private final Context appContext;
    private final String clientId;
    private final BVPixelDispatcher.BvAnalyticsBatch bvAnalyticsBatch;
    private final String analyticsRootUrl;
    private HandlerThread bgHandlerThread;
    private OkHttpClient okHttpClient;
    private boolean dryRunAnalytics;

    public Builder(Context context, String clientId, boolean isStaging) {
      this.appContext = context.getApplicationContext();
      this.clientId = clientId;
      this.bvAnalyticsBatch = new BVPixelDispatcher.BvAnalyticsBatch();
      this.analyticsRootUrl = isStaging ? ANALYTICS_ROOT_URL_STAGING : ANALYTICS_ROOT_URL_PRODUCTION;
      this.bgHandlerThread = new BVPixelDispatcher.BvAnalyticsThread();
      this.okHttpClient = new OkHttpClient();
      this.dryRunAnalytics = false;
    }

    public Builder bgHandlerThread(@NonNull HandlerThread bgHandlerThread) {
      warnShouldNotBeEmpty("bgHandlerThread", bgHandlerThread);
      this.bgHandlerThread = bgHandlerThread;
      return this;
    }

    public Builder okHttpClient(@NonNull OkHttpClient okHttpClient) {
      warnShouldNotBeEmpty("okHttpClient", okHttpClient);
      this.okHttpClient = okHttpClient;
      return this;
    }

    public Builder dryRunAnalytics(boolean dryRunAnalytics) {
      this.dryRunAnalytics = dryRunAnalytics;
      return this;
    }

    public BVPixel build() {
      confirmBvPixelNotCreated();

      if (!bgHandlerThread.isAlive()) {
        bgHandlerThread.start();
      }

      BVPixelDispatcher bvPixelDispatcher = new BVPixelDispatcher(
          appContext,
          bgHandlerThread,
          bvAnalyticsBatch,
          okHttpClient,
          analyticsRootUrl,
          TimeUnit.SECONDS.toMillis(ANALYTICS_DELAY_SECONDS),
          dryRunAnalytics);

      singleton = new BVPixel(bvPixelDispatcher, clientId);
      return singleton;
    }
  }

  // endregion

  // region Internal API

  static <EventType extends BVAnalyticsEvent> boolean shouldDispatchImmediately(EventType event) {
    return
        event instanceof BVPersonalizationEvent ||
        event instanceof BVPageViewEvent;
  }

  private static void confirmBvPixelCreated() {
    if (singleton == null) {
      synchronized (BVPixel.class) {
        if (singleton == null) {
          throw new IllegalStateException("Must initialize BVPixel first.");
        }
      }
    }
  }

  private static void confirmBvPixelNotCreated() {
    if (singleton != null) {
      synchronized (BVPixel.class) {
        if (singleton != null) {
          throw new IllegalStateException("BVPixel singleton already exists.");
        }
      }
    }
  }

  /**
   * Used for testing to emulate app restart when this singleton would be destroyed
   */
  static void destroy() {
    synchronized (BVPixel.class) {
      singleton = null;
    }
  }

  // endregion
}
