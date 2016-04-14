/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.example.bazaarvoice.bv_android_sdk.recommendations.detail;

import com.bazaarvoice.bvandroidsdk.BVProduct;

import java.util.List;

interface DemoRecommendationDetailContract {

    interface View {
        void showRelatedRecommendations(List<BVProduct> relatedRecommendations);
        void transitionToRelatedRecommendation(BVProduct bvProduct);
        void showMessage(String message);
    }

    interface UserActionsListener {
        void onRelatedRecommendationTapped(BVProduct relatedBvProduct);
        void loadRelatedRecommendations(boolean forceRefresh);
    }
}
