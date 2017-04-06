package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;

import java.util.Map;

public class BVViewedCgcEventTest extends BvEventPartTest {

  @Test
  public void shouldHaveClassFeature() throws Exception {
    checkMapContains(BVEventKeys.Event.CLASS, BVEventValues.BVEventClass.FEATURE.toString());
  }

  @Test
  public void shouldHaveTypeViewedCGC() throws Exception {
    checkMapContains(BVEventKeys.Event.TYPE, BVEventValues.BVEventType.VIEWED_CGC.toString());
  }

  @Test
  public void shouldHaveRootCategoryId() throws Exception {
    checkMapContains(BVEventKeys.ViewedCgcEvent.ROOT_CATEGORY_ID, stubData.getRootCategoryId());
  }

  @Test
  public void shouldHaveCategoryId() throws Exception {
    checkMapContains(BVEventKeys.ViewedCgcEvent.CATEGORY_ID, stubData.getCategoryId());
  }

  @Test
  public void shouldHaveBrand() throws Exception {
    checkMapContains(BVEventKeys.ViewedCgcEvent.BRAND, stubData.getBrand());
  }

  @Override
  Map<String, Object> getSubjectMap() throws Exception {
    return stubData.getViewedCgcEvent().toRaw();
  }
}
