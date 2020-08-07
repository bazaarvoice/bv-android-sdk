package com.bazaarvoice.bvsdkdemoandroid.conversations.ReviewHighlights;

import androidx.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsException;
import com.bazaarvoice.bvandroidsdk.DemoRevieHighlightDetailContract;
import com.bazaarvoice.bvandroidsdk.ReviewHighlights;
import com.bazaarvoice.bvandroidsdk.ReviewHighlightsRequest;
import com.bazaarvoice.bvandroidsdk.ReviewHighlightsResponse;

public class DemoReviewHighlightsPresenter implements DemoRevieHighlightDetailContract.UserActionsListener, ConversationsDisplayCallback<ReviewHighlightsResponse> {
    private BVConversationsClient.DisplayLoader<ReviewHighlightsRequest, ReviewHighlightsResponse> loader;
    private BVConversationsClient conversationsClient;
    private String productId;
    private DemoRevieHighlightDetailContract.View view;

    public DemoReviewHighlightsPresenter(DemoRevieHighlightDetailContract.View view,String productId, BVConversationsClient bvConversationsClient) {
        this.view = view;
        this.conversationsClient = bvConversationsClient;
        this.productId = productId;
        this.productId = productId;
    }

    public void loadReviewHighlights(boolean forceRefresh) {
        ReviewHighlightsRequest request = new ReviewHighlightsRequest.Builder(productId)
                .build();
        conversationsClient.prepareCall(request).loadAsync(new ConversationsDisplayCallback<ReviewHighlightsResponse>() {
            @Override
            public void onSuccess(@NonNull ReviewHighlightsResponse response) {
                showReviewHighlights(response.getReviewHighlights());
            }
            @Override
            public void onFailure(@NonNull ConversationsException exception) {
                view.showDialogWithMessage(exception.getErrorListMessages());
            }
        });
    }

    private void showReviewHighlights(ReviewHighlights reviewHighlights) {
        view.showReviewHighlights(reviewHighlights);
    }

    @Override
    public void onSuccess(@NonNull ReviewHighlightsResponse response) {

    }

    @Override
    public void onFailure(@NonNull ConversationsException exception) {

    }
}
