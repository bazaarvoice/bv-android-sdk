package com.bazaarvoice.bvandroidsdk;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    final Map<String, Object> raw = new BVTransaction.Mapper(subject).toRaw();
    assertEquals(2, subject.getItems().size());
    assertEquals(2, subject.getOtherParams().size());

    for (Iterator iter = otherParams.entrySet().iterator(); iter.hasNext(); ) {
      Map.Entry entry = (Map.Entry) iter.next();
      assertTrue(raw.containsKey(entry.getKey()));
      assertTrue(raw.get(entry.getKey()) == entry.getValue());
    }
  }
}
