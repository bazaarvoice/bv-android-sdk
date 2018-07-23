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
 * {@link android.widget.FrameLayout} container for many {@link ReviewView}s
 * providing usage Analytic events.
 * @deprecated please use BVUiReviewsContainerView from conversation-ui module
 */
@Deprecated
public final class ReviewsContainerView extends BVContainerView implements BVConversationsClient.DisplayLoader<ReviewsRequest, ReviewResponse>, EventView.EventViewListener<ReviewsContainerView>, EventView.ProductView {
    private LoadCall call;
    private String productId;
    private ConversationsAnalyticsManager convAnMan;
    private boolean onScreen = false;

    public ReviewsContainerView(Context context) {
        super(context);
    }

    public ReviewsContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReviewsContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ReviewsContainerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {
        super.init();
        EventView.bind(this, this, this);
    }

    @Override
    public void loadAsync(LoadCallDisplay<ReviewsRequest, ReviewResponse> call, ConversationsCallback<ReviewResponse> callback) {
        this.call = call;
        final ReviewsRequest reviewsRequest = call.getRequest();
        productId = reviewsRequest.getProductId();
        convAnMan = call.getConversationsAnalyticsManager();
        call.loadAsync(callback);
        trySendUsedFeatureInViewEvent();
    }

    @Override
    public void loadAsync(LoadCallDisplay<ReviewsRequest, ReviewResponse> call, ConversationsDisplayCallback<ReviewResponse> callback) {
        this.call = call;
        final ReviewsRequest reviewsRequest = call.getRequest();
        productId = reviewsRequest.getProductId();
        convAnMan = call.getConversationsAnalyticsManager();
        call.loadAsync(callback);
        trySendUsedFeatureInViewEvent();
    }

    @Override
    public void onVisibleOnScreenStateChanged(boolean onScreen) {

    }

    @Override
    public void onFirstTimeOnScreen() {
        this.onScreen = true;
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

    private void trySendUsedFeatureInViewEvent() {
        if (onScreen && productId != null && convAnMan != null) {
            convAnMan.sendUsedFeatureInViewEvent(
                productId, "ReviewsContainerView", BVEventValues.BVProductType.CONVERSATIONS_REVIEWS);
        }
    }

    @Override
    public String getProductId() {
        return productId;
    }
}
