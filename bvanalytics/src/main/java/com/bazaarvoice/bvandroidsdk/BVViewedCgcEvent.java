package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.BVEventValues.BVProductType;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;
import static com.bazaarvoice.bvandroidsdk.BVEventValues.BVEventClass.FEATURE;
import static com.bazaarvoice.bvandroidsdk.BVEventValues.BVEventType.VIEWED_CGC;

/**
 * BVViewedCgcEvent should be used to indicate that UGC (User Generated Content)
 * is visible on the screen and has been in the view for an designated amount of
 * time. This event should only be fired once per lifetime of a View. This
 * differs from a BVInViewEvent in that a delay should be provided (typically
 * 5 seconds) to know that content is visible and readable on the screen.
 */
public final class BVViewedCgcEvent extends BVMobileAnalyticsEvent {
  private final String productId;
  private final BVProductType bvProductType;
  private final String rootCategoryId;
  private final String categoryId;
  private final String brand;

  /**
   *
   * @param productId Required - The product Id for which the UGC is assoicated.
   * @param bvProductType Required - The product with API key being used.
   * @param rootCategoryId Optional - This value should be obtained from the product feed.
   * @param categoryId Optional - This value should be obtained from the product feed.
   * @param brand Optional - The brand name of the product.
   */
  public BVViewedCgcEvent(
      @NonNull String productId,
      @NonNull BVProductType bvProductType,
      @Nullable String rootCategoryId,
      @Nullable String categoryId,
      @Nullable String brand) {
    super(FEATURE, VIEWED_CGC);
    this.productId = productId;
    this.bvProductType = bvProductType;
    this.rootCategoryId = rootCategoryId;
    this.categoryId = categoryId;
    this.brand = brand;
  }

  @Override
  public Map<String, Object> toRaw() {
    Map<String, Object> map = super.toRaw();
    mapPutSafe(map, BVEventKeys.ViewedCgcEvent.PRODUCT_ID, productId);
    mapPutSafe(map, BVEventKeys.ViewedCgcEvent.BV_PRODUCT_TYPE, bvProductType);
    mapPutSafe(map, BVEventKeys.ViewedCgcEvent.ROOT_CATEGORY_ID, rootCategoryId);
    mapPutSafe(map, BVEventKeys.ViewedCgcEvent.CATEGORY_ID, categoryId);
    mapPutSafe(map, BVEventKeys.ViewedCgcEvent.BRAND, brand);
    return map;
  }
}
