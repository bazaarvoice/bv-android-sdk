/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import com.bazaarvoice.bvandroidsdk.BaseReview;
import com.bazaarvoice.bvandroidsdk.FeaturesSentimentResponse;
import com.bazaarvoice.bvandroidsdk.SummarisedFeaturesResponse;

import java.util.List;

public interface DemoReviewsContract<ReviewType extends BaseReview> {

    interface View<ReviewType> {
        void showHeaderView(String imageUrl, String productName, float averageRating);
        void showReviews(List<ReviewType> reviews);
        void showLoadingReviews(boolean show);
        void showNoReviews();
        void showDialogWithMessage(String message);
        void transitionToReviews();
        void showSubmitReviewDialog();
        void showSummarisedFeatures(SummarisedFeaturesResponse response);
        void showFeaturesSentiment(FeaturesSentimentResponse response);
    }

    interface UserActionsListener {
        void loadReviews(boolean forceRefresh);
        void loadSummarisedFeatures(boolean forceRefresh);

        void loadFeaturesSentiment(boolean forceRefresh);

        void onReviewsTapped();
    }

}
