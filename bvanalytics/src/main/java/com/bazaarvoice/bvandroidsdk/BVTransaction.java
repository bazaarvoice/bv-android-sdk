package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutAllSafe;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;

public final class BVTransaction {
  private final String orderId;
  private final double total;
  private final List<BVTransactionItem> items;
  private final Map<String, String> otherParams;
  private final double tax;
  private final double shipping;
  private final String city;
  private final String state;
  private final String country;
  private final String currency;

  BVTransaction(Builder builder) {
    this.orderId = builder.orderId;
    this.total = builder.total;
    this.items = builder.items;
    this.otherParams = builder.otherParams;
    this.tax = builder.tax;
    this.shipping = builder.shipping;
    this.city = builder.city;
    this.state = builder.state;
    this.country = builder.country;
    this.currency = builder.currency;
  }

  public String getOrderId() {
    return orderId;
  }

  public double getTotal() {
    return total;
  }

  public List<BVTransactionItem> getItems() {
    return items;
  }

  public Map<String, String> getOtherParams() {
    return otherParams;
  }

  public double getTax() {
    return tax;
  }

  public double getShipping() {
    return shipping;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getCountry() {
    return country;
  }

  public String getCurrency() {
    return currency;
  }

  public static class Builder {
    private String orderId = "";
    private double total = 0.0;
    private List<BVTransactionItem> items = new ArrayList<>();
    private Map<String, String> otherParams = new HashMap<>();
    private double tax = 0.0;
    private double shipping = 0.0;
    private String city = "";
    private String state = "";
    private String country = "";
    private String currency = "";

    public Builder setOrderId(String orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder setTotal(double total) {
      this.total = total;
      return this;
    }

    public Builder setItems(List<BVTransactionItem> items) {
      this.items = items;
      return this;
    }

    public Builder setOtherParams(Map<String, String> otherParams) {
      this.otherParams = otherParams;
      return this;
    }

    public Builder setTax(double tax) {
      this.tax = tax;
      return this;
    }

    public Builder setShipping(double shipping) {
      this.shipping = shipping;
      return this;
    }

    public Builder setCity(String city) {
      this.city = city;
      return this;
    }

    public Builder setState(String state) {
      this.state = state;
      return this;
    }

    public Builder setCountry(String country) {
      this.country = country;
      return this;
    }

    public Builder setCurrency(String currency) {
      this.currency = currency;
      return this;
    }

    public BVTransaction build() {
      return new BVTransaction(this);
    }
  }

  static class Mapper implements BVAnalyticsMapper {
    private BVTransaction transaction;

    public Mapper(@NonNull BVTransaction transaction) {
      this.transaction = transaction;
    }

    @Override
    public Map<String, Object> toRaw() {
      Map<String, Object> map = new HashMap<>();
      mapPutSafe(map, BVEventKeys.Transaction.CITY, transaction.getCity());
      mapPutSafe(map, BVEventKeys.Transaction.COUNTRY, transaction.getCountry());
      mapPutSafe(map, BVEventKeys.Transaction.CURRENCY, transaction.getCurrency());
      mapPutSafe(map, BVEventKeys.Transaction.ORDER_ID, transaction.getOrderId());
      mapPutSafe(map, BVEventKeys.Transaction.SHIPPING, transaction.getShipping());
      mapPutSafe(map, BVEventKeys.Transaction.STATE, transaction.getState());
      mapPutSafe(map, BVEventKeys.Transaction.TAX, transaction.getTax());
      mapPutSafe(map, BVEventKeys.Transaction.TOTAL, transaction.getTotal());

      if (transaction.getItems() != null && !transaction.getItems().isEmpty()) {
        List<BVTransactionItem.Mapper> itemMappers = new ArrayList<>();
        for (BVTransactionItem item : transaction.getItems()) {
          if (item == null) {
            continue;
          }
          BVTransactionItem.Mapper itemMapper = new BVTransactionItem.Mapper(item);
          itemMappers.add(itemMapper);
        }
        List<Map<String, Object>> itemsJsonArr = new ArrayList<>();
        for (BVTransactionItem.Mapper itemMapper : itemMappers) {
          itemsJsonArr.add(itemMapper.toRaw());
        }
        mapPutSafe(map, BVEventKeys.Transaction.ITEMS, itemsJsonArr);
      }

      if (transaction.getOtherParams() != null && !transaction.getOtherParams().isEmpty()) {
        Map<String, Object> otherMap =
            Collections.<String, Object>unmodifiableMap(transaction.getOtherParams());
        mapPutAllSafe(map, otherMap);
      }

      return map;
    }
  }
}
