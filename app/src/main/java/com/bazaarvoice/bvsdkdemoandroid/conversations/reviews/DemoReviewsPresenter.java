/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import androidx.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsException;
import com.bazaarvoice.bvandroidsdk.PDPContentType;
import com.bazaarvoice.bvandroidsdk.Review;
import com.bazaarvoice.bvandroidsdk.ReviewIncludeType;
import com.bazaarvoice.bvandroidsdk.ReviewOptions;
import com.bazaarvoice.bvandroidsdk.ReviewResponse;
import com.bazaarvoice.bvandroidsdk.ReviewsRequest;
import com.bazaarvoice.bvandroidsdk.SortOrder;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.products.DemoDisplayableProductsCache;

import java.util.Collections;
import java.util.List;

public class DemoReviewsPresenter implements DemoReviewsContract.UserActionsListener, ConversationsDisplayCallback<ReviewResponse> {
    protected DemoReviewsContract.View view;
    protected DemoClient demoClient;
    protected DemoMockDataUtil demoMockDataUtil;
    protected BVConversationsClient.DisplayLoader reviewsLoader;
    protected String productId;
    protected String filterId;
    protected String calledUrl;
    protected ReviewOptions.PrimaryFilter filterType;
    protected boolean fetched = false;
    protected final BVConversationsClient client;
    protected boolean forceAPICall;

    public DemoReviewsPresenter(DemoReviewsContract.View view, BVConversationsClient client, DemoClient demoClient, DemoMockDataUtil demoMockDataUtil, String productId, String filterId, ReviewOptions.PrimaryFilter filterType, boolean forceAPICall, BVConversationsClient.DisplayLoader reviewsLoader) {
        this.view = view;
        this.client = client;
        this.demoClient = demoClient;
        this.demoMockDataUtil = demoMockDataUtil;
        this.reviewsLoader = reviewsLoader;
        this.productId = productId;
        this.forceAPICall = forceAPICall;
        this.filterId = filterId;
        this.filterType = filterType;

        if (productId != null && !productId.isEmpty()) {
            BVDisplayableProductContent bvProduct = DemoDisplayableProductsCache.getInstance().getDataItem(productId);
            String imageUrl = bvProduct == null ? null : bvProduct.getDisplayImageUrl();
            String productName = bvProduct == null ? "" : bvProduct.getDisplayName();
            float averageOverallRating = 5; //TODO RATING bvProduct == null ? -1 : bvProduct.getAverageRating();
            view.showHeaderView(imageUrl, productName, averageOverallRating);
        }
    }

    @Override
    public void loadReviews(boolean forceRefresh) {
        fetched = false;
        if (!forceAPICall && demoClient.isMockClient()) {
            showReviews(demoMockDataUtil.getConversationsReviews().getResults());
            return;
        }

        String cacheId = getCacheId();
        List<Review> cachedReviews = DemoReviewsCache.getInstance().getDataItem(cacheId);
        boolean shouldHitNetwork = isShouldHitNetwork(forceRefresh, cachedReviews);
        ReviewsRequest request;
        if (shouldHitNetwork) {
            request = buildReviewsRequest();
            reviewsLoader.loadAsync(client.prepareCall(request), this);
        } else {
            showReviews(cachedReviews);
        }
    }

    private boolean isShouldHitNetwork(boolean forceRefresh, List<Review> cachedReviews) {
        boolean haveLocalCache = cachedReviews!=null;
        return forceRefresh || !haveLocalCache;
    }

    private String getCacheId(){
        if(filterId != null && !filterId.isEmpty()) {
            return filterId + filterType;
        }
        return productId;
    }

    private ReviewsRequest buildReviewsRequest() {
        ReviewsRequest request;
        if(filterId != null && !filterId.isEmpty()) {
            request = new ReviewsRequest.Builder(filterType, filterId, 20, 0)
                    .addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC)
                    .build();
        }
        else {
            request = new ReviewsRequest.Builder(productId, 20, 0)
                    .addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC)
                    .addIncludeContent(
                            ReviewIncludeType.PRODUCTS,
                            ReviewIncludeType.CATEGORIES,
                            ReviewIncludeType.AUTHORS,
                            ReviewIncludeType.COMMENTS
                    )
                    .addPDPContentType(
                            PDPContentType.Answers,
                            PDPContentType.Questions,
                            PDPContentType.Reviews,
                            PDPContentType.Stories
                    )
                    .build();
        }
        return request;
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
        DemoReviewsCache.getInstance().putDataItem(getCacheId(), bazaarReviews);

        if (bazaarReviews.size() > 0) {
            view.showReviews(bazaarReviews);
        } else {
            view.showNoReviews();
        }
    }

    @Override
    public void onSuccess(@NonNull ReviewResponse response) {
        showReviews(response.getResults());
    }

    @Override
    public void onFailure(@NonNull ConversationsException exception) {
        view.showDialogWithMessage(exception.getMessage());
        exception.printStackTrace();
        showReviews(Collections.<Review>emptyList());
    }
}
