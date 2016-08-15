/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts;

import com.bazaarvoice.bvandroidsdk.Product;
/**
 *  @deprecated - Old API to be removed
 */
public interface DemoProductContract {

    interface View {
        void showProduct(Product product);
        void showNoProduct();
        void showLoadingProduct(boolean show);
        void showSubmitReviewDialog();
        void showAskQuestionDialog();
        void transitionToQandA();
        void transitionToReviews();
    }

    interface UserActionsListener {
        void loadProduct(boolean forceRefresh);
        void onQandATapped();
        void onReviewsTapped();
    }
}
