package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 4/8/16.
 */
class ImpressionSchema extends BvAnalyticsSchema{

    private static final String eventClass = "Impression";
    private static final String KEY_BV_PRODUCT = "bvProduct";
    private static final String KEY_PRODUCT_ID = "productId";

    public ImpressionSchema(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, String productId, String bvProduct, String eventType, String source){
        super(eventClass, eventType, source);
        addKeyVal(KEY_PRODUCT_ID, productId);
        addPartialSchema(magpieMobileAppPartialSchema);
        addKeyVal(KEY_BV_PRODUCT, bvProduct);
    }
}
