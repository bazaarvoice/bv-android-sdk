package com.bazaarvoice.bvsdkdemoandroid.conversations.matchedtokens;

import androidx.annotation.NonNull;
import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsException;
import com.bazaarvoice.bvandroidsdk.MatchedTokensRequest;
import com.bazaarvoice.bvandroidsdk.MatchedTokensResponse;

public class DemoMatchedTokensPresenter implements DemoMatchedTokensContract.UserActionsListener {
    private static final String SAMPLE_REVIEW_TEXT =
            "The fit is Fragrance, Projection is very nice, you may like it";
    private final BVConversationsClient conversationsClient;
    private final String productId;
    private final DemoMatchedTokensContract.View view;

    public DemoMatchedTokensPresenter(DemoMatchedTokensContract.View view,
                                      String productId,
                                      BVConversationsClient conversationsClient) {
        this.view = view;
        this.productId = productId;
        this.conversationsClient = conversationsClient;
    }

    @Override
    public void loadMatchedTokens(boolean forceRefresh) {
        MatchedTokensRequest request = new MatchedTokensRequest.Builder(productId)
                .addReviewText(SAMPLE_REVIEW_TEXT)
                .build();
        conversationsClient.prepareCall(request).loadAsync(new ConversationsDisplayCallback<MatchedTokensResponse>() {
            @Override
            public void onSuccess(@NonNull MatchedTokensResponse response) {
                if (response.getData() != null && !response.getData().isEmpty()) {
                    view.showTokens(response.getData());
                } else {
                    view.showDialogWithMessage("No matched tokens found for this product.");
                }
            }

            @Override
            public void onFailure(@NonNull ConversationsException exception) {
                view.showDialogWithMessage(exception.getErrorListMessages());
            }
        });
    }
}
