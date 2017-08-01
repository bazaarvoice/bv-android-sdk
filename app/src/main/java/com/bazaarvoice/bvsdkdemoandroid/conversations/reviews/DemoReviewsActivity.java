/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.Review;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.conversations.DemoConvResponseHandler;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import javax.inject.Inject;

public class DemoReviewsActivity extends DemoBaseReviewsActivity<Review> {

    @Inject DemoMockDataUtil demoMockDataUtil;
    @Inject DemoClient demoClient;
    @Inject PrettyTime prettyTime;
    @Inject Picasso picasso;
    @Inject DemoConvResponseHandler demoConvResponseHandler;
    @Inject BVConversationsClient bvConversationsClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DemoApp.getAppComponent(this).inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    DemoMockDataUtil getDataUtil() {
        return demoMockDataUtil;
    }

    @Override
    DemoClient getDemoClient() {
        return demoClient;
    }

    @Override
    public Picasso getPicasso() {
        return picasso;
    }

    @Override
    public DemoConvResponseHandler getDemoConvResponseHandler() {
        return demoConvResponseHandler;
    }

    @Override
    BVConversationsClient getConvClient() {
        return bvConversationsClient;
    }

    @Override
    protected DemoReviewsAdapter<Review> createAdapter() {
        return new DemoReviewsAdapter<>(picasso, prettyTime, bvConversationsClient);
    }

}
