package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 4/8/16.
 */
class CurationsImpressionSchema extends UgcImpressionCanonicalSchema {

    private static final String bvProduct = "Curations";
    private static final String source = "native-mobile-sdk";

    private static final String contentType = "socialPost";
    private static final String CHANNEL_KEY = "syndicationSource";

    public CurationsImpressionSchema(CurationsFeedItem item, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema){
        super(magpieMobileAppPartialSchema, item.externalIdInQuery, String.valueOf(item.id), contentType, bvProduct, source);
        addKeyVal(CHANNEL_KEY, item.channel);
    }
}
