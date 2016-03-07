/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.Utils.mapPutSafe;

class AdLifecyclePartialSchema extends BvPartialSchema {

    private static final String KEY_AD_UNIT_ID = "adUnitId";
    private static final String KEY_LOAD_ID = "loadId";
    private static final String KEY_AD_TYPE = "adType";
    private static final String KEY_AD_STATE = "adState";
    private static final String KEY_BRANDS = "brands";
    private static final String KEY_INTERESTS = "interests";
    /**
     * Unique identifier for the advertising unit
     */
    private String adUnitId;

    /**
     * Unique identifier for the current ad load instance. Used to track Lifecycle events on the same ad instance.
     */
    private String loadId;

    /**
     * Type of advertisement
     */
    private AdTracker.AdTrackerType adType;

    /**
     * State of the ad in the ad lifecycle. Ex: Requested, Loaded, Shown, Dismissed, etc
     */
    private String adState;

    /**
     * Brand keywords used to target this ad request, which comes from the user profile API
     */
    private String brands;

    /**
     * Interest keywords used to target this ad request, which comes from the user profile API
     */
    private String interests;

    private AdLifecyclePartialSchema(Builder builder) {
        adUnitId = builder.adUnitId;
        loadId = builder.loadId;
        adType = builder.adType;
        adState = builder.adState;
        brands = builder.brands;
        interests = builder.interests;
    }

    @Override
    public void addPartialData(Map<String, Object> dataMap) {
        mapPutSafe(dataMap, KEY_AD_UNIT_ID, adUnitId);
        mapPutSafe(dataMap, KEY_LOAD_ID, loadId);
        if (adType != null) {
            mapPutSafe(dataMap, KEY_AD_TYPE, adType.getValue());
        }
        mapPutSafe(dataMap, KEY_AD_STATE, adState);
        mapPutSafe(dataMap, KEY_BRANDS, brands);
        mapPutSafe(dataMap, KEY_INTERESTS, interests);
    }


    public static final class Builder {
        private String adUnitId;
        private String loadId;
        private AdTracker.AdTrackerType adType;
        private String adState;
        private String brands;
        private String interests;

        public Builder(String adUnitId, AdTracker.AdTrackerType adType, String adState) {
            this.adUnitId = adUnitId;
            this.adType = adType;
            this.adState = adState;
        }

        public Builder loadId(String loadId) {
            this.loadId = loadId;
            return this;
        }

        public Builder brands(String val) {
            brands = val;
            return this;
        }

        public Builder interests(String val) {
            interests = val;
            return this;
        }

        public AdLifecyclePartialSchema build() {
            return new AdLifecyclePartialSchema(this);
        }
    }
}

