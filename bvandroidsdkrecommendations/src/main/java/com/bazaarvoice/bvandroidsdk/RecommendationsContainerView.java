/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Collections;
import java.util.List;

/**
 * Bazaarvoice Provided {@link FrameLayout} to display {@link RecommendationView} objects
 */
public class RecommendationsContainerView extends BVContainerView implements View.OnAttachStateChangeListener, BVViewGroupEventListener {
    private static final String TAG = RecommendationsContainerView.class.getSimpleName();

    private List<BVProduct> bvProducts = Collections.emptyList();

    public RecommendationsContainerView(Context context) {
        super(context);
    }

    public RecommendationsContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendationsContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecommendationsContainerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {
        super.init();
        setEventListener(this);
    }

    @Override
    public void onViewGroupInteractedWith() {

    }

    @Override
    public void onEmbeddedPageView() {
        RecommendationsAnalyticsManager.sendEmbeddedPageView(ReportingGroup.CUSTOM);
    }

    @Override
    public void onViewGroupAddedToHierarchy() {
        RecommendationsAnalyticsManager.sendBvViewGroupAddedToHierarchyEvent(ReportingGroup.CUSTOM);
    }
}
