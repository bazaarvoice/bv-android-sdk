/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;

public abstract class ConversationsDisplayRecyclerView<RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> extends BVRecyclerView implements BVConversationsClient.DisplayLoader<RequestType, ResponseType> {

    WeakReference<ConversationsCallback<ResponseType>> delegateCbWeakRef;
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
        delegateCbWeakRef = new WeakReference<>(callback);
        call.loadAsync(receiverCb);
        trySendUsedFeatureInViewEvent();
    }

    abstract String getProductIdFromRequest(RequestType requestType);

    private ConversationsCallback<ResponseType> receiverCb = new ConversationsCallback<ResponseType>() {
        @Override
        public void onSuccess(ResponseType response) {
            ConversationsCallback<ResponseType> delegateCb = delegateCbWeakRef.get();
            if (delegateCb == null) {
                return;
            }
            delegateCbWeakRef.clear();
            delegateCb.onSuccess(response);
        }

        @Override
        public void onFailure(BazaarException exception) {
            ConversationsCallback<ResponseType> delegateCb = delegateCbWeakRef.get();
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
        ConversationsAnalyticsManager.sendUsedFeatureScrolledEvent(productId, getMagpieBvProduct());
    }

    private void trySendUsedFeatureInViewEvent() {
        if (onScreen && productId != null) {
            ConversationsAnalyticsManager.sendUsedFeatureInViewEvent(productId, getMagpieBvProduct());
        }
    }

    @Override
    public String getProductId() {
        return productId;
    }

    abstract MagpieBvProduct getMagpieBvProduct();
}