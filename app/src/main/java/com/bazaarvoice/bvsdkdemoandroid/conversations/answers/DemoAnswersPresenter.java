/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.answers;


import com.bazaarvoice.bvsdkdemoandroid.conversations.BazaarAnswer;
import com.bazaarvoice.bvsdkdemoandroid.conversations.BazaarQuestion;
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

    public DemoAnswersPresenter(DemoAnswersContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, String productId, String questionId) {
        this.view = view;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
        this.productId = productId;
        this.questionId = questionId;
    }

    @Override
    public void loadAnswers(boolean forceRefresh) {
        DemoConfig currentConfig = demoConfigUtils.getCurrentConfig();
        if (currentConfig.isDemoClient()) {
            view.showAnswers(demoDataUtil.getConversationsAnswers(questionId));
            return;
        }

        DemoQuestionsCache demoQuestionsCache = DemoQuestionsCache.getInstance();
        List<BazaarQuestion> cachedQuestions = demoQuestionsCache.getDataItem(productId);
        List<BazaarAnswer> cachedAnswers = new ArrayList<>();
        for (BazaarQuestion bazaarQuestion : cachedQuestions) {
            if (bazaarQuestion.getId().equals(questionId)) {
                cachedAnswers.addAll(bazaarQuestion.getBazaarAnswers());
                break;
            }
        }
        view.showAnswers(cachedAnswers);
    }

}
