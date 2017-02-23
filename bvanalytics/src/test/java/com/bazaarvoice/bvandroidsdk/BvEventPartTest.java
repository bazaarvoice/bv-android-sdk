package com.bazaarvoice.bvandroidsdk;

import org.junit.Before;

import java.util.Map;

public abstract class BvEventPartTest {
  protected BvStubData stubData;

  @Before
  public void setup() {
    stubData = new BvStubData();
  }

  protected void checkMapContains(String expectedKey, String expectedValue) throws Exception {
    BvTestUtil.checkMapContains(getSubjectMap(), expectedKey, expectedValue);
  }

  protected void checkMapContains(String expectedKey, Object expectedValue) throws Exception {
    BvTestUtil.checkMapContains(getSubjectMap(), expectedKey, expectedValue);
  }

  abstract Map<String, Object> getSubjectMap() throws Exception;
}
