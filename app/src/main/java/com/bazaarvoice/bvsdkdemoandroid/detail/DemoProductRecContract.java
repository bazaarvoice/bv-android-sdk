package com.bazaarvoice.bvsdkdemoandroid.detail;

import com.bazaarvoice.bvandroidsdk.BVProduct;

import java.util.List;

public interface DemoProductRecContract {

    interface View {
        void showRecommendations(List<BVProduct> bvProducts);
        void showLoadingRecs(boolean show);
        void showNoRecommendations();
        void showRecMessage(String message);
    }

    interface UserActionsListener {
        void loadRecommendations(boolean forceRefresh);
        void loadRecommendationsWithProductId(boolean forceRefresh, String productId);
        void loadRecommendationsWithCategoryId(boolean forceRefresh, String categoryId);
    }
}
