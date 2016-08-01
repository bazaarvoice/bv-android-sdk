/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

class ProductPageViewSchema extends BvAnalyticsSchema {
    private static final String eventClass = "PageView";
    private static final String eventType = "Product";
    private static final String source = "native-mobile-sdk";

    private static final String KEY_PRODUCT_ID = "productId";
    private static final String KEY_CATEGORY_ID = "categoryId";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_BV_PRODUCT = "bvProduct";
    private static final String KEY_REVIEWS = "numReviews";
    private static final String KEY_QUESTIONS = "numQuestions";
    private static final String KEY_ANSWERS = "numAnswers";

    private ProductPageViewSchema(Builder builder) {
        super(eventClass, eventType, source);
        addKeyVal(KEY_PRODUCT_ID, builder.productId);
        addKeyVal(KEY_CATEGORY_ID, builder.categoryId);
        addKeyVal(KEY_BRAND, builder.brand);
        addKeyVal(KEY_BV_PRODUCT, builder.magpieBvProduct);
        addKeyVal(KEY_REVIEWS, builder.numReviews);
        addKeyVal(KEY_QUESTIONS, builder.numQuestions);
        addKeyVal(KEY_ANSWERS, builder.numAnswers);
        addPartialSchema(builder.magpieMobileAppPartialSchema);
    }

    @Deprecated
    public ProductPageViewSchema(String productId, String categoryId, String brand, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema) {
        super(eventClass, eventType, source);
        addKeyVal(KEY_PRODUCT_ID, productId);
        addKeyVal(KEY_CATEGORY_ID, categoryId);
        addKeyVal(KEY_BRAND, brand);
        addPartialSchema(magpieMobileAppPartialSchema);
    }

    public static class Builder {
        // Non-Optional
        String productId;
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema;

        // Optional
        String categoryId;
        String brand;
        int numReviews;
        int numQuestions;
        int numAnswers;
        MagpieBvProduct magpieBvProduct;

        public Builder(String productId, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema) {
            this.productId = productId;
            this.magpieMobileAppPartialSchema = magpieMobileAppPartialSchema;
        }

        public Builder categoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder numReviews(int numReviews) {
            this.numReviews = numReviews;
            return this;
        }

        public Builder numQuestions(int numQuestions) {
            this.numQuestions = numQuestions;
            return this;
        }

        public Builder numAnswers(int numAnswers) {
            this.numAnswers = numAnswers;
            return this;
        }

        public Builder magpieBvProduct(MagpieBvProduct magpieBvProduct) {
            this.magpieBvProduct = magpieBvProduct;
            return this;
        }

        public ProductPageViewSchema build() {
            return new ProductPageViewSchema(this);
        }
    }
}
