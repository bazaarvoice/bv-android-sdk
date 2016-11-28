/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

class RecommendationImpressionSchema extends BvAnalyticsSchema {

    private static final String source = "recommendation-mob";
    private static final String eventClass = "Impression";
    private static final String eventType = "Recommendation";
    private static final String bvProduct = "Recommendations";
    private static final String KEY_VISIBLE = "visible";
    private static final boolean VALUE_VISIBLE = true;
    private static final String KEY_BV_PRODUCT = "bvProduct";
    private static final String KEY_PRODUCT_ID = "productId";

    public RecommendationImpressionSchema(String productId, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, RecommendationAttributesPartialSchema recommendationAttributesPartialSchema) {
        super(eventClass, eventType, source);
        addPartialSchema(recommendationAttributesPartialSchema);
        addKeyVal(KEY_VISIBLE, VALUE_VISIBLE);
        addKeyVal(KEY_PRODUCT_ID, productId);
        addPartialSchema(magpieMobileAppPartialSchema);
        addKeyVal(KEY_BV_PRODUCT, bvProduct);
    }

}
