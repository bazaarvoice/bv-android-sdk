/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

/**
 * @deprecated TODO remove after full BVPixel swap is complete
 *
 * Internal API: Custom schema for recommendation used feature events
 */
class RecommendationUsedFeatureSchema extends UsedFeatureCanonicalSchema {

    private static final String source = "recommendation-mob";
    private static final String bvProduct = "Recommendations";

    public RecommendationUsedFeatureSchema(Feature feature, String productId, ReportingGroup reportingGroup, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, RecommendationAttributesPartialSchema recommendationAttributesPartialSchema) {
        super(source, feature.toString(), bvProduct);
        addProductId(productId);
        addPartialSchema(magpieMobileAppPartialSchema);
        addPartialSchema(recommendationAttributesPartialSchema);
        if (reportingGroup != null) {
            addComponent(reportingGroup.toString());
        }
    }

}
