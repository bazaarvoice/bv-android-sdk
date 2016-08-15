/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.PDPContentType;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageRequest;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageResponse;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
/**
 *  @deprecated - Old API to be removed
 */
public class DemoProductPresenter implements DemoProductContract.UserActionsListener {

    private DemoProductContract.View view;
    private Product product;
    private String productId;
    private DemoDataUtil demoDataUtil;
    private DemoConfigUtils demoConfigUtils;
    private BVConversationsClient conversationsClient = new BVConversationsClient();

    public DemoProductPresenter(DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, DemoProductContract.View view, String productId) {
        this.view = view;
        this.productId = productId;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
    }

    @Override
    public void loadProduct(boolean forceRefresh) {
        if (demoConfigUtils.isDemoClient()) {
            product = demoDataUtil.getBazaarProductWithStats();
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
        conversationsClient.prepareCall(request).loadAsync(new ConversationsCallback<ProductDisplayPageResponse>() {
            @Override
            public void onSuccess(ProductDisplayPageResponse response) {
                showProduct(response.getResults().get(0));
            }

            @Override
            public void onFailure(BazaarException exception) {
                showProduct(null);
            }
        });
    }

    @Override
    public void onQandATapped() {
        if (product != null && product.getQaStatistics().getTotalQuestionCount() > 0) {
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
