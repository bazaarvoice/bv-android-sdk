package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.warnShouldNotBeEmpty;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;

public abstract class BVMobileAnalyticsEvent extends BVAnalyticsEvent {
  private BVMobileParams bvMobileParams;
  private boolean allowAdId;

  public BVMobileAnalyticsEvent(BVEventValues.BVEventClass eventClass,
                                String customEventType) {
    super(eventClass, customEventType);
    allowAdId = true;
  }

  public BVMobileAnalyticsEvent(BVEventValues.BVEventClass eventClass,
                                BVEventValues.BVEventType eventType) {
    super(eventClass, eventType);
    allowAdId = true;
  }

  public void setBvMobileParams(@NonNull BVMobileParams bvMobileParams) {
    warnShouldNotBeEmpty("bvMobileParams", bvMobileParams);
    this.bvMobileParams = bvMobileParams;
    setBvCommonAnalyticsParams(bvMobileParams.getBvCommonAnalyticsParams());
  }

  protected BVMobileParams getBvMobileParams() {
    return bvMobileParams;
  }

  protected void setAllowAdId(boolean allowAdId) {
    this.allowAdId = allowAdId;
  }

  @NonNull
  private String getAllowedAdId() {
    String allowedAdId = null;
    if (bvMobileParams == null ||
        bvMobileParams.getBvAdvertisingId().getAdvertisingId() == null ||
        bvMobileParams.getBvAdvertisingId().getAdvertisingId().isEmpty()) {
      // If AdId not available use nontracking
      allowedAdId = BVEventValues.NONTRACKING_TOKEN;
    } else {
      // If AdId not allowed (e.g. pii event) use nontracking, else use actual AdId
      allowedAdId = allowAdId ?
          bvMobileParams.getBvAdvertisingId().getAdvertisingId() :
          BVEventValues.NONTRACKING_TOKEN;
    }
    return allowedAdId;
  }

  @Override
  public Map<String, Object> toRaw() {
    warnShouldNotBeEmpty("bvMobileParams", bvMobileParams);
    Map<String, Object> map = super.toRaw();
    mapPutSafe(map, BVEventKeys.MobileEvent.ADVERTISING_ID, getAllowedAdId());
    mapPutSafe(map, BVEventKeys.MobileEvent.MOBILE_SOURCE, BVEventValues.MOBILE_SOURCE);
    mapPutSafe(map, BVEventKeys.MobileEvent.CLIENT_ID, bvMobileParams.getClientId());
    mapPutSafe(map, BVEventKeys.Event.SOURCE, bvMobileParams.getSource().toString());
    return map;
  }
}
