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
        private NotificationAction(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }
    
    static final void sendNotificationEventForNotificationInView(String viewName, String productId){

        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        NotificationUsedFeatureSchema notificationUsedFeatureSchema = new NotificationUsedFeatureSchema(true, productId, viewName, "store", magpieMobileAppPartialSchema);
        analyticsManager.enqueueEvent(notificationUsedFeatureSchema);

    }

    static final void sendNotifiactionEnentForStoreReviewFeatureUsed(String actionDetail, String productId){

        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        NotificationUsedFeatureSchema notificationUsedFeatureSchema = new NotificationUsedFeatureSchema(false, productId, actionDetail, "store", magpieMobileAppPartialSchema);
        analyticsManager.enqueueEvent(notificationUsedFeatureSchema);

    }

}
