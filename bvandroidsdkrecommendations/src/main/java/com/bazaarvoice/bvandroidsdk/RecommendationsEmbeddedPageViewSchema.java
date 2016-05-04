package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 4/8/16.
 */
public class RecommendationsEmbeddedPageViewSchema extends EmbeddedPageViewSchema {

    private static final String bvProduct = "Recommendations";
    private static final String KEY_BRAND = "brand";
    private static final String source = "recommendation-mob";

    public RecommendationsEmbeddedPageViewSchema(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, ReportingGroup reportingGroup, String brand){
        super(magpieMobileAppPartialSchema, reportingGroup, bvProduct, source);
        addKeyVal(KEY_BRAND, brand);
    }
}
