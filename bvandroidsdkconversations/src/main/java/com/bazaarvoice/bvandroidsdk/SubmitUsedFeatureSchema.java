/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

/**
 * Internal API: Custom schema for conversations submit used feature
 */
class SubmitUsedFeatureSchema extends UsedFeatureCanonicalSchema {

    private static final String KEY_FINGERPRINT = "fingerprinting";
    private static final String source = "native-mobile-sdk";

    private SubmitUsedFeatureSchema(Builder builder) {
        super(source, builder.submitFeature.toString());
        addCategoryId(builder.categoryId);
        addProductId(builder.productId);
        addKeyVal(KEY_FINGERPRINT, builder.fingerprint);
        addPartialSchema(builder.magpieMobileAppPartialSchema);
    }

    public enum SubmitFeature {
        ANSWER("Answer"), COMMENT("Comment"), STORY_COMMENT("StoryComment"),
        FEEDBACK("Feedback"), ASK("Ask"), WRITE("Write"),
        STORY("Story");

        private String value;

        SubmitFeature(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static final class Builder {
        private String categoryId;
        private String productId;
        private boolean fingerprint;
        private SubmitFeature submitFeature;
        private MagpieMobileAppPartialSchema magpieMobileAppPartialSchema;

        public Builder(SubmitFeature submitFeature, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema) {
            this.submitFeature = submitFeature;
            this.magpieMobileAppPartialSchema = magpieMobileAppPartialSchema;
        }

        public Builder categoryId(String val) {
            categoryId = val;
            return this;
        }

        public Builder productId(String val) {
            productId = val;
            return this;
        }

        public Builder fingerprint(boolean val) {
            fingerprint = val;
            return this;
        }

        public SubmitUsedFeatureSchema build() {
            return new SubmitUsedFeatureSchema(this);
        }
    }

}
