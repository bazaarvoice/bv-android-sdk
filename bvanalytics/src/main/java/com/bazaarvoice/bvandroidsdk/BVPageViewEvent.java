package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.warnShouldNotBeEmpty;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.PageViewEvent.BV_PRODUCT_TYPE;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.PageViewEvent.CATEGORY_ID;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.PageViewEvent.PRODUCT_ID;

public final class BVPageViewEvent extends BVMobileAnalyticsEvent {
  private final String productId;
  private final String categoryId;
  private final BVEventValues.BVProductType bvProductType;

  public BVPageViewEvent(@NonNull String productId,
                         @NonNull BVEventValues.BVProductType bvProductType,
                         @Nullable String categoryId) {
    super(BVEventValues.BVEventClass.PAGE_VIEW, BVEventValues.BVEventType.PRODUCT);
    warnShouldNotBeEmpty("productId", productId);
    this.productId = productId;
    warnShouldNotBeEmpty("bvProductType", bvProductType);
    this.bvProductType = bvProductType;
    this.categoryId = categoryId;
  }

  @Override
  public Map<String, Object> toRaw() {
    Map<String, Object> map = super.toRaw();
    mapPutSafe(map, PRODUCT_ID, productId);
    if (categoryId != null) {
      mapPutSafe(map, CATEGORY_ID, categoryId);
    }
    mapPutSafe(map, BV_PRODUCT_TYPE, bvProductType.toString());
    return map;
  }
}
