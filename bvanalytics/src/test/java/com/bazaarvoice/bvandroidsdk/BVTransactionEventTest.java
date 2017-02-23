package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BVTransactionEventTest extends BvEventPartTest {

  @Test
  public void shouldHaveCity() throws Exception {
    checkMapContains(BVEventKeys.Transaction.CITY, stubData.getTransaction().getCity());
  }

  @Test
  public void shouldHaveCountry() throws Exception {
    checkMapContains(BVEventKeys.Transaction.COUNTRY, stubData.getTransaction().getCountry());
  }

  @Test
  public void shouldHaveCurrency() throws Exception {
    checkMapContains(BVEventKeys.Transaction.CURRENCY, stubData.getTransaction().getCurrency());
  }

  @Test
  public void shouldHaveOrderId() throws Exception {
    checkMapContains(BVEventKeys.Transaction.ORDER_ID, stubData.getTransaction().getOrderId());
  }

  @Test
  public void shouldHaveShipping() throws Exception {
    checkMapContains(BVEventKeys.Transaction.SHIPPING, String.valueOf(stubData.getTransaction().getShipping()));
  }

  @Test
  public void shouldHaveState() throws Exception {
    checkMapContains(BVEventKeys.Transaction.STATE, stubData.getTransaction().getState());
  }

  @Test
  public void shouldHaveTax() throws Exception {
    checkMapContains(BVEventKeys.Transaction.TAX, String.valueOf(stubData.getTransaction().getTax()));
  }

  @Test
  public void shouldHaveTotal() throws Exception {
    checkMapContains(BVEventKeys.Transaction.TOTAL, String.valueOf(stubData.getTransaction().getTotal()));
  }

  @Test
  public void shouldHaveItems() throws Exception {
    assertTrue(getSubjectMap().containsKey(BVEventKeys.Transaction.ITEMS));
    List<Map<String, Object>> itemsList = (List<Map<String, Object>>) getSubjectMap().get(BVEventKeys.Transaction.ITEMS);
    assertEquals(8, itemsList.size());
  }

  @Test
  public void toRawShouldHaveNonTrackingAdId() throws Exception {
    checkMapContains(BVEventKeys.MobileEvent.ADVERTISING_ID, BVEventValues.NONTRACKING_TOKEN);
  }

  @Test
  public void shouldHaveNonTrackingAdIdForNonPiiMap() throws Exception {
    BvTestUtil.checkMapContains(
        getSubjectMapWithPii(),
        BVEventKeys.MobileEvent.ADVERTISING_ID,
        stubData.getAdId());
  }

  @Test
  public void toRawShouldHavePiiFields() throws Exception {
    Map<String, Object> map = getSubjectMap();
    for (String piiKey : stubData.getPiiParams().keySet()) {
      assertTrue(map.containsKey(piiKey));
    }
  }

  @Test
  public void toRawNonPiiShouldNotHavePiiFields() throws Exception {
    Map<String, Object> mapWithPii = getSubjectMapWithPii();
    for (String piiKey : stubData.getPiiParams().keySet()) {
      assertFalse(mapWithPii.containsKey(piiKey));
    }
  }

  @Override
  Map<String, Object> getSubjectMap() throws Exception {
    BVTransactionEvent subject = stubData.getTransactionEvent();
    subject.setAdditionalParams(stubData.getBothPiiAndNonPiiAdditionalParams());
    return subject.toRaw();
  }

  Map<String, Object> getSubjectMapWithPii() throws Exception {
    BVTransactionEvent subject = stubData.getTransactionEvent();
    subject.setAdditionalParams(stubData.getBothPiiAndNonPiiAdditionalParams());
    return subject.toRawNonPii();
  }
}
