/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.recommendations.data;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import java.util.List;

/**
 * TODO: Description Here
 */
public interface BvRepository {

    interface LoadRecommendedProductsCallback {
        void onRecommendedProductsLoaded(List<BVProduct> recommendedProducts);
        void onFailure(Throwable throwable);
    }

    void getRecommendedProducts(LoadRecommendedProductsCallback callback);

    void refreshRecommendedProducts();
}
