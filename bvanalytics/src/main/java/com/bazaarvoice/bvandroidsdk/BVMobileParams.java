package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import androidx.annotation.NonNull;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.warnShouldNotBeEmpty;

public class BVMobileParams {
  private final BVAdvertisingId bvAdvertisingId;
  private final BVCommonAnalyticsParams bvCommonAnalyticsParams;
  private final com.bazaarvoice.bvandroidsdk.BVMobileInfo bvMobileInfo;
  private final String clientId;
  private final BVEventValues.BVEventSource source;

  /**
   * Must be constructed off of the UI Thread since this calls
   * {@link com.google.android.gms.ads.identifier.AdvertisingIdClient#getAdvertisingIdInfo(Context)}
   * which will throw an IllegalStateException if run on the UI Thread
   *
   * @param context Application Context
   */
  public BVMobileParams(Context context, String clientId) {
    this(context, clientId, BVEventValues.BVEventSource.NATIVE_MOBILE_CUSTOM);
  }

  BVMobileParams(Context context, String clientId, BVEventValues.BVEventSource source) {
    warnShouldNotBeEmpty("Context", context);
    Context appContext = context.getApplicationContext();
    this.bvAdvertisingId = new BVAdvertisingId(appContext);
    this.bvCommonAnalyticsParams = new BVCommonAnalyticsParams(appContext);
    this.bvMobileInfo = new BVMobileInfo(appContext);
    warnShouldNotBeEmpty("clientId", clientId);
    this.clientId = clientId;
    this.source = source;
  }

  @NonNull
  BVAdvertisingId getBvAdvertisingId() {
    return bvAdvertisingId;
  }

  @NonNull
  BVCommonAnalyticsParams getBvCommonAnalyticsParams() {
    return bvCommonAnalyticsParams;
  }

  @NonNull
  com.bazaarvoice.bvandroidsdk.BVMobileInfo getMobileInfo() {
    return bvMobileInfo;
  }

  @NonNull
  String getClientId() {
    return clientId;
  }

  @NonNull
  BVEventValues.BVEventSource getSource() {
    return source;
  }
}
