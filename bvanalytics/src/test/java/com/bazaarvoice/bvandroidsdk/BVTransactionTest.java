package com.bazaarvoice.bvandroidsdk;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BVTransactionTest {

  private BvStubData stubData;

  @Before
  public void setup() {
    stubData = new BvStubData();
  }

  @Test
  public void shouldFormEvent() throws Exception {
    final List<BVTransactionItem> items = stubData.getTransactionItems(2);
    final Map<String, String> otherParams = stubData.getOtherParams(2);
    final BVTransaction subject = stubData.getTransaction(items, otherParams);
    assertEquals(2, subject.getItems().size());
    assertEquals(2, subject.getOtherParams().size());
  }
}
