/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid.recommendations;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVRecommendations;
import com.bazaarvoice.bvandroidsdk.RecommendationsRequest;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;

import java.util.Collections;
import java.util.List;

public class DemoRecommendationsPresenter implements DemoRecommendationsContract.UserActionsListener, BVRecommendations.BVRecommendationsCallback {

    private static final int NUM_RECS = 20;

    private DemoRecommendationsContract.View view;
    private DemoClient demoClient;
    private DemoMockDataUtil demoMockDataUtil;
    private BVRecommendations.BVRecommendationsLoader recommendationsLoader;

    public DemoRecommendationsPresenter(DemoRecommendationsContract.View view, DemoClient demoClient, DemoMockDataUtil demoMockDataUtil, BVRecommendations.BVRecommendationsLoader recommendationsLoader) {
        this.view = view;
        view.showLoading(true);
        this.demoClient = demoClient;
        this.demoMockDataUtil = demoMockDataUtil;
        this.recommendationsLoader = recommendationsLoader;
    }

    @Override
    public void onRecommendationProductTapped(BVProduct recommendationProduct) {
        view.showMessage("Tapped on: " + recommendationProduct.getId());
    }

    @Override
    public void loadRecommendationProducts(boolean forceRefresh) {
        List<BVProduct> demoBvProds = demoMockDataUtil.getRecommendedProducts();
        if (demoClient.isMockClient()) {
            showRecommendedProducts(demoBvProds);
            return;
        } else if (!demoClient.hasShopperAds()) {
            view.showNotConfiguredDialog(demoClient.getDisplayName());
            view.showLoading(false);
            view.showSwipeRefreshLoading(false);
            return;
        }

        view.showSwipeRefreshLoading(true);

        boolean haveLocalCache = !DemoProductsCache.getInstance().getData().isEmpty();
        boolean shouldHitNetwork = forceRefresh || !haveLocalCache;

        if (shouldHitNetwork) {
            RecommendationsRequest request = new RecommendationsRequest.Builder(NUM_RECS).build();
            recommendationsLoader.loadRecommendations(request, this);
        } else {
            showRecommendedProducts(DemoProductsCache.getInstance().getData());
        }
    }

    @Override
    public void onSuccess(List<BVProduct> recommendedProducts) {
        showRecommendedProducts(recommendedProducts);
    }

    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();
        view.showMessage("Failed to get recommended products");
        showRecommendedProducts(Collections.<BVProduct>emptyList());
    }

    private void showRecommendedProducts(List<BVProduct> recommendedProducts) {
        view.showSwipeRefreshLoading(false);
        view.showLoading(false);
        DemoProductsCache.getInstance().putData(recommendedProducts);

        if (recommendedProducts.size() > 0) {
            view.showRecommendations(recommendedProducts);
        } else {
            view.showNoRecommendationsFound();
        }
    }
}
