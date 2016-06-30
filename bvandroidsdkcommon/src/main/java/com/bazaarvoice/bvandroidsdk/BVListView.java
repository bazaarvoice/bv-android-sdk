/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Bazaarvoice Provided {@link ListView} to display {@link BVView} objects
 */
abstract class BVListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = BVListView.class.getSimpleName();
    private AbsListView.OnScrollListener theirScrollListener;

    private boolean hasInteracted = false;
    private boolean seen = false;

    public BVListView(Context context) {
        super(context);
        init();
    }

    public BVListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BVListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BVListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        super.setOnScrollListener(this);
    }

    abstract BVViewGroupEventListener getEventListener();

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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (hasInteracted && scrollState == SCROLL_STATE_IDLE) {
            getEventListener().onViewGroupInteractedWith();
        }

        if (!hasInteracted && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            getEventListener().onViewGroupInteractedWith();
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
            getEventListener().onViewGroupAddedToHierarchy();
        }
    }

}
