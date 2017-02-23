package com.bazaarvoice.bvandroidsdk;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BvTestUtil.checkMapContains;

public class BvFeatureUsedEventTest {

  private BvStubData stubData;

  @Before
  public void setup() {
    stubData = new BvStubData();
  }

  @Test
  public void writeReviewFeatureUsedEventMinimumFields() throws Exception {
    final BVFeatureUsedEvent subject = stubData.getWriteReviewFeatureUsedSchema(
        stubData.getProductId(),
        stubData.getAdditionalParams());
    final Map<String, Object> subjectMap = subject.toRaw();
    checkMapContains(subjectMap, BVEventKeys.Event.CLASS, BVEventValues.BVEventClass.FEATURE.toString());
    checkMapContains(subjectMap, BVEventKeys.Event.TYPE, BVEventValues.BVEventType.USED.toString());
    checkMapContains(subjectMap, BVEventKeys.Event.SOURCE, BVEventValues.BVEventSource.NATIVE_MOBILE_SDK.toString());
    checkMapContains(subjectMap, BVEventKeys.FeatureUsedEvent.PRODUCT_ID, stubData.getProductId());
    checkMapContains(subjectMap, BVEventKeys.FeatureUsedEvent.BV_PRODUCT_TYPE, BVEventValues.BVProductType.CONVERSATIONS_REVIEWS.toString());
    checkMapContains(subjectMap, BVEventKeys.FeatureUsedEvent.BV_FEATURE_TYPE, BVEventValues.BVFeatureUsedEventType.WRITE_REVIEW.toString());
  }
}
