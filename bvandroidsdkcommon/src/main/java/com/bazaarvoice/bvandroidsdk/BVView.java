/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Bazaarvoice provided {@link FrameLayout} to contain a single Bazaarvoice
 * recommended product. This view will allow Bazaarvoice to receive feedback
 * about the users interaction with the Product in order to help influence
 * future recommendations.
 */
abstract class BVView extends FrameLayout implements BVViewEventListener, EventView.EventViewListener<BVView>, EventView.ProductView {

    private static final String TAG = BVView.class.getSimpleName();

    private boolean seenInHierarchy = false;

    BVView(Context context) {
        super(context);
        init();
    }

    BVView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    BVView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    BVView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    void init() {
        setWillNotDraw(false);
        EventView.bind(this, this, this);
    }

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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                onTap();
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (!seenInHierarchy) {
            seenInHierarchy = true;
            onAddedToViewHierarchy();
        }
    }

    @Override
    public void onFirstTimeOnScreen() {

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
}
