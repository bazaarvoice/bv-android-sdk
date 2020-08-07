package com.bazaarvoice.bvandroidsdk;

public interface DemoRevieHighlightDetailContract {

    interface View {
        void showDialogWithMessage(String message);
        void showReviewHighlights(ReviewHighlights reviewHighlights);
    }

    interface UserActionsListener {
        void loadReviewHighlights(boolean forceRefresh);
    }
}
