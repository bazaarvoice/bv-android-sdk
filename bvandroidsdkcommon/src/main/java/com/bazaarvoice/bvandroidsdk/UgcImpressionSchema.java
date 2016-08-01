/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

@Deprecated
class UgcImpressionSchema extends ImpressionSchema {

    private static final String eventType = "UGC";
    private static final String source = "native-mobile-sdk";

    @Deprecated
    public UgcImpressionSchema(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, String productId, String bvProduct) {
        super(magpieMobileAppPartialSchema, productId, bvProduct, eventType, source);
    }
}
