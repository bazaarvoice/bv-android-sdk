package com.bazaarvoice.bvandroidsdk;


import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.addPiiOnly;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutAllSafe;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.nonPiiOnly;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.NonCommerceConversionEvent.HAD_PII;

public abstract class BVPiiEvent extends BVMobileAnalyticsEvent {
  @NonNull
  private final Map<String, Object> piiParams;
  private boolean hadPii;

  public BVPiiEvent(BVEventValues.BVEventClass eventClass, String customEventType) {
    super(eventClass, customEventType);
    this.piiParams = new HashMap<>();
  }

  public BVPiiEvent(BVEventValues.BVEventClass eventClass, BVEventValues.BVEventType eventType) {
    super(eventClass, eventType);
    this.piiParams = new HashMap<>();
  }

  @Override
  public void setAdditionalParams(Map<String, Object> additionalParams) {
    if (additionalParams == null) {
      return;
    }
    this.additionalParams = nonPiiOnly(additionalParams);
    this.hadPii = addPiiOnly(piiParams, additionalParams);
  }


  public Map<String, Object> toRawNonPii() {
    setAllowAdId(true);
    Map<String, Object> map = getCommonParams();
    return map;
  }

  @Override
  public Map<String, Object> toRaw() {
    setAllowAdId(false);
    Map<String, Object> map = getCommonParams();
    mapPutAllSafe(map, piiParams);
    return map;
  }

  protected abstract Map<String, Object> getPiiUnrelatedParams();

  private Map<String, Object> getCommonParams() {
    Map<String, Object> map = super.toRaw();
    mapPutAllSafe(map, getPiiUnrelatedParams());
    mapPutSafe(map, HAD_PII, hadPii);
    return map;
  }

}
