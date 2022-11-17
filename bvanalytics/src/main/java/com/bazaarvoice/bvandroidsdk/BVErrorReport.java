package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.FeatureUsedEvent.DETAIL_1;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.FeatureUsedEvent.DETAIL_2;

/**
 * A report of a fatal error that occurred while using the BVSDK. Will track the client, module, class
 * and line number.
 */
class BVErrorReport extends BVPiiEvent {

    private final String bvproduct;
    private final String detail1;
    private final String detail2;

    BVErrorReport(BVEventValues.BVProductType productType, String requestType, Exception exception) {
        super(BVEventValues.BVEventClass.ERROR, BVEventValues.BVEventType.RECORD);
        this.detail1 = exception.getMessage();
        this.detail2 = requestType;
        this.bvproduct = productType.toString();
    }

    BVErrorReport(BVEventValues.BVProductType productType, String requestType, String detailedMessage) {
        super(BVEventValues.BVEventClass.ERROR, BVEventValues.BVEventType.RECORD);
        this.detail1 = detailedMessage;
        this.detail2 = requestType;
        this.bvproduct = productType.toString();
    }

    @Override
    public Map<String, Object> toRaw() {
        Map<String, Object> analyticsMap = super.toRaw();
        mapPutSafe(analyticsMap, DETAIL_1, detail1);
        mapPutSafe(analyticsMap, DETAIL_2, detail2);
        mapPutSafe(analyticsMap, "bvproduct", bvproduct);
        mapPutSafe(analyticsMap, "name", "Error");
        return analyticsMap;
    }

    @Override
    protected Map<String, Object> getPiiUnrelatedParams() {
        return null;
    }

    @Override
    protected void getPIIEvent(Map<String, Object> piiParams) {

    }


}

