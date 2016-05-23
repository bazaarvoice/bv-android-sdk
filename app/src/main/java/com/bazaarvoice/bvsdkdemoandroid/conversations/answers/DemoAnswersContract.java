/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.answers;

import com.bazaarvoice.bvsdkdemoandroid.conversations.BazaarAnswer;

import java.util.List;

interface DemoAnswersContract {

    interface View {
        void showAnswers(List<BazaarAnswer> answers);
    }

    interface UserActionsListener {
        void loadAnswers(boolean forceRefresh);
    }

}
