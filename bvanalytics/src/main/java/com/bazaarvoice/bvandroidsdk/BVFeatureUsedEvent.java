package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;

public final class BVFeatureUsedEvent extends BVMobileAnalyticsEvent {
  private final String productId;
  private final BVEventValues.BVProductType bvProductType;
  private final BVEventValues.BVFeatureUsedEventType bvFeatureUsedEventType;
  private final String brand;

  public BVFeatureUsedEvent(String productId,
                            @NonNull BVEventValues.BVProductType bvProductType,
                            @NonNull BVEventValues.BVFeatureUsedEventType bvFeatureUsedEventType,
                            @Nullable String brand) {
    super(BVEventValues.BVEventClass.FEATURE, BVEventValues.BVEventType.USED);
    this.productId = productId;
    this.bvProductType = bvProductType;
    this.bvFeatureUsedEventType = bvFeatureUsedEventType;
    this.brand = brand;
  }

  @Override
  public Map<String, Object> toRaw() {
    Map<String, Object> map = super.toRaw();
    if (brand != null) {
      mapPutSafe(map, BVEventKeys.FeatureUsedEvent.BRAND, brand);
    }

    boolean isInteractionEvent = bvFeatureUsedEventType != BVEventValues.BVFeatureUsedEventType.IN_VIEW;
    mapPutSafe(map, BVEventKeys.FeatureUsedEvent.INTERACTION, isInteractionEvent);

    mapPutSafe(map, BVEventKeys.FeatureUsedEvent.PRODUCT_ID, productId);
    mapPutSafe(map, BVEventKeys.FeatureUsedEvent.BV_PRODUCT_TYPE, bvProductType.toString());
    mapPutSafe(map, BVEventKeys.FeatureUsedEvent.BV_FEATURE_NAME, bvFeatureUsedEventType.toString());
    return map;
  }
}
