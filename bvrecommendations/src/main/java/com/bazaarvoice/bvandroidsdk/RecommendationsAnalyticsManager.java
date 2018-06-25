/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.mapPutSafe;

/**
 * Wrapper around AnalyticsManger to form/send analytics
 * events for recommendations events
 */
public class RecommendationsAnalyticsManager {
    private static final String TAG = RecommendationsAnalyticsManager.class.getSimpleName();

    private static BVPixel bvPixel = BVSDK.getInstance().getBvPixel();
    private static final String KEY_RKC = "RKC";
    private static final String KEY_RKT = "RKT";
    private static final String KEY_RKP = "RKP";
    private static final String KEY_RKI = "RKI";
    private static final String KEY_RKB = "RKB";
    private static final String KEY_RS = "RS";
    private static final String KEY_SPONSORED = "sponsored";

    public static void sendEmbeddedPageView(ReportingGroup reportingGroup, String productId, String categoryId, int numRecommendations) {
        BVPageViewEvent pageViewEvent = new BVPageViewEvent(productId, BVEventValues.BVProductType.PERSONALIZATION,categoryId);

        Map<String, Object> additionalParams = new HashMap<>();
        mapPutSafe(additionalParams, "component", reportingGroup.toString());
        mapPutSafe(additionalParams, "numRecommendations", numRecommendations);
        pageViewEvent.setAdditionalParams(additionalParams);

        bvPixel.track(pageViewEvent);
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

        BVRecomendationImpressionEvent recomendationProductEvent = new BVRecomendationImpressionEvent(bvProduct.getId());
        recomendationProductEvent.setAdditionalParams(getRecommendationAttributesPartialSchema(bvProduct));

        bvPixel.track(recomendationProductEvent);
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

        BVFeatureUsedEvent recomendationProductEvent = new BVFeatureUsedEvent(bvProduct.getId(), BVEventValues.BVProductType.PERSONALIZATION, BVEventValues.BVFeatureUsedEventType.CONTENT_CLICK,  null);
        Map<String, Object> additionalParams = getRecommendationAttributesPartialSchema(bvProduct);
        mapPutSafe(additionalParams, "productId", bvProduct.getId());
        recomendationProductEvent.setAdditionalParams(additionalParams);

        bvPixel.track(recomendationProductEvent);
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
        BVFeatureUsedEvent event = new BVFeatureUsedEvent("", BVEventValues.BVProductType.PERSONALIZATION, BVEventValues.BVFeatureUsedEventType.IN_VIEW, null);
        final Map<String, Object> additionalParams = new HashMap<>();
        additionalParams.put("component", reportingGroup.toString());
        event.setAdditionalParams(additionalParams);
        bvPixel.track(event);
    }

    public static void sendBvViewGroupInteractedWithEvent(ReportingGroup reportingGroup) {
        BVFeatureUsedEvent event = new BVFeatureUsedEvent("", BVEventValues.BVProductType.PERSONALIZATION, BVEventValues.BVFeatureUsedEventType.SCROLLED, null);
        final Map<String, Object> additionalParams = new HashMap<>();
        additionalParams.put("component", reportingGroup.toString());
        event.setAdditionalParams(additionalParams);
        bvPixel.track(event);
    }

    private static Map<String, Object> getRecommendationAttributesPartialSchema(@NonNull BVProduct product) {
        Map<String, Object> recommendationSchema = new HashMap<>();
        RecommendationStats stats = product.getRecommendationStats();
        if (stats != null) {
            mapPutSafe(recommendationSchema, KEY_RKC, stats.getRkc());
            mapPutSafe(recommendationSchema, KEY_RKT, stats.getRkt());
            mapPutSafe(recommendationSchema, KEY_RKP, stats.getRkp());
            mapPutSafe(recommendationSchema, KEY_RKI, stats.getRki());
            mapPutSafe(recommendationSchema, KEY_RKB, stats.getRkb());
        }
        mapPutSafe(recommendationSchema, KEY_RS, product.getRs());
        mapPutSafe(recommendationSchema, KEY_SPONSORED, product.isSponsored());
        return  recommendationSchema;
    }

    private static boolean isProductValid(BVProduct bvProduct) {
        return bvProduct != null && bvProduct.getId() != null;
    }
}
