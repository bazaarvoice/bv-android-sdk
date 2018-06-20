package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static junit.framework.Assert.assertFalse;

public class BVLocationEventTest extends BvEventPartTest {

    String locationId = UUID.randomUUID().toString();
    String transition = "Entry";
    long duration = 1;

    @Test
    public void shouldHaveClassLocation() throws Exception{
        checkMapContains(BVEventKeys.Event.CLASS, BVEventValues.BVEventClass.LOCATION.toString());
    }

    @Test
    public void shouldHaveTypeLocation() throws Exception {
        checkMapContains(BVEventKeys.Event.TYPE, BVEventValues.BVProductType.VISIT.toString());
    }

    @Test
    public void locationEventShouldHaveTransition() throws Exception {
        checkMapContains(BVEventKeys.Location.TRANSITION, transition);
    }

    @Test
    public void locationEventShouldHaveLocationId() throws Exception {
        checkMapContains(BVEventKeys.Location.LOCATION_ID, locationId);
    }

    @Test
    public void locationEventIncludesDurationIfGreaterThanZero() throws Exception {
        checkMapContains(BVEventKeys.Location.DURATION, String.valueOf(duration));
    }

    @Test
    public void locationEventExcludesDurationIfLessThanOne() {
        duration = 0;
        assertFalse(getSubjectMap().containsKey(BVEventKeys.Location.DURATION));
    }

    @Override
    Map<String, Object> getSubjectMap(){
        final BVLocationEvent subject = stubData.getLocationEventStub(transition, locationId, duration);
        subject.setBvMobileParams(stubData.getBvMobileParams());
        return subject.toRaw();
    }
}
