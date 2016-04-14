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
    private static final String source = "recommendation-mob";
    private static final String KEY_REPORTING_GROUP = "reportingGroup";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_BV_PRODUCT = "bvProduct";

    private static final String bvProduct = "Recommendations";

    public enum ReportingGroup {
        LISTVIEW("listView"), GRIDVIEW("gridView"), RECYCLERVIEW("recyclerView"), CUSTOM("custom");

        private String value;

        ReportingGroup(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private EmbeddedPageViewSchema(Builder builder) {
        super(eventClass, eventType, source);
        addPartialSchema(builder.magpieMobileAppPartialSchema);
        if (builder.reportingGroup != null) {
            addKeyVal(KEY_REPORTING_GROUP, builder.reportingGroup.toString());
        }
        addKeyVal(KEY_BRAND, builder.brand);
        addKeyVal(KEY_BV_PRODUCT, bvProduct);
    }

    public static final class Builder {
        private MagpieMobileAppPartialSchema magpieMobileAppPartialSchema;
        private ReportingGroup reportingGroup;
        private String brand;

        public Builder(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema) {
            this.magpieMobileAppPartialSchema = magpieMobileAppPartialSchema;
        }

        public Builder reportingGroup(ReportingGroup reportingGroup) {
            this.reportingGroup = reportingGroup;
            return this;
        }

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public EmbeddedPageViewSchema build() {
            return new EmbeddedPageViewSchema(this);
        }
    }


}
