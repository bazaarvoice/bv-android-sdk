/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.ConversationsDisplayRecyclerView;
import com.bazaarvoice.bvandroidsdk.StoreReview;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.conversations.DemoConvResponseHandler;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import javax.inject.Inject;

public class DemoStoreReviewsActivity extends DemoBaseReviewsActivity<StoreReview> {

    @Inject DemoMockDataUtil demoMockDataUtil;
    @Inject DemoClient demoClient;
    @Inject Picasso picasso;
    @Inject PrettyTime prettyTime;
    @Inject
    DemoConvResponseHandler demoConvResponseHandler;

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
    protected DemoReviewsContract.UserActionsListener getReviewsUserActionListener(DemoReviewsContract.View view, DemoClient demoClient, DemoMockDataUtil demoMockDataUtil, String productId, boolean forceLoadFromProductId, ConversationsDisplayRecyclerView reviewsRecyclerView) {
        return new DemoStoreReviewsPresenter(view, demoClient, demoMockDataUtil, productId, forceLoadFromProductId, reviewsRecyclerView, getDemoConvResponseHandler());
    }

    @Override
    void inflateRecyclerView() {
        recyclerViewStub.setLayoutResource(R.layout.store_reviews_recyclerview);
        recyclerViewStub.inflate();
        reviewsRecyclerView = (ConversationsDisplayRecyclerView) findViewById(R.id.store_reviews_recycler_view);
    }

    @Override
    protected DemoReviewsAdapter<StoreReview> createAdapter() {
        return new DemoStoreReviewsAdapter(picasso, prettyTime);
    }

}