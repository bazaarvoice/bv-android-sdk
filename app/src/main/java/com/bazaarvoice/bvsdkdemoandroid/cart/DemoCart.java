/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.cart;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.Transaction;
import com.bazaarvoice.bvandroidsdk.TransactionItem;

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

    public Transaction completeTransaction() {

        List<TransactionItem> items = new ArrayList<>();

        double total = 0;

        for (BVDisplayableProductContent product : products) {
            TransactionItem item = new TransactionItem.Builder(product.getId())
                    .name(product.getDisplayName())
                    .price(0.01)
                    .quantity(1)
                    .imageUrl(product.getDisplayImageUrl())
                    .build();

            total += 0.1;

            items.add(item);
        }

        products.clear();

        String orderId = "123456";

        return new Transaction.Builder(orderId, total, items, null)
                .shipping(0.00)
                .tax(0.00)
                .build();
    }
}
