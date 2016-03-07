/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import java.util.List;

/**
 * Interface for Recommendations
 */
public interface Recommendations {

    /**
     *
     * @param limit
     *          max number of recommended products
     *          default is 20
     *          valid limits 1...50; will use default if out of range
     * @param categoryId
     *          used to filter recommended products within a category. Mutually exclusive with productId.
     *          if productId and categoryId are set, productId will be used
     * @param callback
     *          callback to provide list of Products or handle errors
     */
    void getRecommendedProductsWithCategoryId(final int limit, String categoryId, BVRecommendationsCallback callback);

    /**
     *
     * @param limit
     *          max number of recommended products
     *          default is 20
     *          valid limits 1...50; will use default if out of range
     * @param productId
     *          used to filter recommended products to similar products
     * @param callback
     *          callback to provide list of Products or handle errors
     */
    void getRecommendedProductsWithProductId(final int limit, String productId, BVRecommendationsCallback callback);

    /**
     *
     * @param limit
     *          max number of recommended products
     * @param callback
     *          callback to provide list of Products or handle errors
     */

    void getRecommendedProducts(int limit, BVRecommendationsCallback callback);

    /**
     * Callback used to asynchronously receive the Bazaarvoice product recommendations
     */
    interface BVRecommendationsCallback {
        void onSuccess(List<BVProduct> recommendedProducts);
        void onFailure(Throwable throwable);
    }

}
