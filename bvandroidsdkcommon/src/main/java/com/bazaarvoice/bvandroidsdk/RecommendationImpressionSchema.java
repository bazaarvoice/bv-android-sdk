/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

class RecommendationImpressionSchema extends BvAnalyticsSchema {

    private static final String KEY_PRODUCT_ID = "productId";
    private static final String KEY_BV_PRODUCT = "bvProduct";
    private static final String eventClass = "Impression";
    private static final String eventType = "Recommendation";
    private static final String source = "recommendation-mob";

    private static final String bvProduct = "Recommendations";

    public RecommendationImpressionSchema(String productId, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, RecommendationAttributesPartialSchema recommendationAttributesPartialSchema) {
        super(eventClass, eventType, source);
        addKeyVal(KEY_PRODUCT_ID, productId);
        addKeyVal(KEY_BV_PRODUCT, bvProduct);
        addPartialSchema(magpieMobileAppPartialSchema);
        addPartialSchema(recommendationAttributesPartialSchema);
    }

}
