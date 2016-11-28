/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.gimbal.android.Visit;

class LocationAnalyticsManager {

    static void sendLocationEventForGimbalVisit(Visit visit, VisitLocationSchema.TransitionState transitionState) {
        if (visit == null || visit.getPlace() == null) {
            return;
        }
        String id = visit.getPlace().getAttributes().getValue(PlaceAttribute.Id.getKey());

        long seconds = (visit.getDepartureTimeInMillis() > 0) ? visit.getDwellTimeInMillis() / 1000 : 0;

        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();

        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        VisitLocationSchema visitLocationSchema = new VisitLocationSchema(magpieMobileAppPartialSchema, id, transitionState, seconds);
        analyticsManager.enqueueEvent(visitLocationSchema);
    }
}