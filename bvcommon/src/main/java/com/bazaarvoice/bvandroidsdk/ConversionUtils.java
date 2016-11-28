package com.bazaarvoice.bvandroidsdk;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Bazaarvoice on 3/30/16.
 */
class ConversionUtils {

    private static DecimalFormat twoPlaceFormat = new DecimalFormat("0.00");
    private static final String eventClassPII = "PIIConversion";

    private static final Set<String> whitelistParams = new HashSet<String>(Arrays.asList("orderId","affiliation","total","tax"
            ,"shipping","city","state","currency","items","locale","type","label","value","partnerSource","TestCase","TestSession"
            ,"dc","ref"));

    static String formatDouble(double num) {
        return twoPlaceFormat.format(num);
    }

    static Map<String, Object> getNonPIIParams(Map<String, Object>params){

        Map<String, Object> nonPII = new HashMap<>();

        if (params != null){
            for (String key : params.keySet()){
                if (whitelistParams.contains(key)){
                    nonPII.put(key,params.get(key));
                }
            }
        }

        return nonPII;
    }

    static String generateLoadId() {
        final int length = 20;
        StringBuilder buf = new StringBuilder(length);
        while (buf.length() < length) {
            buf.append(Integer.toHexString((int) (Math.random() * 16)));
        }
        return buf.toString();
    }

    static Map<String, Object> generatePIIEvent(BvAnalyticsSchema schema, Map<String, Object> params){
        Map<String, Object> piiEvent = schema.getDataMap();

        //override current class
        piiEvent.put(BvAnalyticsSchema.KEY_EVENT_CLASS, eventClassPII);
        //all other params including PII
        piiEvent.putAll(params);

        return piiEvent;
    }
}
