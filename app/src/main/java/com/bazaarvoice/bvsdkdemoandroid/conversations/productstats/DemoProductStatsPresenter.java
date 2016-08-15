/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.productstats;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.PDPContentType;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageRequest;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageResponse;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;

import java.util.Collections;
import java.util.List;

public class DemoProductStatsPresenter implements DemoProductStatsContract.UserActionsListener {

    private DemoProductStatsContract.View view;
    private DemoConfigUtils demoConfigUtils;
    private DemoDataUtil demoDataUtil;
    private String productId;
    private final BVConversationsClient client = new BVConversationsClient();

    public DemoProductStatsPresenter(DemoProductStatsContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, String productId) {
        this.view = view;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
        this.productId = productId;
    }

    @Override
    public void loadProductStats() {

        ProductDisplayPageRequest request = new ProductDisplayPageRequest.Builder(productId)
                .addIncludeStatistics(PDPContentType.Answers)
                .addIncludeStatistics(PDPContentType.Questions)
                .addIncludeStatistics(PDPContentType.Reviews)
                .build();

        client.prepareCall(request).loadAsync(new ConversationsCallback<ProductDisplayPageResponse>() {
            @Override
            public void onSuccess(ProductDisplayPageResponse response) {
                // called on Main Thread
                showProductStats(response.getResults());
            }

            @Override
            public void onFailure(BazaarException exception) {
                //called on Main Thread
                exception.printStackTrace();
                showProductStats(Collections.<Product>emptyList());
            }
        });

    }

    private void showProductStats(List<Product> bazaarProduct) {

        view.showLoadingProductStats(false);

        if (bazaarProduct.size() > 0) {
            view.showProductStats(bazaarProduct);
        } else {
            view.showNoProductStats();
        }
    }
}
