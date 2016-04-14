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
public final class RecommendationView extends FrameLayout {

    private static final String TAG = RecommendationView.class.getSimpleName();

    private BVProduct bvProduct;

    public RecommendationView(Context context) {
        super(context);
        init();
    }

    public RecommendationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecommendationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecommendationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        setWillNotDraw(false);
    }

    /**
     * @param bvProduct Bazaarvoice recommended product that should be associated with this view
     */
    public void setBvProduct(BVProduct bvProduct) {
        this.bvProduct = bvProduct;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bvProduct != null) {
            bvProduct.recordImpression();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (bvProduct != null) {
                    BVSDK.getInstance().sendProductConversionEvent(bvProduct);
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

}
