package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 4/8/16.
 */
class CurationsUsedFeatureSchema extends UsedFeatureCanonicalSchema {

    private static final String source = "native-mobile-sdk";
    private static final String bvProduct = "Curations";
    private static final String CONTENT_ID_KEY = "detail2";

    public CurationsUsedFeatureSchema(Feature feature, String externalId, String widgetId, ReportingGroup reportingGroup, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema){
        super(source, feature.toString(), bvProduct);
        addComponent(reportingGroup.toString());
        addProductId(externalId);
        addDetail1(widgetId);
    }

    public CurationsUsedFeatureSchema(Feature feature, CurationsFeedItem item, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema){
        super(source, feature.toString(), bvProduct);
        addProductId(item.externalIdInQuery);
        addDetail1(item.channel);
        addKeyVal(CONTENT_ID_KEY, item.id);
    }
}
