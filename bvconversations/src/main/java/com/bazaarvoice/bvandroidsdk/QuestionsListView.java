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
 * @deprecated Used the {@link QuestionsRecyclerView} instead
 *
 * {@link android.widget.ListView} container for many
 * {@link QuestionView}s providing usage Analytic events.
 */
public final class QuestionsListView extends BVListView implements BVConversationsClient.DisplayLoader<QuestionAndAnswerRequest, QuestionAndAnswerResponse> {
    private LoadCall call;
    private ConversationsAnalyticsManager convAnMan;
    private String productId;
    private boolean onScreen = false;

    public QuestionsListView(Context context) {
        super(context);
    }

    public QuestionsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuestionsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public QuestionsListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
    public void onFirstTimeOnScreen() {
        this.onScreen = true;
        trySendUsedFeatureInViewEvent();
    }

    @Override
    public void onViewGroupInteractedWith() {
        if (convAnMan != null) {
            convAnMan.sendUsedFeatureScrolledEvent(productId, BVEventValues.BVProductType.CONVERSATIONS_QANDA);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (call != null) {
            call.cancel();
            call = null;
        }
    }

    private void trySendUsedFeatureInViewEvent() {
        if (onScreen && productId != null && convAnMan != null) {
            convAnMan.sendUsedFeatureInViewEvent(
                productId, "QuestionsListView", BVEventValues.BVProductType.CONVERSATIONS_QANDA);
        }
    }

    @Override
    public String getProductId() {
        return productId;
    }
}
