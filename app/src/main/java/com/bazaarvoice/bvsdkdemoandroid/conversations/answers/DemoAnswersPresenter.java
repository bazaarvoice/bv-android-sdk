/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.answers;


import com.bazaarvoice.bvandroidsdk.Answer;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.Question;
import com.bazaarvoice.bvsdkdemoandroid.conversations.questions.DemoQuestionsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;

import java.util.ArrayList;
import java.util.List;

public class DemoAnswersPresenter implements DemoAnswersContract.UserActionsListener {

    private DemoAnswersContract.View view;
    private DemoConfigUtils demoConfigUtils;
    private DemoDataUtil demoDataUtil;
    private String productId;
    private String questionId;
    private boolean forceAPICall;

    public DemoAnswersPresenter(DemoAnswersContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, String productId, String questionId,  boolean forceAPICall) {
        this.view = view;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
        this.productId = productId;
        this.questionId = questionId;
        this.forceAPICall = forceAPICall;
    }

    @Override
    public void loadAnswers(boolean forceRefresh) {
        DemoConfig currentConfig = demoConfigUtils.getCurrentConfig();
        if (!forceAPICall && currentConfig.isDemoClient()) {
            view.showAnswers(demoDataUtil.getConversationsAnswers(questionId));
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
        if (cachedAnswers.size() > 0) {
            Answer firstAnswer = cachedAnswers.get(0);
            Question question = firstAnswer.getQuestion();
            if (question != null) {
                Product product = question.getProduct();
                if (product != null) {
                    String imageUrl = product.getImageUrl();
                    String productName = product.getName();
                    float averageOverallRating = -1;
                    if (product.getReviewStatistics() != null) {
                        averageOverallRating = product.getReviewStatistics().getAverageOverallRating();
                    }
                    view.showHeaderView(imageUrl, productName, averageOverallRating);
                }
            }
        }
    }

}
