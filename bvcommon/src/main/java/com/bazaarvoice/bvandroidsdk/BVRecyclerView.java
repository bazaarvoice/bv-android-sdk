/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Bazaarvoice Provided {@link android.support.v7.widget.RecyclerView} to display {@link BVView} objects
 */
abstract class BVRecyclerView extends RecyclerView implements BVViewGroupEventListener, EventView.EventViewListener<BVRecyclerView>, EventView.ProductView {

    private static final String TAG = BVRecyclerView.class.getSimpleName();

    private boolean hasInteracted = false;
    private boolean seen = false;

    public BVRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public BVRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BVRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context context, @Nullable AttributeSet attr) {
        super.addOnScrollListener(onScrollListener);
        setWillNotDraw(false);
        EventView.bind(this, this, this);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (hasInteracted && newState == SCROLL_STATE_IDLE) {
                onViewGroupInteractedWith();
            }

            if (!hasInteracted && newState == SCROLL_STATE_DRAGGING) {
                onViewGroupInteractedWith();
                hasInteracted = true;
            }
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (!seen) {
            seen = true;
            onAddedToViewHierarchy();
        }
    }

    // TODO handle recyclerview nested scroll

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVisibleOnScreenStateChanged(boolean onScreen) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTap() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAddedToViewHierarchy() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewGroupInteractedWith() {

    }

    @Override
    public void onFirstTimeOnScreen() {

    }
}
