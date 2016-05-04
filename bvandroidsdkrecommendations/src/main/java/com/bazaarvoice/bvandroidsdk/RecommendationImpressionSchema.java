/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

class RecommendationImpressionSchema extends ImpressionSchema {

    private static final String source = "recommendation-mob";
    private static final String eventType = "Recommendation";
    private static final String bvProduct = "Recommendations";

    public RecommendationImpressionSchema(String productId, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, RecommendationAttributesPartialSchema recommendationAttributesPartialSchema) {
        super(magpieMobileAppPartialSchema, productId, bvProduct, eventType, source);
        addPartialSchema(recommendationAttributesPartialSchema);
    }

}
