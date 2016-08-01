/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.questions;

import com.bazaarvoice.bvandroidsdk.Question;

import java.util.List;

public interface DemoQuestionsContract {

    interface View {
        void showHeaderView(String imageUrl, String productName, float averageRating);
        void showQuestions(List<Question> questions);
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
