/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collections;
import java.util.List;

/**
 * Bazaarvoice Provided {@link RecyclerView} to display {@link RecommendationView} objects
 */
public class RecommendationsRecyclerView extends RecyclerView implements View.OnAttachStateChangeListener {

    private static final String TAG = RecommendationsRecyclerView.class.getSimpleName();

    private boolean hasInteracted = false;
    private boolean seen = false;
    private List<BVProduct> bvProducts = Collections.emptyList();

    public RecommendationsRecyclerView(Context context) {
        super(context);
        init();
    }

    public RecommendationsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecommendationsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnAttachStateChangeListener(this);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (hasInteracted && newState == SCROLL_STATE_IDLE) {
                    BVSDK.getInstance().getAnalyticsManager().sendBvViewGroupInteractedWithEvent(RecommendationUsedFeatureSchema.Component.RECYCLERVIEW);
                }

                if (!hasInteracted && newState == SCROLL_STATE_DRAGGING) {
                    BVSDK.getInstance().getAnalyticsManager().sendBvViewGroupInteractedWithEvent(RecommendationUsedFeatureSchema.Component.RECYCLERVIEW);
                    hasInteracted = true;
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewAttachedToWindow(View v) {
        RecommendationsAnalyticsManager.sendEmbeddedPageView(EmbeddedPageViewSchema.ReportingGroup.RECYCLERVIEW);
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
            BVSDK.getInstance().getAnalyticsManager().sendBvViewGroupAddedToHierarchyEvent(RecommendationUsedFeatureSchema.Component.RECYCLERVIEW);
        }
    }

}
