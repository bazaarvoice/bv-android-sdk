package com.bazaarvoice.bvsdkdemoandroid.conversations.matchedtokens;

import java.util.List;

public interface DemoMatchedTokensContract {

    interface View {
        void showDialogWithMessage(String message);
        void showTokens(List<String> tokens);
    }

    interface UserActionsListener {
        void loadMatchedTokens(boolean forceRefresh);
    }
}

