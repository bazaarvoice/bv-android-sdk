/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import com.bazaarvoice.bvandroidsdk.StoreReview;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoCache;

import java.util.List;

public class DemoStoreReviewsCache extends DemoCache<List<StoreReview>> {

    private static DemoStoreReviewsCache instance;

    private DemoStoreReviewsCache() {
        super();
    }

    public static DemoStoreReviewsCache getInstance() {
        if (instance == null) {
            instance = new DemoStoreReviewsCache();
        }
        return instance;
    }

    @Override
    protected String getKey(List<StoreReview> bazaarReviewList) {
        return null;
    }
}
