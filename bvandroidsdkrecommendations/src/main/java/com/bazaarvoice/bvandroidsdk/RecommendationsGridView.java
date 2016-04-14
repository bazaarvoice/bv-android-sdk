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
import android.widget.AbsListView;
import android.widget.GridView;

/**
 * Bazaarvoice Provided {@link GridView} to display {@link RecommendationView} objects
 */
public final class RecommendationsGridView extends GridView implements View.OnAttachStateChangeListener, AbsListView.OnScrollListener {

    private AbsListView.OnScrollListener theirScrollListener;

    private boolean hasInteracted = false;
    private boolean seen = false;

    public RecommendationsGridView(Context context) {
        super(context);
        init();
    }

    public RecommendationsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecommendationsGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecommendationsGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        super.setOnScrollListener(this);
        addOnAttachStateChangeListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnScrollListener(OnScrollListener l) {
        theirScrollListener = l;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewAttachedToWindow(View v) {
        RecommendationsAnalyticsManager.sendEmbeddedPageView(EmbeddedPageViewSchema.ReportingGroup.GRIDVIEW);
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (hasInteracted && scrollState == SCROLL_STATE_IDLE) {
            BVSDK.getInstance().getAnalyticsManager().sendBvViewGroupInteractedWithEvent(RecommendationUsedFeatureSchema.Component.GRIDVIEW);
        }

        if (!hasInteracted && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            BVSDK.getInstance().getAnalyticsManager().sendBvViewGroupInteractedWithEvent(RecommendationUsedFeatureSchema.Component.GRIDVIEW);
            hasInteracted = true;
        }

        if (theirScrollListener != null) {
            theirScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (theirScrollListener != null) {
            theirScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (!seen) {
            seen = true;
            BVSDK.getInstance().getAnalyticsManager().sendBvViewGroupAddedToHierarchyEvent(RecommendationUsedFeatureSchema.Component.GRIDVIEW);
        }
    }

}
