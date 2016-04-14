/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.recommendations;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVRecommendations;

import java.util.Collections;
import java.util.List;

public class DemoRecommendationsPresenter implements DemoRecommendationsContract.UserActionsListener {

    private DemoRecommendationsContract.View view;

    public DemoRecommendationsPresenter(DemoRecommendationsContract.View view) {
        this.view = view;
    }

    @Override
    public void onRecommendationProductTapped(BVProduct recommendationProduct) {
        view.showMessage("Tapped on: " + recommendationProduct.getProductId());
    }

    @Override
    public void loadRecommendationProducts(boolean forceRefresh) {
        view.showLoading(true);

        boolean haveLocalCache = !DemoProductsCache.getBvProducts().isEmpty();
        boolean shouldHitNetwork = forceRefresh || !haveLocalCache;

        if (shouldHitNetwork) {
            BVRecommendations recs = new BVRecommendations();
            recs.getRecommendedProducts(20, new BVRecommendations.BVRecommendationsCallback() {
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
            });
        } else {
            showRecommendedProducts(DemoProductsCache.getBvProducts());
        }
    }

    private void showRecommendedProducts(List<BVProduct> recommendedProducts) {
        view.showLoading(false);
        DemoProductsCache.putBvProducts(recommendedProducts);
        view.showRecommendations(recommendedProducts);
    }
}
