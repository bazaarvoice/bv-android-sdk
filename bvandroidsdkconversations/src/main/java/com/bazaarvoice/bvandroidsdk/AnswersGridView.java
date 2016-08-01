/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.util.AttributeSet;

public final class AnswersGridView extends BVGridView {
    private String productId = "";

    public AnswersGridView(Context context) {
        super(context);
    }

    public AnswersGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnswersGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AnswersGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFirstTimeOnScreen() {
        ConversationsAnalyticsManager.sendUsedFeatureInViewEvent(productId, MagpieBvProduct.QUESTIONS_AND_ANSWERS);
    }

    @Override
    public void onViewGroupInteractedWith() {
        ConversationsAnalyticsManager.sendUsedFeatureScrolledEvent(productId, MagpieBvProduct.QUESTIONS_AND_ANSWERS);
    }

    @Override
    public String getProductId() {
        return productId;
    }
}
