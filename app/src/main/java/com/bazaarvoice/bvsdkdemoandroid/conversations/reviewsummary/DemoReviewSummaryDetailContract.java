package com.bazaarvoice.bvsdkdemoandroid.conversations.reviewsummary;

import com.bazaarvoice.bvandroidsdk.ReviewSummary;

public interface DemoReviewSummaryDetailContract {

    interface View {
        void showDialogWithMessage(String message);
        void showReviewSummary(ReviewSummary reviewSummary);
    }

    interface UserActionsListener {
        void loadReviewSummary(boolean forceRefresh);
    }
}
