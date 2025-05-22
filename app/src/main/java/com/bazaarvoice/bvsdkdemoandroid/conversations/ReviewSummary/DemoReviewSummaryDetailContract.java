package com.bazaarvoice.bvsdkdemoandroid.conversations.ReviewSummary;

import com.bazaarvoice.bvandroidsdk.ReviewSummary;
import com.bazaarvoice.bvandroidsdk.ReviewSummaryResponse;

public interface DemoReviewSummaryDetailContract {

    interface View {
        void showDialogWithMessage(String message);
        void showReviewSummary(ReviewSummary reviewSummary);
    }

    interface UserActionsListener {
        void loadReviewSummary(boolean forceRefresh);
    }
}
