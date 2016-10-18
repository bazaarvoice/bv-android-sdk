/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.stores;

import com.bazaarvoice.bvandroidsdk.Store;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoCache;

import java.util.ArrayList;
import java.util.List;

public class DemoStoreCache extends DemoCache<List<Store>> {

    private static DemoStoreCache instance;

    public static DemoStoreCache getInstance() {
        if (instance == null) {
            instance = new DemoStoreCache();
        }
        return instance;
    }

    @Override
    protected String getKey(List<Store> stores) {
        List<String> storeIds = new ArrayList<>();
        for (Store store : stores) {
            storeIds.add(store.getId());
        }
        return generateKeyForStoresList(storeIds);
    }

    public static String generateKeyForStoresList(List<String> storeIds) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String storeId : storeIds) {
            stringBuilder.append(storeId);
        }
        return stringBuilder.toString();
    }
}
