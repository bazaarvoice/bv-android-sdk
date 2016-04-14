/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.example.bazaarvoice.bv_android_sdk.recommendations;

import com.bazaarvoice.bvandroidsdk.BVProduct;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DemoProductsCache {

    private static long timeMillisAdded = 0l;
    private static List<BVProduct> cachedBvProducts = Collections.emptyList();

    public static void putBvProducts(List<BVProduct> bvProducts) {
        timeMillisAdded = System.currentTimeMillis();
        cachedBvProducts = bvProducts;
    }

    public static List<BVProduct> getBvProducts() {
        checkValid();
        return cachedBvProducts;
    }

    private static void checkValid() {
        long tenMinutesInMillis = TimeUnit.MINUTES.toMillis(10);
        long tenMinutesAgoInMillis = System.currentTimeMillis() - tenMinutesInMillis;
        boolean olderThan10Minutes = timeMillisAdded < tenMinutesAgoInMillis;
        if (olderThan10Minutes) {
            cachedBvProducts = Collections.emptyList();
        }
    }
}
