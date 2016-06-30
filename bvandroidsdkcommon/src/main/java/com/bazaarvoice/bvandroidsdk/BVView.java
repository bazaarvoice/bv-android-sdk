/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import java.util.HashSet;
import java.util.Set;

/**
 * Bazaarvoice provided {@link FrameLayout} to contain a single Bazaarvoice
 * recommended product. This view will allow Bazaarvoice to receive feedback
 * about the users interaction with the Product in order to help influence
 * future recommendations.
 */
abstract class BVView extends FrameLayout {

    private static final String TAG = BVView.class.getSimpleName();

    private BvOnScreenListener onScreenListener;

    public BVView(Context context) {
        super(context);
        init();
    }

    public BVView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BVView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BVView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    void init() {
        setWillNotDraw(false);
        this.onScreenListener = new BvOnScreenListener(getEventListener());
        getViewTreeObserver().addOnGlobalLayoutListener(new EventView.GlobalLayoutListener<BVView>(this, onScreenListener));
        getViewTreeObserver().addOnScrollChangedListener(new EventView.ScrollChangeListener<BVView>(this, onScreenListener));
    }

    private static class BvOnScreenListener implements EventView.OnScreenListener<BVView> {
        private final Set<String> seenIds = new HashSet<>();
        private final BVViewEventListener eventListener;

        BvOnScreenListener(BVViewEventListener eventListener) {
            this.eventListener = eventListener;
        }

        @Override
        public void onScreen(BVView bvView) {
            if (!seenIds.contains(bvView.getProductId())) {
                eventListener.onImpression();
                seenIds.add(bvView.getProductId());
            }
        }
    }

    abstract BVViewEventListener getEventListener();

    abstract String getProductId();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                getEventListener().onConversion();
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
