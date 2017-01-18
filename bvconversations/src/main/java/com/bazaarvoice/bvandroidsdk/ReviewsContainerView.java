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

import java.lang.ref.WeakReference;

/**
 * {@link android.widget.FrameLayout} container for many {@link ReviewView}s
 * providing usage Analytic events.
 */
public final class ReviewsContainerView extends BVContainerView implements BVConversationsClient.DisplayLoader<ReviewsRequest, ReviewResponse>, EventView.EventViewListener<ReviewsContainerView>, EventView.ProductView {
    private WeakReference<ConversationsCallback<ReviewResponse>> delegateCbWeakRef;
    private String productId;
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
        final ReviewsRequest reviewsRequest = call.getRequest();
        productId = reviewsRequest.getProductId();
        delegateCbWeakRef = new WeakReference<ConversationsCallback<ReviewResponse>>(callback);
        call.loadAsync(receiverCb);
        trySendUsedFeatureInViewEvent();
    }

    private ConversationsCallback<ReviewResponse> receiverCb = new ConversationsCallback<ReviewResponse>() {
        @Override
        public void onSuccess(ReviewResponse response) {
            ConversationsCallback<ReviewResponse> delegateCb = delegateCbWeakRef.get();
            if (delegateCb == null) {
                return;
            }
            delegateCbWeakRef.clear();
            delegateCb.onSuccess(response);
        }

        @Override
        public void onFailure(BazaarException exception) {
            ConversationsCallback<ReviewResponse> delegateCb = delegateCbWeakRef.get();
            if (delegateCb == null) {
                return;
            }
            delegateCbWeakRef.clear();
            delegateCb.onFailure(exception);
        }
    };

    @Override
    public void onVisibleOnScreenStateChanged(boolean onScreen) {

    }

    @Override
    public void onFirstTimeOnScreen() {
        this.onScreen = true;
        trySendUsedFeatureInViewEvent();
    }

    private void trySendUsedFeatureInViewEvent() {
        if (onScreen && productId != null) {
            ConversationsAnalyticsManager.sendUsedFeatureInViewEvent(productId, MagpieBvProduct.RATINGS_AND_REVIEWS);
        }
    }

    @Override
    public String getProductId() {
        return productId;
    }
}
