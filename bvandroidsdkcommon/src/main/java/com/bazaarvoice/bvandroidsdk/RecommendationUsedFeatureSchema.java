/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

/**
 * Internal API: Custom schema for recommendation used feature events
 */
class RecommendationUsedFeatureSchema extends UsedFeatureCanonicalSchema {

    private static final String source = "recommendation-mob";
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
        super(source, name);
        if (feature != null) {
            addDetail1(feature.getValue());
        }
        addProductId(productId);
        addPartialSchema(magpieMobileAppPartialSchema);
        addPartialSchema(recommendationAttributesPartialSchema);
    }

}
