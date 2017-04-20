/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.answers;


import com.bazaarvoice.bvandroidsdk.Answer;
import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.Question;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.conversations.questions.DemoQuestionsCache;
import com.bazaarvoice.bvsdkdemoandroid.products.DemoDisplayableProductsCache;

import java.util.ArrayList;
import java.util.List;

public class DemoAnswersPresenter implements DemoAnswersContract.UserActionsListener {

    private DemoAnswersContract.View view;
    private DemoClient demoClient;
    private DemoMockDataUtil demoMockDataUtil;
    private String productId;
    private String questionId;
    private boolean forceAPICall;

    public DemoAnswersPresenter(DemoAnswersContract.View view, DemoClient demoClient, DemoMockDataUtil demoMockDataUtil, String productId, String questionId, boolean forceAPICall) {
        this.view = view;
        this.demoClient = demoClient;
        this.demoMockDataUtil = demoMockDataUtil;
        this.productId = productId;
        this.questionId = questionId;
        this.forceAPICall = forceAPICall;

        if (productId != null && !productId.isEmpty()) {
            BVDisplayableProductContent bvProduct = DemoDisplayableProductsCache.getInstance().getDataItem(productId);
            String imageUrl = bvProduct == null ? null : bvProduct.getDisplayImageUrl();
            String productName = bvProduct == null ? "" : bvProduct.getDisplayName();
            float averageOverallRating = 5; //TODO RATING bvProduct == null ? -1 : bvProduct.getAverageRating();
            view.showHeaderView(imageUrl, productName, averageOverallRating);
        }
    }

    @Override
    public void loadAnswers(boolean forceRefresh) {
        if (!forceAPICall && demoClient.isMockClient()) {
            view.showAnswers(demoMockDataUtil.getConversationsAnswers(questionId));
            return;
        }

        DemoQuestionsCache demoQuestionsCache = DemoQuestionsCache.getInstance();
        List<Question> cachedQuestions = demoQuestionsCache.getDataItem(productId);
        List<Answer> cachedAnswers = new ArrayList<>();
        for (Question bazaarQuestion : cachedQuestions) {
            if (bazaarQuestion.getId().equals(questionId)) {
                cachedAnswers.addAll(bazaarQuestion.getAnswers());
                break;
            }
        }
        view.showAnswers(cachedAnswers);
    }

}
