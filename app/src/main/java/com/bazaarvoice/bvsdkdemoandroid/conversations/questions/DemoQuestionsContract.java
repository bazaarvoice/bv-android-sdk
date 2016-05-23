/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.questions;

import com.bazaarvoice.bvsdkdemoandroid.conversations.BazaarQuestion;

import java.util.List;

public interface DemoQuestionsContract {

    interface View {
        void showQuestions(List<BazaarQuestion> bazaarQuestions);
        void showLoadingQuestions(boolean show);
        void showNoQuestions();
        void transitionToQandA();
        void showAskQuestionDialog();
    }

    interface UserActionsListener {
        void loadQuestions(boolean forceRefresh);
        void onQandATapped();
    }
}
