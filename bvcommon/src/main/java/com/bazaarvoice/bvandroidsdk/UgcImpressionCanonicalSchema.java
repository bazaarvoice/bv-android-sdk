package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 4/8/16.
 */
abstract class UgcImpressionCanonicalSchema extends BvAnalyticsSchema{

    private static final String eventClass = "Impression";
    private static final String eventType = "UGC";
    private static final String KEY_BV_PRODUCT = "bvProduct";
    private static final String KEY_PRODUCT_ID = "productId";
    private static final String KEY_CONTENT_ID = "contentId";
    private static final String KEY_CONTENT_TYPE = "contentType";

    public UgcImpressionCanonicalSchema(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, String productId, String contentId, String contentType, String bvProduct, String source){
        super(eventClass, eventType, source);
        addKeyVal(KEY_PRODUCT_ID, productId);
        addPartialSchema(magpieMobileAppPartialSchema);
        addKeyVal(KEY_BV_PRODUCT, bvProduct);
        addKeyVal(KEY_CONTENT_ID, contentId);
        addKeyVal(KEY_CONTENT_TYPE, contentType);
    }
}
