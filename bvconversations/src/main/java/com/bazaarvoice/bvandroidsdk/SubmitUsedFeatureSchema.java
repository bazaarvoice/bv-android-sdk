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
    private static final String bvProduct = "RatingsAndReviews";

    private SubmitUsedFeatureSchema(Builder builder) {
        super(source, builder.submitFeature.toString(), bvProduct);
        addCategoryId(builder.categoryId);
        addProductId(builder.productId);
        addContentId(builder.contentId);
        addContentType(builder.contentType);
        addDetail1(builder.detail1);
        addKeyVal(KEY_FINGERPRINT, builder.fingerprint);
        addPartialSchema(builder.magpieMobileAppPartialSchema);
    }

    public enum SubmitFeature {
        ANSWER("Answer"), COMMENT("Comment"), STORY_COMMENT("StoryComment"),
        FEEDBACK("Feedback"), ASK("Ask"), WRITE("Write"),
        STORY("Story"), PHOTO("Photo");

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
        private String contentId;
        private String contentType;
        private String detail1;
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

        public Builder contentId(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder detail1(String detail1) {
            this.detail1 = detail1;
            return this;
        }

        public SubmitUsedFeatureSchema build() {
            return new SubmitUsedFeatureSchema(this);
        }
    }

}
