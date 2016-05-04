/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;

/**
 * Bazaarvoice Provided {@link GridView} to display {@link RecommendationView} objects
 */
public final class RecommendationsGridView extends BVGridView implements View.OnAttachStateChangeListener, AbsListView.OnScrollListener, BVViewGroupEventListener {

    public RecommendationsGridView(Context context) {
        super(context);
    }

    public RecommendationsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendationsGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecommendationsGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {
        super.init();
        super.setEventListener(this);
    }

    @Override
    public void onViewGroupInteractedWith() {
        RecommendationsAnalyticsManager.sendBvViewGroupInteractedWithEvent(ReportingGroup.GRIDVIEW);
    }

    @Override
    public void onEmbeddedPageView() {
        RecommendationsAnalyticsManager.sendEmbeddedPageView(ReportingGroup.GRIDVIEW);
    }

    @Override
    public void onViewGroupAddedToHierarchy() {
        RecommendationsAnalyticsManager.sendBvViewGroupAddedToHierarchyEvent(ReportingGroup.GRIDVIEW);
    }
}
