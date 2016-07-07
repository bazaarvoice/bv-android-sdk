/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import com.bazaarvoice.bvandroidsdk.Review;

import java.util.List;

public interface DemoReviewsContract {

    interface View {
        void showReviews(List<Review> bazaarReviews);
        void showLoadingReviews(boolean show);
        void showNoReviews();
        void showReviewsMessage(String message);
        void transitionToReviews();
        void showSubmitReviewDialog();
    }

    interface UserActionsListener {
        void loadReviews(boolean forceRefresh);
        void onReviewsTapped();
    }

}
