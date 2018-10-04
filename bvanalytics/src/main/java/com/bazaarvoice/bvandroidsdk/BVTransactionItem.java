package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;

public final class BVTransactionItem {
  private final String sku;
  private final String name;
  private final String imageUrl;
  private final String category;
  private final double price;
  private final int quantity;

  private BVTransactionItem(Builder builder) {
    this.sku = builder.sku;
    this.name = builder.name;
    this.imageUrl = builder.imageUrl;
    this.category = builder.category;
    this.price = builder.price;
    this.quantity = builder.quantity;
  }

  public String getSku() {
    return sku;
  }

  public String getName() {
    return name;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getCategory() {
    return category;
  }

  public double getPrice() {
    return price;
  }

  public int getQuantity() {
    return quantity;
  }

  public static class Builder {
    private String sku = "";
    private String name = "";
    private String imageUrl = "";
    private String category = "";
    private double price = 0.0;
    private int quantity = 1;

    public Builder(@NonNull String sku) {
      this.sku = sku;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
      return this;
    }

    public Builder setCategory(String category) {
      this.category = category;
      return this;
    }

    public Builder setPrice(double price) {
      this.price = price;
      return this;
    }

    public Builder setQuantity(int quantity) {
      this.quantity = quantity;
      return this;
    }

    public BVTransactionItem build() {
      return new BVTransactionItem(this);
    }
  }

  static class Mapper implements BVAnalyticsMapper {
    private BVTransactionItem transactionItem;

    public Mapper(@NonNull BVTransactionItem transactionItem) {
      this.transactionItem = transactionItem;
    }

    @Override
    public Map<String, Object> toRaw() {
      Map<String, Object> map = new HashMap<>();
      mapPutSafe(map, BVEventKeys.TransactionItem.CATEGORY, transactionItem.getCategory());
      mapPutSafe(map, BVEventKeys.TransactionItem.IMAGE_URL, transactionItem.getImageUrl());
      mapPutSafe(map, BVEventKeys.TransactionItem.NAME, transactionItem.getName());
      mapPutSafe(map, BVEventKeys.TransactionItem.QUANTITY, transactionItem.getQuantity());
      mapPutSafe(map, BVEventKeys.TransactionItem.PRICE, transactionItem.getPrice());
      mapPutSafe(map, BVEventKeys.TransactionItem.SKU, transactionItem.getSku());
      return map;
    }
  }
}
