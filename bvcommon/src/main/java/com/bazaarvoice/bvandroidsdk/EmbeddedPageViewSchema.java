/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * Internal API: Helper class to build embedded page view schema events for recommendations analytics
 */
class EmbeddedPageViewSchema extends BvAnalyticsSchema {

    private static final String eventClass = "PageView";
    private static final String eventType = "Embedded";
    private static final String KEY_REPORTING_GROUP = "reportingGroup";
    private static final String KEY_BV_PRODUCT = "bvProduct";

    public EmbeddedPageViewSchema(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, ReportingGroup reportingGroup, String bvProduct, String source) {
        super(eventClass, eventType, source);
        addPartialSchema(magpieMobileAppPartialSchema);
        if (reportingGroup != null) {
            addKeyVal(KEY_REPORTING_GROUP, reportingGroup.toString());
        }
        addKeyVal(KEY_BV_PRODUCT, bvProduct);
    }
}
