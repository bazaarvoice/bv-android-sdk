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
 * Bazaarvoice Provided {@link RecyclerView} to display {@link BVView} objects
 */
abstract class BVRecyclerView extends RecyclerView {

    private static final String TAG = BVRecyclerView.class.getSimpleName();

    private boolean hasInteracted = false;
    private boolean seen = false;

    public BVRecyclerView(Context context) {
        super(context);
        init();
    }

    public BVRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BVRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
        super.addOnScrollListener(onScrollListener);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (hasInteracted && newState == SCROLL_STATE_IDLE) {
                getEventListener().onViewGroupInteractedWith();
            }

            if (!hasInteracted && newState == SCROLL_STATE_DRAGGING) {
                getEventListener().onViewGroupInteractedWith();
                hasInteracted = true;
            }
        }
    };

    abstract BVViewGroupEventListener getEventListener();

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (!seen) {
            seen = true;
            getEventListener().onViewGroupAddedToHierarchy();
        }
    }

}
