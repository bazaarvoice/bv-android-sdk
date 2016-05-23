/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid.recommendations;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVRecommendations;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;

import java.util.Collections;
import java.util.List;

public class DemoRecommendationsPresenter implements DemoRecommendationsContract.UserActionsListener, BVRecommendations.BVRecommendationsCallback {

    private DemoRecommendationsContract.View view;
    private DemoConfigUtils demoConfigUtils;
    private DemoDataUtil demoDataUtil;

    public DemoRecommendationsPresenter(DemoRecommendationsContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil) {
        this.view = view;
        view.showLoading(true);
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
    }

    @Override
    public void onRecommendationProductTapped(BVProduct recommendationProduct) {
        view.showMessage("Tapped on: " + recommendationProduct.getProductId());
    }

    @Override
    public void loadRecommendationProducts(boolean forceRefresh) {
        DemoConfig currentConfig = demoConfigUtils.getCurrentConfig();
        List<BVProduct> demoBvProds = demoDataUtil.getRecommendedProducts();
        if (currentConfig.isDemoClient()) {
            showRecommendedProducts(demoBvProds);
            return;
        }

        view.showSwipeRefreshLoading(true);

        boolean haveLocalCache = !DemoProductsCache.getInstance().getData().isEmpty();
        boolean shouldHitNetwork = forceRefresh || !haveLocalCache;

        if (shouldHitNetwork) {
            BVRecommendations recs = new BVRecommendations();
            recs.getRecommendedProducts(20, this);
        } else {
            showRecommendedProducts(DemoProductsCache.getInstance().getData());
        }
    }

    @Override
    public void onResume() {
        DemoConfig currentConfig = demoConfigUtils.getCurrentConfig();
        String shopperAdKey = currentConfig.apiKeyShopperAdvertising;
        String displayName = currentConfig.displayName;

        if (!DemoConstants.isSet(shopperAdKey) && !currentConfig.isDemoClient()) {
            view.showNotConfiguredDialog(displayName);
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
