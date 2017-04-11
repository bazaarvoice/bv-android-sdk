package com.bazaarvoice.bvandroidsdk;

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
    
    static void sendNotificationEventForNotificationInView(String productId, String analyticsViewName, String analyticsCgcType){
        AnalyticsManager analyticsManager = BVSDK.getInstance().getBvWorkerData().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        NotificationUsedFeatureSchema notificationUsedFeatureSchema = new NotificationUsedFeatureSchema(true, productId, analyticsViewName, analyticsCgcType, magpieMobileAppPartialSchema);
        analyticsManager.enqueueEvent(notificationUsedFeatureSchema);

    }

    static void sendNotificationEventForStoreReviewFeatureUsed(String actionDetail, String productId, String remoteConfigCgcType){
        AnalyticsManager analyticsManager = BVSDK.getInstance().getBvWorkerData().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        NotificationUsedFeatureSchema notificationUsedFeatureSchema = new NotificationUsedFeatureSchema(false, productId, actionDetail, remoteConfigCgcType, magpieMobileAppPartialSchema);
        analyticsManager.enqueueEvent(notificationUsedFeatureSchema);
    }

}
