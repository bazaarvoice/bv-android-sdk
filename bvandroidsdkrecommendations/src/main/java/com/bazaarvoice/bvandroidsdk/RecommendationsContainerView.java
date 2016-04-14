/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Collections;
import java.util.List;

/**
 * Bazaarvoice Provided {@link FrameLayout} to display {@link RecommendationView} objects
 */
public class RecommendationsContainerView extends FrameLayout implements View.OnAttachStateChangeListener {
    private static final String TAG = RecommendationsContainerView.class.getSimpleName();

    private boolean seen = false;
    private List<BVProduct> bvProducts = Collections.emptyList();

    public RecommendationsContainerView(Context context) {
        super(context);
        init();
    }

    public RecommendationsContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecommendationsContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecommendationsContainerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        super.addOnAttachStateChangeListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewAttachedToWindow(View v) {
        RecommendationsAnalyticsManager.sendEmbeddedPageView(EmbeddedPageViewSchema.ReportingGroup.CUSTOM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewDetachedFromWindow(View v) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (!seen && bvProducts.size() > 0) {
            seen = true;
            BVSDK.getInstance().getAnalyticsManager().sendBvViewGroupAddedToHierarchyEvent(RecommendationUsedFeatureSchema.Component.CUSTOM);
        }
    }

}
