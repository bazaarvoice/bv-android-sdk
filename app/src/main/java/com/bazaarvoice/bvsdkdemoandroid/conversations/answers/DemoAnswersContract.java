/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.answers;

import com.bazaarvoice.bvandroidsdk.Answer;

import java.util.List;

interface DemoAnswersContract {

    interface View {
        void showHeaderView(String imageUrl, String productName, float averageRating);
        void showAnswers(List<Answer> answers);
    }

    interface UserActionsListener {
        void loadAnswers(boolean forceRefresh);
    }

}
