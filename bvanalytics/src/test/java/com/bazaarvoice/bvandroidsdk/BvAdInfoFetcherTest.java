package com.bazaarvoice.bvandroidsdk;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BvAdInfoFetcherTest {
  @Mock BVAdvertisingId.BvAdInfoFetcher fetcher;
  private BvStubData stubData;

  @Before
  public void setup() {
    initMocks(this);
    stubData = new BvStubData();
  }

  @Test
  public void shouldReturnNonTrackingForNullAdInfo() throws Exception {
    when(fetcher.getAdInfo()).thenReturn(null);
    BVAdvertisingId.BvAdIdDecider decider = new BVAdvertisingId.BvAdIdDecider(fetcher);
    String actualAdId = getAdIdFuture(decider).get(5, TimeUnit.SECONDS);
    assertEquals(BVEventValues.NONTRACKING_TOKEN, actualAdId);
  }

  @Test
  public void shouldReturnNonTrackingForLimitEnabled() throws Exception {
    AdvertisingIdClient.Info info = stubData.getAdInfo(true);
    when(fetcher.getAdInfo()).thenReturn(info);
    BVAdvertisingId.BvAdIdDecider decider = new BVAdvertisingId.BvAdIdDecider(fetcher);
    String actualAdId = getAdIdFuture(decider).get(5, TimeUnit.SECONDS);
    assertEquals(BVEventValues.NONTRACKING_TOKEN, actualAdId);
  }

  @Test
  public void shouldReturnAdId() throws Exception {
    AdvertisingIdClient.Info info = stubData.getAdInfo(false);
    when(fetcher.getAdInfo()).thenReturn(info);
    BVAdvertisingId.BvAdIdDecider decider = new BVAdvertisingId.BvAdIdDecider(fetcher);
    String actualAdId = getAdIdFuture(decider).get(5, TimeUnit.SECONDS);
    assertEquals(stubData.getAdId(), actualAdId);
  }

  private Future<String> getAdIdFuture(BVAdvertisingId.BvAdIdDecider decider) {
    ExecutorService testExecutor = Executors.newSingleThreadExecutor();
    return testExecutor.submit(new TestAdIdCall(decider));
  }

  private static class TestAdIdCall implements Callable<String> {
    private final BVAdvertisingId.BvAdIdDecider decider;

    public TestAdIdCall(BVAdvertisingId.BvAdIdDecider decider) {
      this.decider = decider;
    }

    @Override
    public String call() throws Exception {
      return decider.getAdId();
    }
  }
}
