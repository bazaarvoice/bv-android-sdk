/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.recommendations;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoCache;

public class DemoProductsCache extends DemoCache<BVProduct> {

    private static DemoProductsCache instance;

    private DemoProductsCache() {
        super();
    }

    public static DemoProductsCache getInstance() {
        if (instance == null) {
            instance = new DemoProductsCache();
        }
        return instance;
    }

    @Override
    protected boolean shouldEvict() {
        return false; // Never auto-evict products cache for demo
    }

    @Override
    protected String getKey(BVProduct bvProduct) {
        return bvProduct.getId();
    }

}
