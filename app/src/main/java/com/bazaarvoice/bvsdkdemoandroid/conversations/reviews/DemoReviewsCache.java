/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import com.bazaarvoice.bvandroidsdk.Review;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoCache;

import java.util.List;

public class DemoReviewsCache extends DemoCache<List<Review>> {

    private static DemoReviewsCache instance;

    private DemoReviewsCache() {
        super();
    }

    public static DemoReviewsCache getInstance() {
        if (instance == null) {
            instance = new DemoReviewsCache();
        }
        return instance;
    }

    @Override
    protected String getKey(List<Review> bazaarReviewList) {
        return null;
    }
}
