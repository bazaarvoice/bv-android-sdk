/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.questions;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.Question;
import com.bazaarvoice.bvandroidsdk.QuestionAndAnswerRequest;
import com.bazaarvoice.bvandroidsdk.QuestionAndAnswerResponse;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;

import java.util.Collections;
import java.util.List;

public class DemoQuestionsPresenter implements DemoQuestionsContract.UserActionsListener, ConversationsCallback<QuestionAndAnswerResponse> {

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
            view.showHeaderView(null, "API Test Questions for Product: " + productId, -1);
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
            Product product = firstQuestion.getProduct();
            if (product != null) {
                String imageUrl = product.getImageUrl();
                String productName = product.getName();
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
