package com.bazaarvoice.bvandroidsdk;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class BvPixelTest {
  private BvStubData stubData;
  @Mock
  BVPixelDispatcher dispatcher;

  @Before
  public void setup() {
    initMocks(this);
    stubData = new BvStubData();
  }

  @Test
  public void shouldEnqueueEvent() throws Exception {
    BVAnalyticsEvent event = stubData.getEvent();
    BVPixel bvPixel = new BVPixel(dispatcher);

    bvPixel.track(event);

    verify(dispatcher).enqueueEvent(event);
  }

  @Test
  public void shouldSendPersonalizationImmediately() throws Exception {
    BVPersonalizationEvent event = stubData.getPersonalizationEvent();

    assertTrue(BVPixel.shouldDispatchImmediately(event));
  }

  @Test
  public void shouldSendPageViewImmediately() throws Exception {
    BVPageViewEvent event = stubData.getPageViewEvent();

    assertTrue(BVPixel.shouldDispatchImmediately(event));
  }

  @Test
  public void shouldDispatchADispatchImmediatelyEvent() throws Exception {
    BVPixel bvPixel = new BVPixel(dispatcher);
    BVPersonalizationEvent event = stubData.getPersonalizationEvent();
    BVPageViewEvent event2 = stubData.getPageViewEvent();

    bvPixel.track(event);
    bvPixel.track(event2);

    verify(dispatcher, Mockito.times(2)).dispatchBatchImmediately();
  }

  @Test
  public void shouldBeginScheduleOfDispatching() {
    BVPixel bvPixel = new BVPixel(dispatcher);
    verify(dispatcher).beginDispatchWithDelay();
  }
}
