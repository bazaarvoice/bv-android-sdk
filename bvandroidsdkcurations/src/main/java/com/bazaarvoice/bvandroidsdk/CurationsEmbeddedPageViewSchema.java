package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 4/8/16.
 */
class CurationsEmbeddedPageViewSchema extends EmbeddedPageViewSchema {

    private static final String bvProduct = "Curations";
    private static final String source = "native-mobile-sdk";
    private static final String productId = "productId";

    public CurationsEmbeddedPageViewSchema(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, String externalId, ReportingGroup reportingGroup)
    {
        super(magpieMobileAppPartialSchema, reportingGroup, bvProduct, source);
        addKeyVal(productId, externalId);
    }
}
