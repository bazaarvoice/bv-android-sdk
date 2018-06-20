package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.warnShouldNotBeEmpty;

public class BVLocationEvent extends BVMobileAnalyticsEvent {

    private String transition;
    private String locationId;
    private long duration;

    /**
     *
     * @param transition The transition state. Enter or Exit
     * @param locationId The id of the Place
     * @param duration The length of time in seconds spent in the location
     */
    public BVLocationEvent(@NonNull String transition, @NonNull String locationId, long duration) {
        super(BVEventValues.BVEventClass.LOCATION, BVEventValues.BVEventType.VISIT);
        warnShouldNotBeEmpty("transition", transition);
        this.transition = transition;
        warnShouldNotBeEmpty("locationId", transition);
        this.locationId = locationId;
        this.duration = duration;
    }

    @Override
    public Map<String, Object> toRaw() {
        Map<String, Object> map = super.toRaw();
        mapPutSafe(map, BVEventKeys.Location.LOCATION_ID, locationId);
        mapPutSafe(map, BVEventKeys.Location.TRANSITION, transition);
        if(duration > 0) {
            mapPutSafe(map, BVEventKeys.Location.DURATION, duration);
        }
        return map;
    }
}