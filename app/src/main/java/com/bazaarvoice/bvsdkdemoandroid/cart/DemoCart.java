/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.cart;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.BVTransaction;
import com.bazaarvoice.bvandroidsdk.BVTransactionItem;

import java.util.ArrayList;
import java.util.List;

public enum DemoCart {

    INSTANCE;

    public DemoCart getSharedInstance() {
        return INSTANCE;
    }

    private static final BVConversationsClient bvClient = new BVConversationsClient();

    private final List<BVDisplayableProductContent> products = new ArrayList<>();

    public void addProduct(BVDisplayableProductContent product) {
        products.add(product);
    }

    public void remove(int i) {
        products.remove(i);
    }

    public int numberOfItems() {
        return products.size();
    }

    public BVDisplayableProductContent get(int i) {
        return products.get(i);
    }

    public BVTransaction completeTransaction() {
        List<BVTransactionItem> items = new ArrayList<>();

        double total = 0;

        for (BVDisplayableProductContent product : products) {
            BVTransactionItem item = new BVTransactionItem.Builder(product.getId())
                .setName(product.getDisplayName())
                .setPrice(0.01)
                .setQuantity(1)
                .setImageUrl(product.getDisplayImageUrl())
                .build();

            total += 0.1;

            items.add(item);
        }

        products.clear();

        String orderId = "123456";

        return new BVTransaction.Builder()
            .setOrderId(orderId)
            .setTotal(total)
            .setItems(items)
            .setShipping(0.00)
            .setTax(0.00)
            .build();
    }
}
