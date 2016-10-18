/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import com.bazaarvoice.bvandroidsdk.Review;

public class DemoReviewsActivity extends DemoBaseReviewsActivity<Review> {

    @Override
    protected DemoReviewsAdapter<Review> createAdapter() {
        return new DemoReviewsAdapter<>();
    }

}
