/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts;

import com.bazaarvoice.bvandroidsdk.BazaarRequest;
import com.bazaarvoice.bvandroidsdk.DisplayParams;
import com.bazaarvoice.bvandroidsdk.OnBazaarResponse;
import com.bazaarvoice.bvandroidsdk.types.Equality;
import com.bazaarvoice.bvandroidsdk.types.IncludeStatsType;
import com.bazaarvoice.bvandroidsdk.types.RequestType;
import com.bazaarvoice.bvsdkdemoandroid.conversations.BazaarProduct;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DemoProductPresenter implements DemoProductContract.UserActionsListener, OnBazaarResponse {

    private DemoProductContract.View view;
    private BazaarProduct bazaarProduct;
    private String productId;
    private DemoDataUtil demoDataUtil;
    private DemoConfigUtils demoConfigUtils;

    public DemoProductPresenter(DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, DemoProductContract.View view, String productId) {
        this.view = view;
        this.productId = productId;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
    }

    @Override
    public void loadProduct(boolean forceRefresh) {
        if (demoConfigUtils.isDemoClient()) {
            bazaarProduct = demoDataUtil.getBazaarProductWithStats();
            showProduct(bazaarProduct);
            return;
        }

        if (bazaarProduct != null) {
            showProduct(bazaarProduct);
            return;
        }

        view.showLoadingProduct(true);
        BazaarRequest bazaarRequest = new BazaarRequest();
        DisplayParams displayParams = new DisplayParams();
        displayParams.addFilter("id", Equality.EQUAL, productId);
        displayParams.addStats(IncludeStatsType.REVIEWS);
        displayParams.addStats(IncludeStatsType.QUESTIONS);
        bazaarRequest.sendDisplayRequest(RequestType.PRODUCTS, displayParams, this);
    }

    @Override
    public void onQandATapped() {
        if (bazaarProduct != null && bazaarProduct.getTotalQuestionCount() > 0) {
            view.transitionToQandA();
        } else if (bazaarProduct != null) {
            view.showAskQuestionDialog();
        }
    }

    @Override
    public void onReviewsTapped() {
        if (bazaarProduct != null && bazaarProduct.getNumReviews() > 0) {
            view.transitionToReviews();
        } else if (bazaarProduct != null) {
            view.showSubmitReviewDialog();
        }
    }

    @Override
    public void onResponse(String url, JSONObject response) {
        BazaarProduct bazaarProduct = null;
        try {
            JSONArray resultsArray = response.getJSONArray("Results");
            if (resultsArray != null && resultsArray.length() > 0) {
                JSONObject productJsonObj = resultsArray.getJSONObject(0);
                bazaarProduct = new BazaarProduct(productJsonObj);
                this.bazaarProduct = bazaarProduct;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        showProduct(bazaarProduct);
    }

    @Override
    public void onException(String message, Throwable exception) {
        showProduct(null);
    }

    private void showProduct(BazaarProduct bazaarProduct) {
        this.bazaarProduct = bazaarProduct;
        view.showLoadingProduct(false);

        if (bazaarProduct == null) {
            view.showNoProduct();
        } else {
            view.showProduct(bazaarProduct);
        }
    }
}
