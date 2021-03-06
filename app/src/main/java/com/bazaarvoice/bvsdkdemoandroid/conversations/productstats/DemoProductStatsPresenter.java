/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.productstats;

import androidx.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsException;
import com.bazaarvoice.bvandroidsdk.PDPContentType;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageRequest;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageResponse;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.conversations.DemoConvResponseHandler;

import java.util.Collections;
import java.util.List;

public class DemoProductStatsPresenter implements DemoProductStatsContract.UserActionsListener {

    private DemoProductStatsContract.View view;
    private DemoClientConfigUtils demoClientConfigUtils;
    private DemoMockDataUtil demoMockDataUtil;
    private String productId;
    private final BVConversationsClient client;
    private final DemoConvResponseHandler demoConvResponseHandler;

    public DemoProductStatsPresenter(DemoProductStatsContract.View view, BVConversationsClient client, DemoClientConfigUtils demoClientConfigUtils, DemoMockDataUtil demoMockDataUtil, String productId, DemoConvResponseHandler demoConvResponseHandler) {
        this.view = view;
        this.client = client;
        this.demoClientConfigUtils = demoClientConfigUtils;
        this.demoConvResponseHandler = demoConvResponseHandler;
        this.demoMockDataUtil = demoMockDataUtil;
        this.productId = productId;
    }

    @Override
    public void loadProductStats() {
        ProductDisplayPageRequest request = new ProductDisplayPageRequest.Builder(productId)
                .addIncludeStatistics(PDPContentType.Answers)
                .addIncludeStatistics(PDPContentType.Questions)
                .addIncludeStatistics(PDPContentType.Reviews)
                .build();

        client.prepareCall(request).loadAsync(new ConversationsDisplayCallback<ProductDisplayPageResponse>() {
            @Override
            public void onSuccess(@NonNull ProductDisplayPageResponse response) {
                showProductStats(response.getResults());
            }

            @Override
            public void onFailure(@NonNull ConversationsException exception) {
                view.showDialogWithMessage(exception.getMessage());
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
