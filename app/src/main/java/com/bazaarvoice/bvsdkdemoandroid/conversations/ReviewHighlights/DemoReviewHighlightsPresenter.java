package com.bazaarvoice.bvsdkdemoandroid.conversations.ReviewHighlights;

import androidx.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsException;
import com.bazaarvoice.bvandroidsdk.QuestionAndAnswerRequest;
import com.bazaarvoice.bvandroidsdk.QuestionAndAnswerResponse;
import com.bazaarvoice.bvandroidsdk.ReviewHighlightsRequest;
import com.bazaarvoice.bvandroidsdk.ReviewHighlightsResponse;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;

public class DemoReviewHighlightsPresenter implements ConversationsDisplayCallback<ReviewHighlightsResponse> {
    private BVConversationsClient.DisplayLoader<ReviewHighlightsRequest, ReviewHighlightsResponse> loader;
    private BVConversationsClient conversationsClient;
    private String productId;
    private boolean forceAPICall;
    private DemoClient demoClient;
    private DemoMockDataUtil demoMockDataUtil;

    public DemoReviewHighlightsPresenter(String productId, boolean forceAPICall, BVConversationsClient bvConversationsClient, DemoClient demoClient, DemoMockDataUtil demoMockDataUtil,BVConversationsClient.DisplayLoader<ReviewHighlightsRequest, ReviewHighlightsResponse> loader) {
        this.conversationsClient = bvConversationsClient;
        this.demoClient = demoClient;
        this.demoMockDataUtil = demoMockDataUtil;
        this.productId = productId;
        this.forceAPICall = forceAPICall;
        this.loader = loader;



    }
    //@Override
    public void loadQuestions(boolean forceRefresh) {
        ReviewHighlightsRequest request = new ReviewHighlightsRequest.Builder(productId)
                .build();
        conversationsClient.prepareCall(request).loadAsync(new ConversationsDisplayCallback<ReviewHighlightsResponse>() {
            @Override
            public void onSuccess(@NonNull ReviewHighlightsResponse response) {

            }

            @Override
            public void onFailure(@NonNull ConversationsException exception) {

            }
        });
            //  loader.loadAsync(conversationsClient.prepareCall(request), this);
          // loader.loadAsync();
    }



    @Override
    public void onSuccess(@NonNull ReviewHighlightsResponse response) {

    }

    @Override
    public void onFailure(@NonNull ConversationsException exception) {

    }
}
