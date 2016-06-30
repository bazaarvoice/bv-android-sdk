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

    public ProductPageViewSchema(String productId, String categoryId, String brand, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema) {
        super(eventClass, eventType, source);
        addKeyVal(KEY_PRODUCT_ID, productId);
        addKeyVal(KEY_CATEGORY_ID, categoryId);
        addKeyVal(KEY_BRAND, brand);
        addPartialSchema(magpieMobileAppPartialSchema);
    }
}
