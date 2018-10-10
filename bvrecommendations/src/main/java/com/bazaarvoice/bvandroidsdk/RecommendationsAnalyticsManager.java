/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

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

    public static void sendEmbeddedPageView(ReportingGroup reportingGroup,
                                            String productId,
                                            String categoryId,
                                            PageType pageType,
                                            ShopperProfile shopperProfile) {

        BVPageViewEvent pageViewEvent = new BVPageViewEvent(productId, BVEventValues.BVProductType.PERSONALIZATION, categoryId);
        Map<String, Object> shopperProfileParams = getShopperProfileAttributesPartialSchema(shopperProfile, pageType);
        mapPutSafe(shopperProfileParams, "component", reportingGroup.toString());
        pageViewEvent.setAdditionalParams(shopperProfileParams);
        bvPixel.track(pageViewEvent);

    }

    public static void sendFeatureUsedEvent(String component, ShopperProfile shopperProfile, BVProduct bvProduct) {
        BVFeatureUsedEvent bvFeatureUsedEvent = new BVFeatureUsedEvent(
                bvProduct.getId(),
                BVEventValues.BVProductType.PERSONALIZATION,
                BVEventValues.BVFeatureUsedEventType.IN_VIEW,
                null);
        Map<String, Object> additionalParams = getShopperProfileAttributesPartialSchema(shopperProfile, null);
        mapPutSafe(additionalParams, "categoryId", getCategoryId(bvProduct));
        mapPutSafe(additionalParams, "type", "Used");
        mapPutSafe(additionalParams, "component", component);
        bvFeatureUsedEvent.setAdditionalParams(additionalParams);
        bvPixel.track(bvFeatureUsedEvent);
    }

    /**
     * Event when a product is tapped (i.e. conversion)
     *
     * @param bvProduct
     */
    public static void sendProductConversionEvent(ShopperProfile shopperProfile, BVProduct bvProduct) {
        if (!shouldSendProductEvent(bvProduct)) {
            return;
        }

        BVFeatureUsedEvent recommendationProductEvent = new BVFeatureUsedEvent(bvProduct.getId(), BVEventValues.BVProductType.PERSONALIZATION, BVEventValues.BVFeatureUsedEventType.ENGAGED, null);
        Map<String, Object> additionalParams = getRecommendationAttributesPartialSchema(bvProduct);
        additionalParams.putAll(getShopperProfileAttributesPartialSchema(shopperProfile,null));
        mapPutSafe(additionalParams, "productId", bvProduct.getId());
        mapPutSafe(additionalParams, "categoryId", getCategoryId(bvProduct));
        recommendationProductEvent.setAdditionalParams(additionalParams);

        bvPixel.track(recommendationProductEvent);
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

    public static void sendBvViewGroupInteractedWithEvent(ReportingGroup reportingGroup,
                                                          PageType pageType,
                                                          ShopperProfile shopperProfile) {
        BVFeatureUsedEvent event = new BVFeatureUsedEvent(null, BVEventValues.BVProductType.PERSONALIZATION, BVEventValues.BVFeatureUsedEventType.SCROLLED, null);
        final Map<String, Object> additionalParams = getShopperProfileAttributesPartialSchema(shopperProfile, pageType);
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
        return recommendationSchema;
    }

    private static Map<String, Object> getShopperProfileAttributesPartialSchema(ShopperProfile shopperProfile, PageType pageType) {
        Map<String, Object> shopperProfileParams = new HashMap<>();
        if (pageType != null) {
            mapPutSafe(shopperProfileParams, "pageType", pageType.toString());
        }
        if (shopperProfile != null && shopperProfile.getProfile() != null) {
            Profile profile = shopperProfile.getProfile();
            mapPutSafe(shopperProfileParams, "plan", shopperProfile.getProfile().getPlan());
            mapPutSafe(shopperProfileParams, "activeUser", shopperProfile.getProfile().getRecommendationStats().isActiveUser());
            if (profile.getRecommendedProducts() != null) {
                mapPutSafe(shopperProfileParams, "numRecommendations", shopperProfile.getProfile().getRecommendedProducts().size());
            }
            RecommendationStats stats = profile.getRecommendationStats();
            if (stats != null) {
                shopperProfileParams.putAll(getShopperStatsPartialSchema(stats));
            }
        }

        return shopperProfileParams;
    }

    private static Map<String, Object> getShopperStatsPartialSchema(RecommendationStats stats) {
        Map<String, Object> shopperProfileParams = new HashMap<>();

        if (stats != null) {
            mapPutSafe(shopperProfileParams, KEY_RKC, stats.getRkc());
            mapPutSafe(shopperProfileParams, KEY_RKT, stats.getRkt());
            mapPutSafe(shopperProfileParams, KEY_RKP, stats.getRkp());
            mapPutSafe(shopperProfileParams, KEY_RKI, stats.getRki());
            mapPutSafe(shopperProfileParams, KEY_RKB, stats.getRkb());
        }
        return shopperProfileParams;
    }

    private static boolean isProductValid(BVProduct bvProduct) {
        return bvProduct != null && bvProduct.getId() != null;
    }

    private static String getCategoryId(BVProduct bvProduct) {
        if(bvProduct != null && bvProduct.getCategoryIds() != null && bvProduct.getCategoryIds().size() > 0) {
            return bvProduct.getCategoryIds().get(bvProduct.getCategoryIds().size() - 1);
        }
        return null;
    }
}
