/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.detail;

import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVRecommendations;
import com.bazaarvoice.bvandroidsdk.RecommendationsRequest;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.products.DemoDisplayableProductsCache;

import java.util.Collections;
import java.util.List;

public class DemoProductRecPresenter implements DemoProductRecContract.UserActionsListener, BVRecommendations.BVRecommendationsCallback {

    private static final int NUM_RECS = 20;

    private DemoProductRecContract.View view;
    private DemoClient demoClient;
    private DemoMockDataUtil demoMockDataUtil;
    private boolean isHomePage;
    private BVRecommendations.BVRecommendationsLoader recommendationsLoader;

    public DemoProductRecPresenter(DemoProductRecContract.View view, DemoClient demoClient, DemoMockDataUtil demoMockDataUtil, boolean isHomePage, BVRecommendations.BVRecommendationsLoader recommendationsLoader) {
        this.view = view;
        this.demoClient = demoClient;
        this.demoMockDataUtil = demoMockDataUtil;
        this.isHomePage = isHomePage;
        this.recommendationsLoader = recommendationsLoader;
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
        if (!demoClient.hasShopperAds() && !demoClient.isMockClient()) {
            if (demoClient.hasConversations()) {
                return;
            }
            view.showNoApiKey(demoClient.getDisplayName());
            view.showNoRecommendations();
            return;
        }

        List<BVProduct> demoBvProds = demoMockDataUtil.getRecommendationsProfile().getProfile().getRecommendedProducts();
        if (demoClient.isMockClient()) {
            showRecommendedProducts(demoBvProds, true);
            view.showLoadingRecs(false);
            return;
        }

        boolean haveLocalCache = !DemoDisplayableProductsCache.getInstance().getData().isEmpty();
        boolean productUpdate = productId != null && !productId.isEmpty();
        boolean categoryUpdate = categoryId != null && !categoryId.isEmpty();
        boolean shouldHitNetwork = forceRefresh || productUpdate || categoryUpdate;

        if (shouldHitNetwork) {
            view.showLoadingRecs(true);
            RecommendationsRequest.Builder builder = new RecommendationsRequest.Builder(NUM_RECS);
            if (productUpdate) {
                builder.productId(productId);
            }
            if (categoryUpdate) {
                builder.categoryId(categoryId);
            }
            RecommendationsRequest request = builder.build();
            recommendationsLoader.loadRecommendations(request, this);
        } else {
            showRecommendedProducts(DemoDisplayableProductsCache.getInstance().getData(), true);
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

    private <ProductType extends BVDisplayableProductContent> void showRecommendedProducts(List<ProductType> recommendedProducts, boolean success) {
        DemoDisplayableProductsCache.getInstance().putData((List<BVDisplayableProductContent>) recommendedProducts);

        if (success) {
            if (recommendedProducts.size() > 0) {
                view.showRecommendations((List<BVProduct>) recommendedProducts);
            } else {
                view.showNoRecommendations();
            }
        } else {
            view.showError();
        }
    }

}
