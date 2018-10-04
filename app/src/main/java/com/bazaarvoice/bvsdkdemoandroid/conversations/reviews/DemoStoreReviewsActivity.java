/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVUiConversationsDisplayRecyclerView;
import com.bazaarvoice.bvandroidsdk.StoreReview;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import javax.inject.Inject;

public class DemoStoreReviewsActivity extends DemoBaseReviewsActivity<StoreReview> {
    @Inject DemoMockDataUtil demoMockDataUtil;
    @Inject DemoClient demoClient;
    @Inject Picasso picasso;
    @Inject PrettyTime prettyTime;
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
    BVConversationsClient getConvClient() {
        return bvConversationsClient;
    }

    @Override
    protected DemoReviewsContract.UserActionsListener getReviewsUserActionListener(DemoReviewsContract.View view, BVConversationsClient bvConversationsClient, DemoClient demoClient, DemoMockDataUtil demoMockDataUtil, String productId, boolean forceLoadFromProductId, BVUiConversationsDisplayRecyclerView reviewsRecyclerView) {
        return new DemoStoreReviewsPresenter(view, bvConversationsClient, demoClient, demoMockDataUtil, productId, forceLoadFromProductId, reviewsRecyclerView);
    }

    @Override
    void inflateRecyclerView() {
        recyclerViewStub.setLayoutResource(R.layout.store_reviews_recyclerview);
        recyclerViewStub.inflate();
        reviewsRecyclerView = (BVUiConversationsDisplayRecyclerView) findViewById(R.id.store_reviews_recycler_view);
    }

    @Override
    protected DemoReviewsAdapter<StoreReview> createAdapter() {
        return new DemoStoreReviewsAdapter(picasso, prettyTime, bvConversationsClient);
    }

}