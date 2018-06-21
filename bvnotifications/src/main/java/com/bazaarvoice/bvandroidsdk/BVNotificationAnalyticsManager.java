package com.bazaarvoice.bvandroidsdk;

import java.util.HashMap;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;

/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
class BVNotificationAnalyticsManager {

    enum NotificationAction {
        Positive("idactionreply"),
        Neutral("idactionremind"),
        Negative("idactiondismiss");

        private final String key;
        NotificationAction(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }

    static void sendNotificationEventForNotificationInView(String productId, final String analyticsViewName, final String analyticsCgcType){
        BVPixel bvPixel = BVSDK.getInstance().getBvPixel();
        BVAnalyticsEvent event = new BVFeatureUsedEvent(productId, BVEventValues.BVProductType.CONVERSATIONS_REVIEWS, BVEventValues.BVFeatureUsedEventType.NOTIFICATION, null );
        final Map<String, Object> additionalParams = new HashMap<>();
        mapPutSafe(additionalParams, BVEventKeys.FeatureUsedEvent.DETAIL_1, analyticsViewName);
        mapPutSafe(additionalParams, BVEventKeys.FeatureUsedEvent.DETAIL_2, analyticsCgcType);
        mapPutSafe(additionalParams, "name", "InView");
        event.setAdditionalParams(additionalParams);
        bvPixel.track(event);
    }

    static void sendNotificationEventForStoreReviewFeatureUsed(String actionDetail, String productId, String remoteConfigCgcType){
        BVPixel bvPixel = BVSDK.getInstance().getBvPixel();
        BVAnalyticsEvent event = new BVFeatureUsedEvent(productId, BVEventValues.BVProductType.CONVERSATIONS_REVIEWS, BVEventValues.BVFeatureUsedEventType.NOTIFICATION, null );
        final Map<String, Object> additionalParams = new HashMap<>();
        mapPutSafe(additionalParams, BVEventKeys.FeatureUsedEvent.DETAIL_1, actionDetail);
        mapPutSafe(additionalParams, BVEventKeys.FeatureUsedEvent.DETAIL_2, remoteConfigCgcType);
        event.setAdditionalParams(additionalParams);
        bvPixel.track(event);
    }

}