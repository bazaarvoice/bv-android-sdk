package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;

public class BVRecomendationImpressionEvent extends BVMobileAnalyticsEvent {

    private static final String bvProduct = "Recommendations";
    private static final String KEY_VISIBLE = "visible";
    private static final String KEY_BV_PRODUCT = "bvProduct";
    private static final String KEY_PRODUCT_ID = "productId";
    private static final boolean VALUE_VISIBLE = true;
    private final String productId;

    public BVRecomendationImpressionEvent(String productId) {
        super(BVEventValues.BVEventClass.IMPRESSION, BVEventValues.BVEventType.PERSONALIZATION);
        this.productId = productId;
    }

    @Override
    public Map<String, Object> toRaw() {
        Map<String, Object> map = super.toRaw();
        mapPutSafe(map, KEY_VISIBLE, VALUE_VISIBLE);
        mapPutSafe(map, KEY_PRODUCT_ID, productId);
        mapPutSafe(map, KEY_BV_PRODUCT, bvProduct);
        return map;
    }
}
