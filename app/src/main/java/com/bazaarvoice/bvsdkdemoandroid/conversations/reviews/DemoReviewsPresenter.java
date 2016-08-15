/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.Review;
import com.bazaarvoice.bvandroidsdk.ReviewResponse;
import com.bazaarvoice.bvandroidsdk.ReviewsRequest;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;

import java.util.Collections;
import java.util.List;

public class DemoReviewsPresenter implements DemoReviewsContract.UserActionsListener {

    private DemoReviewsContract.View view;
    private DemoConfigUtils demoConfigUtils;
    private DemoDataUtil demoDataUtil;
    private String productId;
    private boolean fetched = false;
    private final BVConversationsClient client = new BVConversationsClient();
    private boolean forceAPICall;

    public DemoReviewsPresenter(DemoReviewsContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, String productId, boolean forceAPICall) {
        this.view = view;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
        this.productId = productId;
        this.forceAPICall = forceAPICall;
    }

    @Override
    public void loadReviews(boolean forceRefresh) {
        fetched = false;
        DemoConfig currentConfig = demoConfigUtils.getCurrentConfig();
        if (!forceAPICall && currentConfig.isDemoClient()) {
            showReviews(demoDataUtil.getConversationsReviews());
            return;
        }

        List<Review> cachedReviews = DemoReviewsCache.getInstance().getDataItem(productId);
        boolean haveLocalCache = cachedReviews!=null;
        boolean shouldHitNetwork = forceRefresh || !haveLocalCache;

        if (shouldHitNetwork) {
            ReviewsRequest request = new ReviewsRequest.Builder(productId, 20, 0).build();
            client.prepareCall(request).loadAsync(new ConversationsCallback<ReviewResponse>() {
                @Override
                public void onSuccess(ReviewResponse response) {
                    showReviews(response.getResults());
                }

                @Override
                public void onFailure(BazaarException exception) {
                    exception.printStackTrace();
                    showReviews(Collections.<Review>emptyList());
                }
            });
        } else {
            showReviews(cachedReviews);
        }
    }

    @Override
    public void onReviewsTapped() {
        List<Review> cachedReviews = DemoReviewsCache.getInstance().getDataItem(productId);
        int numReviews = cachedReviews != null ? cachedReviews.size() : 0;
        if (fetched && numReviews > 0) {
            view.transitionToReviews();
        } else if (fetched) {
            view.showSubmitReviewDialog();
        }
    }

    private void showReviews(List<Review> bazaarReviews) {
        fetched = true;
        view.showLoadingReviews(false);
        DemoReviewsCache.getInstance().putDataItem(productId, bazaarReviews);

        if (bazaarReviews.size() > 0) {
            view.showReviews(bazaarReviews);
        } else {
            view.showNoReviews();
        }
    }
}
