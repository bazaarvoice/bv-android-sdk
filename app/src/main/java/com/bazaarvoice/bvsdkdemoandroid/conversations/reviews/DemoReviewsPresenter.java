/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import android.util.Log;

import com.bazaarvoice.bvandroidsdk.BazaarRequest;
import com.bazaarvoice.bvandroidsdk.DisplayParams;
import com.bazaarvoice.bvandroidsdk.OnBazaarResponse;
import com.bazaarvoice.bvandroidsdk.types.Equality;
import com.bazaarvoice.bvandroidsdk.types.IncludeStatsType;
import com.bazaarvoice.bvandroidsdk.types.IncludeType;
import com.bazaarvoice.bvandroidsdk.types.RequestType;
import com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts.BazaarReview;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DemoReviewsPresenter implements DemoReviewsContract.UserActionsListener, OnBazaarResponse {

    private DemoReviewsContract.View view;
    private DemoConfigUtils demoConfigUtils;
    private DemoDataUtil demoDataUtil;
    private String productId;
    private boolean fetched = false;

    public DemoReviewsPresenter(DemoReviewsContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, String productId) {
        this.view = view;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
        this.productId = productId;
    }

    @Override
    public void loadReviews(boolean forceRefresh) {
        fetched = false;
        DemoConfig currentConfig = demoConfigUtils.getCurrentConfig();
        if (currentConfig.isDemoClient()) {
            showReviews(demoDataUtil.getConversationsReviews());
            return;
        }

        List<BazaarReview> cachedReviews = DemoReviewsCache.getInstance().getDataItem(productId);
        boolean haveLocalCache = cachedReviews!=null;
        boolean shouldHitNetwork = forceRefresh || !haveLocalCache;

        if (shouldHitNetwork) {
            view.showLoadingReviews(true);
            BazaarRequest bazaarRequest = new BazaarRequest();
            DisplayParams displayParams = new DisplayParams();
            displayParams.addFilter("ProductId", Equality.EQUAL, productId);
            displayParams.addInclude(IncludeType.PRODUCTS);
            displayParams.addStats(IncludeStatsType.REVIEWS);
            displayParams.setLimit(50);
            bazaarRequest.sendDisplayRequest(RequestType.REVIEWS, displayParams, this);
        } else {
            showReviews(cachedReviews);
        }
    }

    @Override
    public void onReviewsTapped() {
        List<BazaarReview> cachedReviews = DemoReviewsCache.getInstance().getDataItem(productId);
        int numReviews = cachedReviews != null ? cachedReviews.size() : 0;
        if (fetched && numReviews > 0) {
            view.transitionToReviews();
        } else if (fetched) {
            view.showSubmitReviewDialog();
        }
    }

    private void showReviews(List<BazaarReview> bazaarReviews) {
        fetched = true;
        view.showLoadingReviews(false);
        DemoReviewsCache.getInstance().putDataItem(productId, bazaarReviews);

        if (bazaarReviews.size() > 0) {
            view.showReviews(bazaarReviews);
        } else {
            view.showNoReviews();
        }
    }

    @Override
    public void onResponse(String url, JSONObject response) {
        Log.d("ReviewsPresent", "url: " + url);
        List<BazaarReview> bazaarReviewList = new ArrayList<>();
        try {
            JSONArray resultsJsonArray = response.getJSONArray("Results");
            for (int i=0; i<resultsJsonArray.length(); i++) {
                JSONObject reviewJsonObject = resultsJsonArray.getJSONObject(i);
                BazaarReview bazaarReview = new BazaarReview(reviewJsonObject);
                bazaarReviewList.add(bazaarReview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showReviews(bazaarReviewList);
    }

    @Override
    public void onException(String message, Throwable exception) {
        exception.printStackTrace();
        showReviews(Collections.<BazaarReview>emptyList());
    }
}
