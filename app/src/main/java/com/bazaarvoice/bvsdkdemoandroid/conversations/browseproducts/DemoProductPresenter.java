/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts;

import android.support.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsException;
import com.bazaarvoice.bvandroidsdk.PDPContentType;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageRequest;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageResponse;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;

import java.util.List;

/**
 *  @deprecated - Old API to be removed
 */
public class DemoProductPresenter implements DemoProductContract.UserActionsListener {

    private DemoProductContract.View view;
    private Product product;
    private String productId;
    private DemoMockDataUtil demoMockDataUtil;
    private DemoClient demoClient;
    private BVConversationsClient conversationsClient;

    public DemoProductPresenter(DemoClient demoClient, BVConversationsClient bvConversationsClient, DemoMockDataUtil demoMockDataUtil, DemoProductContract.View view, String productId) {
        this.view = view;
        this.conversationsClient = bvConversationsClient;
        this.productId = productId;
        this.demoClient = demoClient;
        this.demoMockDataUtil = demoMockDataUtil;
    }

    @Override
    public void loadProduct(boolean forceRefresh) {
        if (demoClient.isMockClient()) {
            product = demoMockDataUtil.getConversationsPdp().getResults().get(0);
            showProduct(product);
            return;
        }

        if (product != null) {
            showProduct(product);
            return;
        }

        view.showLoadingProduct(true);

        ProductDisplayPageRequest request = new ProductDisplayPageRequest.Builder(productId)
                .addIncludeStatistics(PDPContentType.Reviews)
                .addIncludeStatistics(PDPContentType.Questions)
                .build();
        conversationsClient.prepareCall(request).loadAsync(new ConversationsDisplayCallback<ProductDisplayPageResponse>() {
            @Override
            public void onSuccess(@NonNull ProductDisplayPageResponse response) {
                List<Product> products = response.getResults();
                Product firstProduct = products.size() > 0 ? products.get(0) : null;
                showProduct(firstProduct);
            }

            @Override
            public void onFailure(@NonNull ConversationsException exception) {
                showProduct(null);
            }
        });
    }

    @Override
    public void onQandATapped() {
        if (product != null && product.getQaStatistics() != null && product.getQaStatistics().getTotalQuestionCount() > 0) {
            view.transitionToQandA();
        } else if (product != null) {
            view.showAskQuestionDialog();
        }
    }

    @Override
    public void onReviewsTapped() {
        if (product != null && product.getReviewStatistics().getTotalReviewCount() > 0) {
            view.transitionToReviews();
        } else if (product != null) {
            view.showSubmitReviewDialog();
        }
    }

    private void showProduct(Product product) {
        this.product = product;
        view.showLoadingProduct(false);

        if (product == null) {
            view.showNoProduct();
        } else {
            view.showProduct(product);
        }
    }
}
