/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.gimbal.android.Visit;

class LocationAnalyticsManager {

    static final void sendLocationEventForGimbalVisit(Visit visit) {

        String id = visit.getPlace().getAttributes().getValue(PlaceAttribute.Id.getKey());
        VisitLocationSchema.TransitionState state = (visit.getDepartureTimeInMillis() > 0) ? VisitLocationSchema.TransitionState.Exit : VisitLocationSchema.TransitionState.Entry;

        long seconds = (visit.getDepartureTimeInMillis() > 0) ? visit.getDwellTimeInMillis() / 1000 : 0;

        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();

        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        VisitLocationSchema visitLocationSchema = new VisitLocationSchema(magpieMobileAppPartialSchema, id, state, seconds);
        analyticsManager.enqueueEvent(visitLocationSchema);
    }
}