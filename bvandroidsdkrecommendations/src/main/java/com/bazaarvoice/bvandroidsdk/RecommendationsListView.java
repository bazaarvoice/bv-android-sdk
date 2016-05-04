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
import android.widget.ListView;

/**
 * Bazaarvoice Provided {@link ListView} to display {@link RecommendationView} objects
 */
public final class RecommendationsListView extends BVListView implements View.OnAttachStateChangeListener, AbsListView.OnScrollListener, BVViewGroupEventListener {

    private static final String TAG = RecommendationsListView.class.getSimpleName();

    public RecommendationsListView(Context context) {
        super(context);
    }

    public RecommendationsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendationsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecommendationsListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {
        super.init();
        super.setEventListener(this);
    }

    @Override
    public void onViewGroupInteractedWith() {
        RecommendationsAnalyticsManager.sendBvViewGroupInteractedWithEvent(ReportingGroup.LISTVIEW);
    }

    @Override
    public void onEmbeddedPageView() {
        RecommendationsAnalyticsManager.sendEmbeddedPageView(ReportingGroup.LISTVIEW);
    }

    @Override
    public void onViewGroupAddedToHierarchy() {
        RecommendationsAnalyticsManager.sendBvViewGroupAddedToHierarchyEvent(ReportingGroup.LISTVIEW);
    }
}
