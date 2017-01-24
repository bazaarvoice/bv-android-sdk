/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.questions;

import android.util.Log;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.Question;
import com.bazaarvoice.bvandroidsdk.QuestionAndAnswerRequest;
import com.bazaarvoice.bvandroidsdk.QuestionAndAnswerResponse;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoDataUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DemoQuestionsPresenter implements DemoQuestionsContract.UserActionsListener, ConversationsCallback<QuestionAndAnswerResponse> {

    private static final String TAG = "DemoQsPresenter";
    private DemoQuestionsContract.View view;
    private DemoConfigUtils demoConfigUtils;
    private DemoDataUtil demoDataUtil;
    private String productId;
    private BVConversationsClient.DisplayLoader<QuestionAndAnswerRequest, QuestionAndAnswerResponse> loader;
    private boolean fetched = false;
    private BVConversationsClient conversationsClient = new BVConversationsClient();
    private boolean forceAPICall;

    public DemoQuestionsPresenter(DemoQuestionsContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, String productId, boolean forceAPICall, BVConversationsClient.DisplayLoader<QuestionAndAnswerRequest, QuestionAndAnswerResponse> loader) {
        this.view = view;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
        this.productId = productId;
        this.forceAPICall = forceAPICall;
        this.loader = loader;

        if (productId != null && !productId.isEmpty()) {
            BVProduct bvProduct = DemoProductsCache.getInstance().getDataItem(productId);
            String imageUrl = bvProduct == null ? null : bvProduct.getDisplayImageUrl();
            String productName = bvProduct == null ? "" : bvProduct.getDisplayName();
            float averageOverallRating = bvProduct == null ? -1 : bvProduct.getAverageRating();
            view.showHeaderView(imageUrl, productName, averageOverallRating);
        }
    }

    @Override
    public void loadQuestions(boolean forceRefresh) {
        fetched = false;
        DemoConfig currentConfig = demoConfigUtils.getCurrentConfig();
        if (!forceAPICall && currentConfig.isDemoClient()) {
            showQuestions(demoDataUtil.getConversationsQuestions());
            return;
        }

        List<Question> cachedQuestions = DemoQuestionsCache.getInstance().getDataItem(productId);
        boolean haveLocalCache = cachedQuestions!=null;
        boolean shouldHitNetwork = forceRefresh || !haveLocalCache;

        if (shouldHitNetwork) {
            view.showLoadingQuestions(true);

            QuestionAndAnswerRequest request = new QuestionAndAnswerRequest.Builder(productId, 20, 0)
                    .build();

            loader.loadAsync(conversationsClient.prepareCall(request), this);
        } else {
            showQuestions(cachedQuestions);
        }
    }

    @Override
    public void onQandATapped() {
        List<Question> cachedQuestions = DemoQuestionsCache.getInstance().getDataItem(productId);
        int numQs = cachedQuestions != null ? cachedQuestions.size() : 0;
        if (fetched && numQs > 0) {
            view.transitionToQandA();
        } else if (fetched) {
            view.showAskQuestionDialog();
        }
    }

    private void showQuestions(List<Question> questions) {
        fetched = true;
        view.showLoadingQuestions(false);
        DemoQuestionsCache.getInstance().putDataItem(productId, questions);

        if (questions.size() > 0) {
            view.showQuestions(questions);
            Question firstQuestion = questions.get(0);
            Date date = firstQuestion.getSubmissionDate();
            Log.d(TAG, "question submitted on " + date.toString());
            Product product = firstQuestion.getProduct();
            if (product != null) {
                String imageUrl = product.getDisplayImageUrl();
                String productName = product.getDisplayName();
                float averageOverallRating = -1;
                if (product.getReviewStatistics() != null) {
                    averageOverallRating = product.getReviewStatistics().getAverageOverallRating();
                }
                view.showHeaderView(imageUrl, productName, averageOverallRating);
            }
        } else {
            view.showNoQuestions();
        }
    }

    @Override
    public void onSuccess(QuestionAndAnswerResponse response) {
        showQuestions(response.getResults());
    }

    @Override
    public void onFailure(BazaarException exception) {
        exception.printStackTrace();
        showQuestions(Collections.<Question>emptyList());
    }
}
