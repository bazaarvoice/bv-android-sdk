package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.warnShouldNotBeEmpty;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutAllSafe;

public final class BVTransactionEvent extends BVPiiEvent {
  private final BVTransaction transaction;

  public BVTransactionEvent(@NonNull BVTransaction transaction) {
    super(BVEventValues.BVEventClass.CONVERSION, BVEventValues.BVEventType.TRANSACTION);
    warnShouldNotBeEmpty("transaction", transaction);
    this.transaction = transaction;
  }

  @Override
  protected Map<String, Object> getPiiUnrelatedParams() {
    Map<String, Object> map = new HashMap<>();
    BVTransaction.Mapper transactionMapper = new BVTransaction.Mapper(transaction);
    Map<String, Object> transactionMap = transactionMapper.toRaw();
    mapPutAllSafe(map, transactionMap);
    return map;
  }
}
