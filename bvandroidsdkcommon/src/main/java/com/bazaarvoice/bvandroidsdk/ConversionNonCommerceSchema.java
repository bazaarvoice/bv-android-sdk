package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

/**
 * Created by Bazaarvoice on 3/30/16.
 */
class ConversionNonCommerceSchema extends BvAnalyticsSchema {

    private static final String eventClass = "Conversion";
    private static final String source = "native-mobile-sdk";
    private static final String loadId = "loadId";
    private static final String hadPII = "hadPII";
    private static final String value = "value";
    private static final String label = "label";

    private Map<String,Object> pIIEvent;

    ConversionNonCommerceSchema(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, Conversion conversion){
        super(eventClass, conversion.getType(), source);
        addPartialSchema(magpieMobileAppPartialSchema);
        addKeyVal(loadId, ConversionUtils.generateLoadId());
        addKeyVal(value, conversion.getValue());
        addKeyVal(label, conversion.getLabel());

        boolean hadPII = false;

        if (conversion.getOtherParams() != null){
            Map<String, Object> nonPIIParams = ConversionUtils.getNonPIIParams(conversion.getOtherParams());
            for (String key : nonPIIParams.keySet()){
                addKeyVal(key, nonPIIParams.get(key));
            }
            hadPII = nonPIIParams.size() != conversion.getOtherParams().size();
        }

        if (hadPII){
            addKeyVal(ConversionNonCommerceSchema.hadPII, "true");
            this.pIIEvent = ConversionUtils.generatePIIEvent(this, conversion.getOtherParams());
        }
    }

    public Map<String, Object> getPIIEvent() {
        return pIIEvent;
    }
}
