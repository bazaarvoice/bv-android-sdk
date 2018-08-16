/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.recommendations.detail;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVRecommendations;
import com.bazaarvoice.bvandroidsdk.BVRecommendationsResponse;
import com.bazaarvoice.bvandroidsdk.RecommendationsRequest;

class DemoProductDetailPresenter implements DemoRecommendationDetailContract.UserActionsListener, BVRecommendations.BVRecommendationsCallback {

    private static final int NUM_RECS = 20;

    private DemoRecommendationDetailContract.View view;
    private String bvProductId;
    private BVRecommendations.BVRecommendationsLoader recommendationsLoader;

    public DemoProductDetailPresenter(DemoRecommendationDetailContract.View view, String bvProductId, BVRecommendations.BVRecommendationsLoader recommendationsLoader) {
        this.view = view;
        this.bvProductId = bvProductId;
        this.recommendationsLoader = recommendationsLoader;
    }

    @Override
    public void onRelatedRecommendationTapped(BVProduct relatedBvProduct) {
        view.transitionToRelatedRecommendation(relatedBvProduct);
    }

    @Override
    public void loadRelatedRecommendations(boolean forceRefresh) {
        RecommendationsRequest request = new RecommendationsRequest.Builder(NUM_RECS)
                .productId(bvProductId)
                .build();
        recommendationsLoader.loadRecommendations(request, this);
    }

    @Override
    public void onSuccess(BVRecommendationsResponse response) {
        view.showRelatedRecommendations(response.getRecommendedProducts());
    }

    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();
        view.showMessage(throwable.getMessage());
    }
}
