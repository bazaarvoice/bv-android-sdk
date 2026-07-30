package com.bazaarvoice.bvsdkdemoandroid.conversations.contentcoach;

import java.util.List;

public interface DemoContentCoachContract {

    interface View {
        void showDialogWithMessage(String message);
        void showTokens(List<String> tokens);
    }

    interface UserActionsListener {
        void loadReviewTokens(boolean forceRefresh);
    }
}
