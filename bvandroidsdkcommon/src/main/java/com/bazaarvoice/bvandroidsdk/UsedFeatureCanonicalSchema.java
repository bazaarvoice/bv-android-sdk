/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

/**
 * Internal API: Canonical schema for used feature events
 */
abstract class UsedFeatureCanonicalSchema extends BvAnalyticsSchema {

    private static final String KEY_NAME = "name";
    private static final String KEY_DETAIL1 = "detail1";
    private static final String KEY_PRODUCT_ID = "productId";
    private static final String KEY_CATEGORY_ID = "categoryId";
    private static final String KEY_BV_PRODUCT = "bvProduct";
    private static final String KEY_COMPONENT = "component";
    private static final String eventClass = "Feature";
    private static final String eventType = "Used";

    public UsedFeatureCanonicalSchema(String source, String name, String bvProduct) {
        super(eventClass, eventType, source);
        addKeyVal(KEY_NAME, name);
        addKeyVal(KEY_BV_PRODUCT, bvProduct);
    }

    protected void addDetail1(String detail1) {
        addKeyVal(KEY_DETAIL1, detail1);
    }

    protected void addProductId(String productId) {
        addKeyVal(KEY_PRODUCT_ID, productId);
    }

    protected void addCategoryId(String categoryId) {
        addKeyVal(KEY_CATEGORY_ID, categoryId);
    }

    protected void addComponent(String component) {
        addKeyVal(KEY_COMPONENT, component);
    }
}