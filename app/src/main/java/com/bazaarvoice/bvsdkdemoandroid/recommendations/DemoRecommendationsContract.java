/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid.recommendations;

import com.bazaarvoice.bvandroidsdk.BVProduct;

import java.util.List;

public interface DemoRecommendationsContract {

    interface View {
        void showRecommendations(List<BVProduct> recommendationProducts);
        void showNoRecommendationsFound();
        void showSwipeRefreshLoading(boolean isLoading);
        void showLoading(boolean isLoading);
        void showMessage(String message);
        void showNotConfiguredDialog(String displayName);
    }

    interface UserActionsListener {
        void onRecommendationProductTapped(BVProduct recommendationProduct);
        void loadRecommendationProducts(boolean forceRefresh);
    }
}
