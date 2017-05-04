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
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * {@link android.support.v7.widget.RecyclerView} container for
 * many reviews providing usage Analytic events.
 *
 * @param <RequestType> Type of {@link ConversationsDisplayRequest}
 * @param <ResponseType> Type of {@link ConversationsDisplayResponse}
 */
public abstract class ConversationsDisplayRecyclerView<RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> extends BVRecyclerView implements BVConversationsClient.DisplayLoader<RequestType, ResponseType> {
    LoadCallDisplay<RequestType, ResponseType> call;
    String productId;
    boolean onScreen = false;

    ConversationsDisplayRecyclerView(Context context) {
        super(context);
    }

    ConversationsDisplayRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    ConversationsDisplayRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void loadAsync(LoadCallDisplay<RequestType, ResponseType> call, ConversationsCallback<ResponseType> callback) {
        final RequestType request = call.getRequest();
        productId = getProductIdFromRequest(request);
        this.call = call;
        this.call.loadAsync(callback);
        trySendUsedFeatureInViewEvent();
    }

    abstract String getProductIdFromRequest(RequestType requestType);

    @Override
    public void onFirstTimeOnScreen() {
        this.onScreen = true;
        trySendUsedFeatureInViewEvent();
    }

    @Override
    public void onViewGroupInteractedWith() {
        ConversationsAnalyticsManager convAnalyticsManager = ConversationsAnalyticsManager.getInstance(BVSDK.getInstance());
        convAnalyticsManager.sendUsedFeatureScrolledEvent(productId, getBvProductType());
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
        if (onScreen && productId != null) {
            ConversationsAnalyticsManager convAnalyticsManager = ConversationsAnalyticsManager.getInstance(BVSDK.getInstance());
            convAnalyticsManager.sendUsedFeatureInViewEvent(
                productId, getContainerId(), getBvProductType());
        }
    }

    @Override
    public String getProductId() {
        return productId;
    }

    abstract BVEventValues.BVProductType getBVProductType();

    abstract String getContainerId();

    abstract BVEventValues.BVProductType getBvProductType();
}