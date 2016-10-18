/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import com.bazaarvoice.bvandroidsdk.ConversationsDisplayRecyclerView;
import com.bazaarvoice.bvandroidsdk.StoreReview;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;

public class DemoStoreReviewsActivity extends DemoBaseReviewsActivity<StoreReview> {

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
        return new DemoStoreReviewsAdapter();
    }

}