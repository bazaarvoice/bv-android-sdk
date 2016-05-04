/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collections;
import java.util.List;

/**
 * Bazaarvoice Provided {@link RecyclerView} to display {@link RecommendationView} objects
 */
public final class RecommendationsRecyclerView extends BVRecyclerView implements View.OnAttachStateChangeListener, BVViewGroupEventListener {

    private static final String TAG = RecommendationsRecyclerView.class.getSimpleName();

    private List<BVProduct> bvProducts = Collections.emptyList();

    public RecommendationsRecyclerView(Context context) {
        super(context);
    }

    public RecommendationsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendationsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    void init() {
        super.init();
        super.setEventListener(this);
    }

    @Override
    public void onViewGroupInteractedWith() {
        RecommendationsAnalyticsManager.sendBvViewGroupInteractedWithEvent(ReportingGroup.RECYCLERVIEW);
    }

    @Override
    public void onEmbeddedPageView() {
        RecommendationsAnalyticsManager.sendEmbeddedPageView(ReportingGroup.RECYCLERVIEW);
    }

    @Override
    public void onViewGroupAddedToHierarchy() {
        if (bvProducts.size() > 0) {
            RecommendationsAnalyticsManager.sendBvViewGroupAddedToHierarchyEvent(ReportingGroup.RECYCLERVIEW);
        }
    }
}
