/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;

public final class QuestionsContainerView extends BVContainerView implements BVConversationsClient.DisplayLoader<QuestionAndAnswerRequest, QuestionAndAnswerResponse>, EventView.EventViewListener<QuestionsContainerView>, EventView.ProductView {
    private WeakReference<ConversationsCallback<QuestionAndAnswerResponse>> delegateCbWeakRef;
    private String productId;
    private boolean onScreen = false;

    public QuestionsContainerView(Context context) {
        super(context);
    }

    public QuestionsContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuestionsContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public QuestionsContainerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {
        super.init();
        EventView.bind(this, this, this);
    }

    @Override
    public void loadAsync(LoadCallDisplay<QuestionAndAnswerRequest, QuestionAndAnswerResponse> call, ConversationsCallback<QuestionAndAnswerResponse> callback) {
        final QuestionAndAnswerRequest qAndARequest = call.getRequest();
        productId = qAndARequest.getProductId();
        delegateCbWeakRef = new WeakReference<ConversationsCallback<QuestionAndAnswerResponse>>(callback);
        call.loadAsync(receiverCb);
        trySendUsedFeatureInViewEvent();
    }

    private ConversationsCallback<QuestionAndAnswerResponse> receiverCb = new ConversationsCallback<QuestionAndAnswerResponse>() {
        @Override
        public void onSuccess(QuestionAndAnswerResponse response) {
            ConversationsCallback<QuestionAndAnswerResponse> delegateCb = delegateCbWeakRef.get();
            if (delegateCb == null) {
                return;
            }
            delegateCbWeakRef.clear();
            delegateCb.onSuccess(response);
        }

        @Override
        public void onFailure(BazaarException exception) {
            ConversationsCallback<QuestionAndAnswerResponse> delegateCb = delegateCbWeakRef.get();
            if (delegateCb == null) {
                return;
            }
            delegateCbWeakRef.clear();
            delegateCb.onFailure(exception);
        }
    };

    @Override
    public void onFirstTimeOnScreen() {
        this.onScreen = true;
        trySendUsedFeatureInViewEvent();
    }

    private void trySendUsedFeatureInViewEvent() {
        if (onScreen && productId != null) {
            ConversationsAnalyticsManager.sendUsedFeatureInViewEvent(productId, MagpieBvProduct.QUESTIONS_AND_ANSWERS);
        }
    }

    @Override
    public String getProductId() {
        return productId;
    }

    @Override
    public void onVisibleOnScreenStateChanged(boolean onScreen) {

    }

}
