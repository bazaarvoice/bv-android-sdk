/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Bazaarvoice provided {@link FrameLayout} to contain a single Bazaarvoice
 * recommended product. This view will allow Bazaarvoice to receive feedback
 * about the users interaction with the Product in order to help influence
 * future recommendations.
 */
public final class RecommendationView extends BVView implements BVViewEventListener {

    private static final String TAG = RecommendationView.class.getSimpleName();

    private BVProduct bvProduct;

    public RecommendationView(Context context) {
        super(context);
    }

    public RecommendationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecommendationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    void init() {
        super.init();
    }

    /**
     * @param bvProduct Bazaarvoice recommended product that should be associated with this view
     */
    public void setBvProduct(BVProduct bvProduct) {
        this.bvProduct = bvProduct;
    }

    @Override
    String getProductId() {
        if (bvProduct == null) {
            throw new IllegalStateException("Must associate BVProduct with RecommendationView");
        }
        return bvProduct.getProductId();
    }

    @Override
    BVViewEventListener getEventListener() {
        return this;
    }

    @Override
    public void onImpression() {
        RecommendationsAnalyticsManager.sendProductImpressionEvent(bvProduct);
    }

    @Override
    public void onConversion() {
        RecommendationsAnalyticsManager.sendProductConversionEvent(bvProduct);
    }

    @Override
    public void onAddedToViewHeirarchy() {
        // no-op
    }
}
