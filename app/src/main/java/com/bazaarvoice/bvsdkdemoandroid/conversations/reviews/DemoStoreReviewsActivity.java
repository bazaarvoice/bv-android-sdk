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
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoDataUtil;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import javax.inject.Inject;

public class DemoStoreReviewsActivity extends DemoBaseReviewsActivity<StoreReview> {

    @Inject DemoDataUtil demoDataUtil;
    @Inject DemoConfigUtils demoConfigUtils;
    @Inject Picasso picasso;
    @Inject PrettyTime prettyTime;

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
    protected DemoReviewsContract.UserActionsListener getReviewsUserActionListener(DemoReviewsContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, String productId, boolean forceLoadFromProductId, ConversationsDisplayRecyclerView reviewsRecyclerView) {
        return new DemoStoreReviewsPresenter(view, demoConfigUtils, demoDataUtil, productId, forceLoadFromProductId, reviewsRecyclerView);
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