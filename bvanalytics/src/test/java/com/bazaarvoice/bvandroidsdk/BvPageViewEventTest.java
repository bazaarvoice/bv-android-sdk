package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class BvPageViewEventTest extends BvEventPartTest {
  @Test
  public void shouldHaveClassPageView() throws Exception {
    checkMapContains(BVEventKeys.Event.CLASS, BVEventValues.BVEventClass.PAGE_VIEW.toString());
  }

  @Test
  public void shouldHaveTypeProduct() throws Exception {
    checkMapContains(BVEventKeys.Event.TYPE, BVEventValues.BVEventType.PRODUCT.toString());
  }

  @Test
  public void shouldHaveProductId() throws Exception {
    checkMapContains(BVEventKeys.PageViewEvent.PRODUCT_ID, stubData.getProductId());
  }

  @Test
  public void shouldHaveCategoryId() throws Exception {
    checkMapContains(BVEventKeys.PageViewEvent.CATEGORY_ID, stubData.getCategoryId());
  }

  @Test
  public void shouldHaveBvProductType() throws Exception {
    checkMapContains(BVEventKeys.PageViewEvent.BV_PRODUCT_TYPE, stubData.getBvProductTypeReviews().toString());
  }

  @Test
  public void shouldNotRequireCategoryId() {
    try {
      new BVPageViewEvent(
          stubData.getProductId(),
          stubData.getBvProductTypeReviews(),
          null);
    } catch (IllegalStateException expected) {
      fail();
    }
  }

  @Test
  public void shouldNotContainCategoryIdWhenNull() {
    BVPageViewEvent event = new BVPageViewEvent(
        stubData.getProductId(),
        stubData.getBvProductTypeReviews(),
        null);
    event.setBvMobileParams(stubData.getBvMobileParams());

    Map<String, Object> map = event.toRaw();
    assertFalse(map.containsKey(BVEventKeys.PageViewEvent.CATEGORY_ID));
  }

  @Override
  Map<String, Object> getSubjectMap() throws Exception {
    final BVPageViewEvent subject = stubData.getReviewsProductPageViewSchema();
    return subject.toRaw();
  }
}
