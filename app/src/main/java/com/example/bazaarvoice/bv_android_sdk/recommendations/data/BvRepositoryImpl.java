/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.recommendations.data;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.bazaarvoice.bvandroidsdk.Recommendations;
import com.example.bazaarvoice.bv_android_sdk.di.UserConfigurationImpl;

import java.util.List;

/**
 * TODO: Description Here
 */
public class BvRepositoryImpl implements BvRepository {

    private Recommendations bvRecommendations;

    private List<BVProduct> recommendedProducts;

    public BvRepositoryImpl(Recommendations bvRecommendations) {
        this.bvRecommendations = bvRecommendations;
        BVSDK.getInstance().setUserAuthString(UserConfigurationImpl.BV_USER_AUTH_STRING);
    }

    @Override public void getRecommendedProducts(final LoadRecommendedProductsCallback callback) {
        if (recommendedProducts != null) {
            callback.onRecommendedProductsLoaded(recommendedProducts);
            return;
        }

        bvRecommendations.getRecommendedProducts(20, new Recommendations.BVRecommendationsCallback() {

            @Override public void onSuccess(List<BVProduct> recommendedProducts) {
                BvRepositoryImpl.this.recommendedProducts = recommendedProducts;
                callback.onRecommendedProductsLoaded(recommendedProducts);
            }

            @Override public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override public void refreshRecommendedProducts() {
        this.recommendedProducts = null;
    }
}
