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

/**
 * Bazaarvoice Provided {@link RecyclerView} to display {@link BVView} objects
 */
class BVRecyclerView extends RecyclerView implements View.OnAttachStateChangeListener {

    private static final String TAG = BVRecyclerView.class.getSimpleName();

    private boolean hasInteracted = false;
    private boolean seen = false;
    private BVViewGroupEventListener eventListener;

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
        addOnAttachStateChangeListener(this);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (hasInteracted && newState == SCROLL_STATE_IDLE) {
                    eventListener.onViewGroupInteractedWith();
                }

                if (!hasInteracted && newState == SCROLL_STATE_DRAGGING) {
                    eventListener.onViewGroupInteractedWith();
                    hasInteracted = true;
                }
            }
        });
    }

    void setEventListener(BVViewGroupEventListener eventListener){
        this.eventListener = eventListener;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewAttachedToWindow(View v) {
        eventListener.onEmbeddedPageView();
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
        if (!seen) {
            seen = true;
            eventListener.onViewGroupAddedToHierarchy();
        }
    }

}
