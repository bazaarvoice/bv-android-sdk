/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.recommendations.detail;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVRecommendations;

import java.util.List;

class DemoProductDetailPresenter implements DemoRecommendationDetailContract.UserActionsListener {

    private DemoRecommendationDetailContract.View view;
    private String bvProductId;

    public DemoProductDetailPresenter(DemoRecommendationDetailContract.View view, String bvProductId) {
        this.view = view;
        this.bvProductId = bvProductId;
    }

    @Override
    public void onRelatedRecommendationTapped(BVProduct relatedBvProduct) {
        view.transitionToRelatedRecommendation(relatedBvProduct);
    }

    @Override
    public void loadRelatedRecommendations(boolean forceRefresh) {
        BVRecommendations recommendations = new BVRecommendations();
        recommendations.getRecommendedProductsWithProductId(20, bvProductId, new BVRecommendations.BVRecommendationsCallback() {
            @Override
            public void onSuccess(List<BVProduct> recommendedProducts) {
                view.showRelatedRecommendations(recommendedProducts);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                view.showMessage(throwable.getMessage());
            }
        });
    }
}
