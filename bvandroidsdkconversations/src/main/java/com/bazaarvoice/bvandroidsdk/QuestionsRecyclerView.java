/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;

public final class QuestionsRecyclerView extends BVRecyclerView implements BVConversationsClient.DisplayLoader<QuestionAndAnswerRequest, QuestionAndAnswerResponse> {
    private WeakReference<ConversationsCallback<QuestionAndAnswerResponse>> delegateCbWeakRef;
    private String productId;
    private boolean onScreen = false;

    public QuestionsRecyclerView(Context context) {
        super(context);
    }

    public QuestionsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QuestionsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public QuestionAndAnswerResponse loadSync(LoadCallDisplay<QuestionAndAnswerRequest, QuestionAndAnswerResponse> call) throws BazaarException {
        final QuestionAndAnswerRequest qAndARequest = call.getRequest();
        productId = qAndARequest.getProductId();
        QuestionAndAnswerResponse qAndAResponse = call.loadSync();
        trySendUsedFeatureInViewEvent();
        return qAndAResponse;
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

    @Override
    public void onViewGroupInteractedWith() {
        ConversationsAnalyticsManager.sendUsedFeatureScrolledEvent(productId, MagpieBvProduct.QUESTIONS_AND_ANSWERS);
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

}