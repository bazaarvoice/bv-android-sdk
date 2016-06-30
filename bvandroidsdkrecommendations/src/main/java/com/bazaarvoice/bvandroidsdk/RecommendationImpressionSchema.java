/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

class RecommendationImpressionSchema extends ImpressionSchema {

    private static final String source = "recommendation-mob";
    private static final String eventType = "Recommendation";
    private static final String bvProduct = "Recommendations";
    private static final String KEY_VISIBLE = "visible";
    private static final boolean VALUE_VISIBLE = true;

    public RecommendationImpressionSchema(String productId, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, RecommendationAttributesPartialSchema recommendationAttributesPartialSchema) {
        super(magpieMobileAppPartialSchema, productId, bvProduct, eventType, source);
        addPartialSchema(recommendationAttributesPartialSchema);
        addKeyVal(KEY_VISIBLE, VALUE_VISIBLE);
    }

}
