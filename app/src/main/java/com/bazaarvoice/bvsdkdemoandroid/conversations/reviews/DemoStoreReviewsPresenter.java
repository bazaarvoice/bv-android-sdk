/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.ReviewOptions;
import com.bazaarvoice.bvandroidsdk.SortOrder;
import com.bazaarvoice.bvandroidsdk.Store;
import com.bazaarvoice.bvandroidsdk.StoreReview;
import com.bazaarvoice.bvandroidsdk.StoreReviewResponse;
import com.bazaarvoice.bvandroidsdk.StoreReviewsRequest;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;

import java.util.List;

public class DemoStoreReviewsPresenter extends DemoReviewsPresenter {

    public DemoStoreReviewsPresenter(DemoReviewsContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, String productId, boolean forceAPICall, BVConversationsClient.DisplayLoader<StoreReviewsRequest, StoreReviewResponse> reviewsLoader) {
        super(view, demoConfigUtils, demoDataUtil, productId, forceAPICall, reviewsLoader);
    }

    @Override
    public void loadReviews(boolean forceRefresh) {
        fetched = false;
        DemoConfig currentConfig = demoConfigUtils.getCurrentConfig();
        if (!forceAPICall && currentConfig.isDemoClient()) {
            showStoreReviews(demoDataUtil.getConversationsStoreReviews());
            return;
        }

        List<StoreReview> cachedReviews = DemoStoreReviewsCache.getInstance().getDataItem(productId);
        boolean haveLocalCache = cachedReviews!=null;
        boolean shouldHitNetwork = forceRefresh || !haveLocalCache;

        if (shouldHitNetwork) {
            StoreReviewsRequest request = new StoreReviewsRequest.Builder(productId, 20, 0)
                    .addSort(ReviewOptions.Sort.Rating, SortOrder.DESC)
                    .build();
            reviewsLoader.loadAsync(client.prepareCall(request), this);
        } else {
            showStoreReviews(cachedReviews);
        }
    }

    @Override
    public void onSuccess(Object object) {
        StoreReviewResponse response = (StoreReviewResponse) object;
        showStoreReviews(response.getResults());
    }

    private void showStoreReviews(List<StoreReview> bazaarReviews) {
        fetched = true;
        view.showLoadingReviews(false);
        DemoStoreReviewsCache.getInstance().putDataItem(productId, bazaarReviews);

        if (bazaarReviews.size() > 0) {
            view.showReviews(bazaarReviews);
            StoreReview firstReview = bazaarReviews.get(0);
            Store store = firstReview.getIncludedIn().getStores().get(0);
            if (store != null) {
                String imageUrl = store.getDisplayImageUrl();
                String productName = store.getDisplayName();
                    float averageOverallRating = -1;
                    if (store.getReviewStatistics() != null) {
                    averageOverallRating = store.getReviewStatistics().getAverageOverallRating();
                }
                view.showHeaderView(imageUrl, productName, averageOverallRating);
            }
        } else {
            view.showNoReviews();
        }
    }
}