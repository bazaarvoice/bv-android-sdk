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
import android.widget.FrameLayout;

/**
 * Bazaarvoice Provided {@link FrameLayout} to display {@link BVView} objects
 */
class BVContainerView extends FrameLayout implements View.OnAttachStateChangeListener {
    private static final String TAG = BVContainerView.class.getSimpleName();

    private boolean seen = false;
    private BVViewGroupEventListener eventListener;

    public BVContainerView(Context context) {
        super(context);
        init();
    }

    public BVContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BVContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BVContainerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        super.addOnAttachStateChangeListener(this);
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
