package com.bazaarvoice.bvandroidsdk;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BvTransactionItemTest {

  private BvStubData stubData;

  @Before
  public void setup() {
    stubData = new BvStubData();
  }

  @Test
  public void shouldFormEvent() throws Exception {
    final BVTransactionItem transactionItem = stubData.getTransactionItem(
        stubData.getSku(),
        stubData.getTransactionItemName(),
        stubData.getImageUrl(),
        stubData.getSmallPrice(),
        stubData.getTransactionQuantity());
    assertEquals(stubData.getCategoryId(), transactionItem.getCategory());
    assertEquals(stubData.getSku(), transactionItem.getSku());
    assertEquals(stubData.getImageUrl(), transactionItem.getImageUrl());
    assertEquals(stubData.getSmallPrice(), transactionItem.getPrice(), 0);
    assertEquals(stubData.getTransactionQuantity(), transactionItem.getQuantity());
  }

}
