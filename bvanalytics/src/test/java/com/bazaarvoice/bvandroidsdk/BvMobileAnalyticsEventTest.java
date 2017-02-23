package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;

import java.util.Map;

public class BvMobileAnalyticsEventTest extends BvEventPartTest {
  @Test
  public void shouldSetMobileSource() throws Exception {
    checkMapContains(BVEventKeys.MobileEvent.MOBILE_SOURCE, BVEventValues.MOBILE_SOURCE);
  }

  @Test
  public void shouldSetAdvertisingId() throws Exception {
    checkMapContains(BVEventKeys.MobileEvent.ADVERTISING_ID, stubData.getAdId());
  }

  @Override
  public Map<String, Object> getSubjectMap() {
    final BVMobileAnalyticsEvent subject = stubData.getMobileAnalyticsEvent();
    return subject.toRaw();
  }
}
