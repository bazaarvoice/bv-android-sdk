package com.bazaarvoice.bvandroidsdk;


import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.warnShouldNotBeEmpty;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;

public final class BVImpressionEvent extends BVMobileAnalyticsEvent {
  private final String productId;
  private final String contentId;
  private final BVEventValues.BVProductType bvProductType;
  private final BVEventValues.BVImpressionContentType bvImpressionContentType;
  private final String categoryId;
  private final String brand;

  public BVImpressionEvent(@NonNull String productId,
                           @NonNull String contentId,
                           @NonNull BVEventValues.BVProductType bvProductType,
                           @NonNull BVEventValues.BVImpressionContentType bvImpressionContentType,
                           @Nullable String categoryId,
                           @Nullable String brand) {
    super(BVEventValues.BVEventClass.IMPRESSION, BVEventValues.BVEventType.UGC);
    warnShouldNotBeEmpty("productId", productId);
    this.productId = productId;
    warnShouldNotBeEmpty("contentId", contentId);
    this.contentId = contentId;
    warnShouldNotBeEmpty("bvProductType", bvProductType);
    this.bvProductType = bvProductType;
    warnShouldNotBeEmpty("bvImpressionContentType", bvImpressionContentType);
    this.bvImpressionContentType = bvImpressionContentType;
    this.categoryId = categoryId;
    this.brand = brand;
  }

  @Override
  public Map<String, Object> toRaw() {
    Map<String, Object> map = super.toRaw();
    mapPutSafe(map, BVEventKeys.ImpressionEvent.PRODUCT_ID, productId);
    mapPutSafe(map, BVEventKeys.ImpressionEvent.CONTENT_ID, contentId);
    mapPutSafe(map, BVEventKeys.ImpressionEvent.BV_CONTENT_TYPE, bvImpressionContentType.toString());
    mapPutSafe(map, BVEventKeys.ImpressionEvent.BV_PRODUCT_TYPE, bvProductType.toString());
    if (categoryId != null) {
      mapPutSafe(map, BVEventKeys.ImpressionEvent.CATEGORY_ID, categoryId);
    }
    if (brand != null) {
      mapPutSafe(map, BVEventKeys.ImpressionEvent.BRAND, brand);
    }
    return map;
  }
}
