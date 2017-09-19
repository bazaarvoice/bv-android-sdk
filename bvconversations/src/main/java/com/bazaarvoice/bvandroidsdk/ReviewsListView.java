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
 * @deprecated Used the {@link ReviewsRecyclerView} instead
 *
 * {@link android.widget.ListView} container for many
 * {@link ReviewView}s providing usage Analytic events.
 */
public final class ReviewsListView extends BVListView implements BVConversationsClient.DisplayLoader<ReviewsRequest, ReviewResponse> {
    private LoadCall call;
    private ConversationsAnalyticsManager convAnMan;
    private String productId;
    private boolean onScreen = false;

    public ReviewsListView(Context context) {
        super(context);
    }

    public ReviewsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReviewsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ReviewsListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void loadAsync(LoadCallDisplay<ReviewsRequest, ReviewResponse> call, ConversationsCallback<ReviewResponse> callback) {
        this.call = call;
        convAnMan = call.getConversationsAnalyticsManager();
        final ReviewsRequest reviewsRequest = call.getRequest();
        productId = reviewsRequest.getProductId();
        call.loadAsync(callback);
        trySendUsedFeatureInViewEvent();
    }

    @Override
    public void loadAsync(LoadCallDisplay<ReviewsRequest, ReviewResponse> call, ConversationsDisplayCallback<ReviewResponse> callback) {
        this.call = call;
        convAnMan = call.getConversationsAnalyticsManager();
        final ReviewsRequest reviewsRequest = call.getRequest();
        productId = reviewsRequest.getProductId();
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
            convAnMan.sendUsedFeatureScrolledEvent(productId, BVEventValues.BVProductType.CONVERSATIONS_REVIEWS);
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
                productId, "ReviewsListView", BVEventValues.BVProductType.CONVERSATIONS_REVIEWS);
        }
    }

    @Override
    public String getProductId() {
        return productId;
    }
}
