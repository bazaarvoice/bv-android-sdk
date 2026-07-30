package com.bazaarvoice.bvsdkdemoandroid.conversations.contentcoach;

import androidx.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsException;
import com.bazaarvoice.bvandroidsdk.ReviewTokensRequest;
import com.bazaarvoice.bvandroidsdk.ReviewTokensResponse;

public class DemoContentCoachPresenter implements DemoContentCoachContract.UserActionsListener {
    private final BVConversationsClient conversationsClient;
    private final String productId;
    private final DemoContentCoachContract.View view;

    public DemoContentCoachPresenter(DemoContentCoachContract.View view, String productId, BVConversationsClient conversationsClient) {
        this.view = view;
        this.productId = productId;
        this.conversationsClient = conversationsClient;
    }

    @Override
    public void loadReviewTokens(boolean forceRefresh) {
        ReviewTokensRequest request = new ReviewTokensRequest.Builder(productId).build();
        conversationsClient.prepareCall(request).loadAsync(new ConversationsDisplayCallback<ReviewTokensResponse>() {
            @Override
            public void onSuccess(@NonNull ReviewTokensResponse response) {
                if (response.getData() != null && !response.getData().isEmpty()) {
                    view.showTokens(response.getData());
                } else {
                    view.showDialogWithMessage("No review tokens found for this product.");
                }
            }

            @Override
            public void onFailure(@NonNull ConversationsException exception) {
                view.showDialogWithMessage(exception.getErrorListMessages());
            }
        });
    }
}
