package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.warnShouldNotBeEmpty;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.FeatureUsedEvent.BRAND;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.FeatureUsedEvent.BV_FEATURE_TYPE;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.FeatureUsedEvent.BV_PRODUCT_TYPE;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.FeatureUsedEvent.CONTAINER_ID;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.FeatureUsedEvent.INTERACTION;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.FeatureUsedEvent.PRODUCT_ID;
import static com.bazaarvoice.bvandroidsdk.BVEventValues.FEATURE_IN_VIEW;

public class BVInViewEvent extends BVMobileAnalyticsEvent {
  private final String productId;
  private final String containerId;
  private final BVEventValues.BVProductType bvProductType;
  private final String brand;

  public BVInViewEvent(@NonNull String productId,
                @NonNull String containerId,
                @NonNull BVEventValues.BVProductType bvProductType,
                @Nullable String brand) {
    super(BVEventValues.BVEventClass.FEATURE, BVEventValues.BVEventType.USED);
    warnShouldNotBeEmpty("productId", productId);
    this.productId = productId;
    warnShouldNotBeEmpty("containerId", containerId);
    this.containerId = containerId;
    warnShouldNotBeEmpty("bvProductType", bvProductType);
    this.bvProductType = bvProductType;
    this.brand = brand;
  }

  @Override
  public Map<String, Object> toRaw() {
    Map<String, Object> map = super.toRaw();
    mapPutSafe(map, PRODUCT_ID, productId);
    mapPutSafe(map, CONTAINER_ID, containerId);
    mapPutSafe(map, BV_PRODUCT_TYPE, bvProductType.toString());
    mapPutSafe(map, BV_FEATURE_TYPE, FEATURE_IN_VIEW);
    mapPutSafe(map, INTERACTION, false);
    if (brand != null) {
      mapPutSafe(map, BRAND, brand);
    }
    return map;
  }
}
