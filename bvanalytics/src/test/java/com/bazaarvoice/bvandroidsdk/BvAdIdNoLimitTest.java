package com.bazaarvoice.bvandroidsdk;

import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {BaseShadows.ShadowAdIdClientNoLimit.class})
public class BvAdIdNoLimitTest {
  private static final String UUID_PATTERN = "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}";
  private BvStubData stubData;

  @Before
  public void setup() {
    stubData = new BvStubData();
  }

  @Test
  public void shouldReturnAdId() throws Exception {
    String actualAdId = stubData.getAdIdFuture(ApplicationProvider.getApplicationContext()).get();
    Pattern adIdPattern = Pattern.compile(UUID_PATTERN);
    Matcher adIdMatcher = adIdPattern.matcher(actualAdId);
    assertTrue("Ad id did not match expected pattern", adIdMatcher.matches());
  }

  @Test
  public void shouldReturnAdInfo() {
    BVAdvertisingId.BvAdInfoFetcher fetcher = new BVAdvertisingId.BvAdInfoFetcher(ApplicationProvider.getApplicationContext());
    AdvertisingIdClient.Info info = fetcher.getAdInfo();
    assertNotNull(info);
  }
}
