/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.recommendations;

import com.bazaarvoice.bvandroidsdk.BVProduct;

import java.util.List;

/**
 * TODO: Description Here
 */
public interface RecommendationsContract {

    interface View {
        void showRecommendations(List<BVProduct> recommendationProducts);
        void showLoading(boolean isLoading);
        void showMessage(String message);
        void showNoRecommendations(String message);
    }

    interface UserActionsListener {
        void onRecommendationProductTapped(BVProduct recommendationProduct);
        void loadRecommendationProducts(boolean forceRefresh);
    }
}
