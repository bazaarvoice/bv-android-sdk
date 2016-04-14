/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * Internal API: Wrapper around AnalyticsManger to form/send analytics
 * events for recommendations events
 */
class RecommendationsAnalyticsManager {

    public static void sendEmbeddedPageView(EmbeddedPageViewSchema.ReportingGroup reportingGroup) {
        BVSDK bvsdk = BVSDK.getInstance();
        AnalyticsManager analyticsManager = bvsdk.getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        EmbeddedPageViewSchema.Builder builder = new EmbeddedPageViewSchema.Builder(magpieMobileAppPartialSchema);
        EmbeddedPageViewSchema schema = builder.reportingGroup(reportingGroup)
                .brand(bvsdk.getClientId())
                .build();
        analyticsManager.enqueueEvent(schema.getDataMap());
    }

}
