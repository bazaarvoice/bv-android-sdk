/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.questions;

import android.util.Log;

import com.bazaarvoice.bvandroidsdk.AnswerOptions;
import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.Question;
import com.bazaarvoice.bvandroidsdk.QuestionAndAnswerRequest;
import com.bazaarvoice.bvandroidsdk.QuestionAndAnswerResponse;
import com.bazaarvoice.bvandroidsdk.QuestionOptions;
import com.bazaarvoice.bvandroidsdk.SortOrder;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.conversations.DemoConvResponseHandler;
import com.bazaarvoice.bvsdkdemoandroid.products.DemoDisplayableProductsCache;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DemoQuestionsPresenter implements DemoQuestionsContract.UserActionsListener, ConversationsCallback<QuestionAndAnswerResponse> {

    private static final String TAG = "DemoQsPresenter";
    private DemoQuestionsContract.View view;
    private DemoClient demoClient;
    private DemoMockDataUtil demoMockDataUtil;
    private String productId;
    private BVConversationsClient.DisplayLoader<QuestionAndAnswerRequest, QuestionAndAnswerResponse> loader;
    private boolean fetched = false;
    private BVConversationsClient conversationsClient;
    private DemoConvResponseHandler demoConvResponseHandler;
    private boolean forceAPICall;

    public DemoQuestionsPresenter(DemoQuestionsContract.View view, BVConversationsClient bvConversationsClient, DemoClient demoClient, DemoMockDataUtil demoMockDataUtil, String productId, boolean forceAPICall, BVConversationsClient.DisplayLoader<QuestionAndAnswerRequest, QuestionAndAnswerResponse> loader, DemoConvResponseHandler demoConvResponseHandler) {
        this.view = view;
        this.conversationsClient = bvConversationsClient;
        this.demoClient = demoClient;
        this.demoMockDataUtil = demoMockDataUtil;
        this.demoConvResponseHandler = demoConvResponseHandler;
        this.productId = productId;
        this.forceAPICall = forceAPICall;
        this.loader = loader;

        if (productId != null && !productId.isEmpty()) {
            BVDisplayableProductContent bvProduct = DemoDisplayableProductsCache.getInstance().getDataItem(productId);
            String imageUrl = bvProduct == null ? null : bvProduct.getDisplayImageUrl();
            String productName = bvProduct == null ? "" : bvProduct.getDisplayName();
            float averageOverallRating = 5; //TODO RATING bvProduct == null ? -1 : bvProduct.getAverageRating();
            view.showHeaderView(imageUrl, productName, averageOverallRating);
        }
    }

    @Override
    public void loadQuestions(boolean forceRefresh) {
        fetched = false;
        if (!forceAPICall && demoClient.isMockClient()) {
            showQuestions(demoMockDataUtil.getConversationsQuestions().getResults());
            return;
        }

        List<Question> cachedQuestions = DemoQuestionsCache.getInstance().getDataItem(productId);
        boolean haveLocalCache = cachedQuestions!=null;
        boolean shouldHitNetwork = forceRefresh || !haveLocalCache;

        if (shouldHitNetwork) {
            view.showLoadingQuestions(true);

            QuestionAndAnswerRequest request = new QuestionAndAnswerRequest.Builder(productId, 20, 0)
                    .addQuestionSort(QuestionOptions.Sort.SubmissionTime, SortOrder.DESC)
                    .addAnswerSort(AnswerOptions.Sort.SubmissionTime, SortOrder.DESC)
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
        demoConvResponseHandler.handleDisplaySuccessResponse(response, new DemoConvResponseHandler.DisplayMessage() {
            @Override
            public void onSuccessMessage(String message) {

            }

            @Override
            public void onErrorMessage(String errorMessage) {
                view.showDialogWithMessage(errorMessage);
            }
        });
        showQuestions(response.getResults());
    }

    @Override
    public void onFailure(BazaarException exception) {
        view.showDialogWithMessage(exception.getMessage());
        exception.printStackTrace();
        showQuestions(Collections.<Question>emptyList());
    }
}
