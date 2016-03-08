/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.recommendations;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.example.bazaarvoice.bv_android_sdk.recommendations.data.BvRepository;
import java.util.List;

/**
 * TODO: Description Here
 */
public class RecommendationsPresenter implements RecommendationsContract.UserActionsListener {

    private RecommendationsContract.View view;
    private BvRepository bvRepository;

    public RecommendationsPresenter(RecommendationsContract.View view, BvRepository bvRepository) {
        this.view = view;
        this.bvRepository = bvRepository;
    }

    @Override
    public void onRecommendationProductTapped(BVProduct recommendationProduct) {
        view.showMessage("Tapped on: " + recommendationProduct.getProductId());
        BVSDK.getInstance().sendProductConversionEvent(recommendationProduct);
    }

    @Override
    public void loadRecommendationProducts(boolean forceRefresh) {
        view.showLoading(true);

        if (forceRefresh) {
            bvRepository.refreshRecommendedProducts();
        }

        bvRepository.getRecommendedProducts(new BvRepository.LoadRecommendedProductsCallback() {
            @Override
            public void onRecommendedProductsLoaded(List<BVProduct> recommendedProducts) {
                view.showLoading(false);

                if (recommendedProducts == null || recommendedProducts.size() == 0) {
                    view.showNoRecommendations("No recommendations found :(");
                } else {
                    view.showRecommendations(recommendedProducts);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                view.showLoading(false);
                view.showMessage("Failed to get recommended products");
            }
        });
    }
}
