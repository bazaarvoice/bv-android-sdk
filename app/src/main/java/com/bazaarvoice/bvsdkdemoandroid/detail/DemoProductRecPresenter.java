/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.detail;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVRecommendations;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;

import java.util.Collections;
import java.util.List;

public class DemoProductRecPresenter implements DemoProductRecContract.UserActionsListener, BVRecommendations.BVRecommendationsCallback {

    private static final int NUM_RECS = 20;

    private DemoProductRecContract.View view;
    private DemoConfigUtils demoConfigUtils;
    private DemoDataUtil demoDataUtil;
    private boolean isHomePage;

    public DemoProductRecPresenter(DemoProductRecContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, boolean isHomePage) {
        this.view = view;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
        this.isHomePage = isHomePage;
    }

    @Override
    public void loadRecommendations(boolean forceRefresh) {
        loadRecommendations(forceRefresh, null, null);
    }

    @Override
    public void loadRecommendationsWithProductId(boolean forceRefresh, String productId) {
        loadRecommendations(forceRefresh, productId, null);
    }

    @Override
    public void loadRecommendationsWithCategoryId(boolean forceRefresh, String categoryId) {
        loadRecommendations(forceRefresh, null, categoryId);
    }

    private void loadRecommendations(boolean forceRefresh, String productId, String categoryId) {
        DemoConfig currentConfig = demoConfigUtils.getCurrentConfig();
        List<BVProduct> demoBvProds = demoDataUtil.getRecommendedProducts();
        if (currentConfig.isDemoClient()) {
            showRecommendedProducts(demoBvProds, true);
            return;
        }

        boolean haveLocalCache = !DemoProductsCache.getInstance().getData().isEmpty();
        boolean productUpdate = productId != null && !productId.isEmpty();
        boolean categoryUpdate = categoryId != null && !categoryId.isEmpty();
        boolean shouldHitNetwork = forceRefresh || !haveLocalCache || productUpdate || categoryUpdate;

        if (shouldHitNetwork) {
            view.showLoadingRecs(true);
            BVRecommendations recs = new BVRecommendations();
            if (productUpdate) {
                recs.getRecommendedProductsWithProductId(NUM_RECS, productId, this);
            } else if (categoryUpdate) {
                recs.getRecommendedProductsWithCategoryId(NUM_RECS, categoryId, this);
            } else {
                recs.getRecommendedProducts(NUM_RECS, this);
            }
        } else {
            showRecommendedProducts(DemoProductsCache.getInstance().getData(), true);
        }
    }

    @Override
    public void onSuccess(List<BVProduct> recommendedProducts) {
        showRecommendedProducts(recommendedProducts, true);
    }

    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();
        view.showRecMessage("Failed to get recommended products");
        showRecommendedProducts(Collections.<BVProduct>emptyList(), false);
    }

    private void showRecommendedProducts(List<BVProduct> recommendedProducts, boolean success) {
        view.showLoadingRecs(false);
        if (isHomePage) {
            DemoProductsCache.getInstance().clear();
        }
        DemoProductsCache.getInstance().putData(recommendedProducts);

        if (success) {
            if (recommendedProducts.size() > 0) {
                view.showRecommendations(recommendedProducts);
            } else {
                view.showNoRecommendations();
            }
        } else {
            view.showError();
        }
    }

}
