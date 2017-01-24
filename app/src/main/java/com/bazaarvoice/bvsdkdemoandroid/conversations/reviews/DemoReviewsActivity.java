/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.Review;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoDataUtil;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import javax.inject.Inject;

public class DemoReviewsActivity extends DemoBaseReviewsActivity<Review> {

    @Inject DemoDataUtil demoDataUtil;
    @Inject DemoConfigUtils demoConfigUtils;
    @Inject PrettyTime prettyTime;
    @Inject Picasso picasso;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DemoApp.get(this).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    DemoDataUtil getDataUtil() {
        return demoDataUtil;
    }

    @Override
    DemoConfigUtils getConfigUtils() {
        return demoConfigUtils;
    }

    @Override
    public Picasso getPicasso() {
        return picasso;
    }

    @Override
    protected DemoReviewsAdapter<Review> createAdapter() {
        return new DemoReviewsAdapter<>(picasso, prettyTime);
    }

}
