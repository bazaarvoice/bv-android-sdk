package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 4/8/16.
 */
class CurationsImpressionSchema extends ImpressionSchema {

    private static final String bvProduct = "Curations";
    private static final String eventType = "UGC";
    private static final String source = "native-mobile-sdk";

    private static final String CONTENT_TYPE_KEY = "contentType";
    private static final String contentType = "socialPost";
    private static final String CHANNEL_KEY = "syndicationSource";
    private static final String CONTENT_ID_KEY = "contentId";

    public CurationsImpressionSchema(CurationsFeedItem item, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema){
        super(magpieMobileAppPartialSchema, item.externalIdInQuery, bvProduct, eventType, source);
        addKeyVal(CONTENT_TYPE_KEY, contentType);
        addKeyVal(CHANNEL_KEY, item.channel);
        addKeyVal(CONTENT_ID_KEY, item.id);
    }
}
