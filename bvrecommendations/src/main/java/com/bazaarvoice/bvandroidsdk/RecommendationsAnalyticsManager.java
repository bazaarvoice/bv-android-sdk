/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * Wrapper around AnalyticsManger to form/send analytics
 * events for recommendations events
 */
public class RecommendationsAnalyticsManager {
    private static final String TAG = RecommendationsAnalyticsManager.class.getSimpleName();

    private static AnalyticsManager analyticsManager = BVSDK.getInstance().getBvWorkerData().getAnalyticsManager();

    public static void sendEmbeddedPageView(ReportingGroup reportingGroup, String productId, String categoryId, int numRecommendations) {
        BVSDK bvsdk = BVSDK.getInstance();
        AnalyticsManager analyticsManager = bvsdk.getBvWorkerData().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        RecommendationsEmbeddedPageViewSchema schema = new RecommendationsEmbeddedPageViewSchema.Builder(magpieMobileAppPartialSchema, reportingGroup)
                .productId(productId)
                .categoryId(categoryId)
                .numRecommendations(numRecommendations)
                .build();
        analyticsManager.enqueueEvent(schema);
    }

    /**
     * Event when a single product recommendation appears on screen
     *
     * @param bvProduct
     */
    public static void sendProductImpressionEvent(BVProduct bvProduct) {
        BVLogger bvLogger = BVSDK.getInstance().getBvLogger();
        bvLogger.d("RecAnalytics", "bvProduct " + bvProduct.getId() + " is impressed?" + bvProduct.impressed);
        if (!shouldSendProductEvent(bvProduct) || bvProduct.impressed) {
            return;
        }
        bvProduct.impressed = true;

        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        RecommendationAttributesPartialSchema recommendationAttributesPartialSchema = getRecommendationAttributesPartialSchema(bvProduct);
        RecommendationImpressionSchema schema = new RecommendationImpressionSchema(bvProduct.getId(), magpieMobileAppPartialSchema, recommendationAttributesPartialSchema);

        analyticsManager.enqueueEvent(schema);
    }

    /**
     * Event when a product is tapped (i.e. conversion)
     *
     * @param bvProduct
     */
    public static void sendProductConversionEvent(BVProduct bvProduct) {
        if (!shouldSendProductEvent(bvProduct)) {
            return;
        }

        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        RecommendationAttributesPartialSchema recommendationAttributesPartialSchema = getRecommendationAttributesPartialSchema(bvProduct);
        RecommendationUsedFeatureSchema schema = new RecommendationUsedFeatureSchema(Feature.CONTENT_CLICK, bvProduct.getProductId(), null, magpieMobileAppPartialSchema, recommendationAttributesPartialSchema);

        analyticsManager.enqueueEvent(schema);
    }

    private static boolean shouldSendProductEvent(BVProduct bvProduct) {
        BVLogger bvLogger = BVSDK.getInstance().getBvLogger();
        String productId = bvProduct == null ? "null_product" : bvProduct.getId();
        if (!isProductValid(bvProduct)) {
            bvLogger.w(TAG, "Product impression not sent for invalid product: " + productId);
            return false;
        } else {
            return true;
        }
    }

    public static void sendBvViewGroupAddedToHierarchyEvent(ReportingGroup reportingGroup) {

        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        RecommendationUsedFeatureSchema schema = new RecommendationUsedFeatureSchema(Feature.INVIEW, null, reportingGroup, magpieMobileAppPartialSchema, null);
        analyticsManager.enqueueEvent(schema);
    }

    public static void sendBvViewGroupInteractedWithEvent(ReportingGroup reportingGroup) {
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        RecommendationUsedFeatureSchema schema = new RecommendationUsedFeatureSchema(Feature.SCROLLED, null, reportingGroup, magpieMobileAppPartialSchema, null);
        analyticsManager.enqueueEvent(schema);
    }


    private static RecommendationAttributesPartialSchema getRecommendationAttributesPartialSchema(BVProduct product) {
        RecommendationAttributesPartialSchema.Builder builder = new RecommendationAttributesPartialSchema.Builder();

        RecommendationStats stats = product.getRecommendationStats();
        if (stats != null) {
            builder.rkb(stats.getRkb()).rkc(stats.getRkc()).rki(stats.getRki()).rkp(stats.getRkp()).rkt(stats.getRkt());
        }

        builder.rs(product.getRs()).sponsored(product.isSponsored());

        return builder.build();
    }

    private static boolean isProductValid(BVProduct bvProduct) {
        return bvProduct != null && bvProduct.getId() != null;
    }
}
