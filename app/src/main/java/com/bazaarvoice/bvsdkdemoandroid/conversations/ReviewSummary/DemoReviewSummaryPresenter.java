package com.bazaarvoice.bvsdkdemoandroid.conversations.ReviewSummary;

import androidx.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsException;
import com.bazaarvoice.bvandroidsdk.ReviewHighlightsRequest;
import com.bazaarvoice.bvandroidsdk.ReviewHighlightsResponse;
import com.bazaarvoice.bvandroidsdk.ReviewSummary;
import com.bazaarvoice.bvandroidsdk.ReviewSummaryRequest;
import com.bazaarvoice.bvandroidsdk.ReviewSummaryResponse;

public class DemoReviewSummaryPresenter implements DemoReviewSummaryDetailContract.UserActionsListener, ConversationsDisplayCallback<ReviewSummaryResponse> {
    private BVConversationsClient.DisplayLoader<ReviewHighlightsRequest, ReviewHighlightsResponse> loader;
    private BVConversationsClient conversationsClient;
    private String productId;
    private DemoReviewSummaryDetailContract.View view;

    public DemoReviewSummaryPresenter(DemoReviewSummaryDetailContract.View view, String productId, BVConversationsClient bvConversationsClient) {
        this.view = view;
        this.conversationsClient = bvConversationsClient;
        this.productId = productId;
    }

    public void loadReviewSummary(boolean forceRefresh) {
        ReviewSummaryRequest request = new ReviewSummaryRequest.Builder(productId)
                .build();
        conversationsClient.prepareCall(request).loadAsync(new ConversationsDisplayCallback<ReviewSummaryResponse>() {
            @Override
            public void onSuccess(@NonNull ReviewSummaryResponse response) {
                showReviewSummary(response.getReviewSummary());
            }
            @Override
            public void onFailure(@NonNull ConversationsException exception) {
                view.showDialogWithMessage(exception.getErrorListMessages());
            }
        });
    }

    private void showReviewSummary(ReviewSummary reviewSummary) {
        view.showReviewSummary(reviewSummary);
    }

    @Override
    public void onSuccess(@NonNull ReviewSummaryResponse response) {

    }

    @Override
    public void onFailure(@NonNull ConversationsException exception) {

    }
}
