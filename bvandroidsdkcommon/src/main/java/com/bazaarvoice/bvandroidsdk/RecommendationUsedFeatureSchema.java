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

    public enum Feature {
        CONVERSION("conversion"), INVIEW("inview"), SCROLLED("scrolled"), SWIPE("swipe");

        private String value;

        Feature(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum Component {
        RECYCLERVIEW("recyclerView"), LISTVIEW("listView"), GRIDVIEW("gridView"), CUSTOM("custom");

        private String value;

        Component(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public RecommendationUsedFeatureSchema(Feature feature, String productId, Component component, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, RecommendationAttributesPartialSchema recommendationAttributesPartialSchema) {
        super(source, feature.toString());
        addProductId(productId);
        addPartialSchema(magpieMobileAppPartialSchema);
        addPartialSchema(recommendationAttributesPartialSchema);
        if (component != null) {
            addComponent(component.toString());
        }
    }

}
