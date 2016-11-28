/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

class ConversationSchemas {
    static abstract class ConversationUsedFeatureSchema extends UsedFeatureCanonicalSchema {
        private static final String source = "native-mobile-sdk";

        public ConversationUsedFeatureSchema(String name, boolean interaction, String productId, MagpieBvProduct magpieBvProduct, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema) {
            super(source, name, magpieBvProduct.toString());
            addProductId(productId);
            addInteraction(interaction);
            addPartialSchema(magpieMobileAppPartialSchema);
        }
    }
    /**
     * Internal API: Custom schema for conversations Used-Feature InView events
     */
    static class InViewUsedFeatureSchema extends ConversationUsedFeatureSchema {
        private static final String name = "InView";
        private static final boolean VALUE_INTERACTION = false;

        public InViewUsedFeatureSchema(String productId, MagpieBvProduct magpieBvProduct, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema) {
            super(name, VALUE_INTERACTION, productId, magpieBvProduct, magpieMobileAppPartialSchema);
        }
    }

    /**
     * Internal API: Custom schema for conversations Used-Feature Scrolled events
     */
    static class ScrolledUsedFeatureSchema extends ConversationUsedFeatureSchema {
        private static final String name = "Scrolled";
        private static final boolean VALUE_INTERACTION = true;

        public ScrolledUsedFeatureSchema(String productId, MagpieBvProduct magpieBvProduct, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema) {
            super(name, VALUE_INTERACTION, productId, magpieBvProduct, magpieMobileAppPartialSchema);
        }
    }

    /**
     * Internal API: Custom schema for conversations Used-Feature Write events
     */
    static class WriteUsedFeatureSchema extends ConversationUsedFeatureSchema {
        private static final String name = "Write";
        private static final boolean VALUE_INTERACTION = false;

        public WriteUsedFeatureSchema(String productId, MagpieBvProduct magpieBvProduct, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema) {
            super(name, VALUE_INTERACTION, productId, magpieBvProduct, magpieMobileAppPartialSchema);
        }
    }

    /**
     * Internal API: Custom schema for conversations UGC-Impression events
     */
    static class UgcImpressionSchema extends UgcImpressionCanonicalSchema {
        private static final String source = "native-mobile-sdk";

        public UgcImpressionSchema(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, String productId, String contentId, AnalyticsContentType analyticsContentType, MagpieBvProduct magpieBvProduct) {
            super(magpieMobileAppPartialSchema, productId, contentId, analyticsContentType.toString(), magpieBvProduct.toString(), source);
        }
    }

    enum AnalyticsContentType {
        Review("Review"),
        Question("Question"),
        Answer("Answer"),
        Store("Store"),
        StoreReview("StoreReview");

        private final String key;

        AnalyticsContentType(String key) {
            this.key = key;
        }

        public String toString() {
            return this.key;
        }
    }

}
