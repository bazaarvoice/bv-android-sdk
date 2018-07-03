/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.StringDef;

import com.gimbal.android.Visit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Deprecated
class LocationAnalyticsManager {

    @StringDef({ENTRY, EXIT})
    @Retention(RetentionPolicy.SOURCE)
    @interface TRANSITION {
    }

    static final String ENTRY = "Entry";
    static final String EXIT = "Exit";

    static void sendLocationEventForGimbalVisit(Visit visit, @TRANSITION String transitionState) {
        if (visit == null || visit.getPlace() == null) {
            return;
        }
        String id = visit.getPlace().getAttributes().getValue(PlaceAttribute.Id.getKey());

        long seconds = (visit.getDepartureTimeInMillis() > 0) ? visit.getDwellTimeInMillis() / 1000 : 0;

        BVPixel bvPixel = BVSDK.getInstance().getBvPixel();
        bvPixel.track(new BVLocationEvent(transitionState, id, seconds));
    }
}