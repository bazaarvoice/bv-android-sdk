package com.bazaarvoice.bvandroidsdk;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {BaseShadows.ShadowAdIdClientNoLimit.class}, packageName = "com.test.package.name")
public class BvAnalyticsUtilTest {

  @Test
  public void shouldGetCorrectPackageName() {
    String actual = BVAnalyticsUtils.getPackageName(ApplicationProvider.getApplicationContext());
    String expected = "com.test.package.name";
    assertEquals(expected, actual);
  }

  @Test
  public void generateLoadIdShouldBe20AlphanumericCharacters() {
    String actual = BVAnalyticsUtils.generateLoadId();
    Pattern expectedPattern = Pattern.compile("[\\w\\d]{20}");
    Matcher matcher = expectedPattern.matcher(actual);
    assertTrue("Load ID in incorrect format", matcher.find());
  }

}
