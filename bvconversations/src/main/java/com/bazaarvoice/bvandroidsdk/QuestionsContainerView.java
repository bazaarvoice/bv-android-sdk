/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.util.AttributeSet;

/**
 * {@link android.widget.FrameLayout} container for many {@link QuestionView}s
 * providing usage Analytic events.
 */
public final class QuestionsContainerView extends BVContainerView implements BVConversationsClient.DisplayLoader<QuestionAndAnswerRequest, QuestionAndAnswerResponse>, EventView.EventViewListener<QuestionsContainerView>, EventView.ProductView {
    private LoadCallDisplay call;
    private String productId;
    private ConversationsAnalyticsManager convAnMan;
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
        this.call = call;
        convAnMan = call.getConversationsAnalyticsManager();
        final QuestionAndAnswerRequest qAndARequest = call.getRequest();
        productId = qAndARequest.getProductId();
        call.loadAsync(callback);
        trySendUsedFeatureInViewEvent();
    }

    @Override
    public void loadAsync(LoadCallDisplay<QuestionAndAnswerRequest, QuestionAndAnswerResponse> call, ConversationsDisplayCallback<QuestionAndAnswerResponse> callback) {
        this.call = call;
        convAnMan = call.getConversationsAnalyticsManager();
        final QuestionAndAnswerRequest qAndARequest = call.getRequest();
        productId = qAndARequest.getProductId();
        call.loadAsync(callback);
        trySendUsedFeatureInViewEvent();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (call != null) {
            call.cancel();
            call = null;
        }
    }

    @Override
    public void onFirstTimeOnScreen() {
        this.onScreen = true;
        trySendUsedFeatureInViewEvent();
    }

    private void trySendUsedFeatureInViewEvent() {
        if (onScreen && productId != null && convAnMan != null) {
            convAnMan.sendUsedFeatureInViewEvent(
                productId, "QuestionsContainerView", BVEventValues.BVProductType.CONVERSATIONS_QANDA);
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
