package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.warnShouldNotBeEmpty;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.generateLoadId;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.NonCommerceConversionEvent.LABEL;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.NonCommerceConversionEvent.LOAD_ID;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.NonCommerceConversionEvent.VALUE;

public class BVConversionEvent extends BVPiiEvent {
  @NonNull private final String value;
  @NonNull private final String loadId;
  @Nullable private final String label;

  public BVConversionEvent(@NonNull String type,
                           @NonNull String value,
                           @Nullable String label) {
    super(BVEventValues.BVEventClass.PII_CONVERSION, type);
    warnShouldNotBeEmpty("value", value);
    this.value = value;
    this.label = label;
    this.loadId = generateLoadId();
  }

  @Override
  public Map<String, Object> toRawNonPii() {
    Map<String, Object> map = super.toRawNonPii();
    mapPutSafe(map, BVEventKeys.Event.CLASS, BVEventValues.BVEventClass.CONVERSION.toString());
    return map;
  }

  @Override
  public Map<String, Object> toRaw() {
    Map<String, Object> map = super.toRaw();
    mapPutSafe(map, BVEventKeys.Event.CLASS, BVEventValues.BVEventClass.PII_CONVERSION.toString());
    return map;
  }

  @Override
  protected Map<String, Object> getPiiUnrelatedParams() {
    Map<String, Object> map = new HashMap<>();
    mapPutSafe(map, VALUE, value);
    mapPutSafe(map, LOAD_ID, loadId);
    mapPutSafe(map, LABEL, label);
    return map;
  }
}
