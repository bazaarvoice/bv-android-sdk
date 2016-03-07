/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

class RecommendationUsedFeatureSchema extends BvAnalyticsSchema {

    private static final String KEY_BV_PRODUCT = "bvProduct";
    private static final String KEY_NAME = "name";
    private static final String KEY_DETAIL1 = "detail1";
    private static final String KEY_PRODUCT_ID = "productId";
    private static final String eventClass = "Feature";
    private static final String eventType = "Used";
    private static final String source = "recommendation-mob";

    private static final String bvProduct = "Recommendations";

    private static final String name = "conversion";

    public enum RecommendationFeature {
        CAROUSEL("carousel"), STATIC_VIEW("staticview"),
        TABLE_VIEW("tableview"), CUSTOM("custom"),
        UNKNOWN("unknown");

        private String value;

        RecommendationFeature(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public RecommendationUsedFeatureSchema(String productId, RecommendationFeature feature, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, RecommendationAttributesPartialSchema recommendationAttributesPartialSchema) {
        super(eventClass, eventType, source);
        addKeyVal(KEY_BV_PRODUCT, bvProduct);
        addKeyVal(KEY_NAME, name);
        if (feature != null) {
            addKeyVal(KEY_DETAIL1, feature.getValue());
        }
        addKeyVal(KEY_PRODUCT_ID, productId);
        addPartialSchema(magpieMobileAppPartialSchema);
        addPartialSchema(recommendationAttributesPartialSchema);
    }

}
