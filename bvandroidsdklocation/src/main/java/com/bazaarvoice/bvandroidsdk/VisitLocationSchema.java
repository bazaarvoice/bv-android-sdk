/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

class VisitLocationSchema extends BvAnalyticsSchema {

    private static final String eventClass = "Location";
    private static final String eventType = "Visit";
    private static final String eventSource = "native-mobile-sdk";
    private static final String KEY_LOCATION_ID = "locationId";
    private static final String KEY_LOCATION_TRANSITION = "transition";
    private static final String KEY_LOCATION_DURATION = "durationSecs";

    public VisitLocationSchema(MagpieMobileAppPartialSchema partialSchema, String locationId, TransitionState state, long durationSeconds) {
        super(VisitLocationSchema.eventClass, VisitLocationSchema.eventType, VisitLocationSchema.eventSource);
        addPartialSchema(partialSchema);
        addKeyVal(KEY_LOCATION_ID, locationId);
        addKeyVal(KEY_LOCATION_TRANSITION, state.getValue());
        if (durationSeconds > 0) {
            addKeyVal(KEY_LOCATION_DURATION, durationSeconds);
        }
    }

    enum TransitionState {
        Entry("Entry"),
        Exit("Exit");

        private final String value;
        TransitionState(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }
}