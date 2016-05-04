/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Bazaarvoice provided {@link FrameLayout} to contain a single CurationsFeedItem
 * This view will allow Bazaarvoice to receive feedback
 * about the users interaction with the item in order to help build ROI reports
 */
public final class CurationsView extends BVView implements BVViewEventListener {

    private static final String TAG = CurationsView.class.getSimpleName();

    private CurationsFeedItem curationsFeedItem;

    public CurationsView(Context context) {
        super(context);
    }

    public CurationsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurationsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CurationsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    void init() {
        super.init();
        super.setEventListener(this);
    }

    /**
     * Setting the CurationsFeedItem is required to provide proper analytics.
     * @param curationsFeedItem The item being shown in the view.
     */
    public void setCurationsFeedItem(CurationsFeedItem curationsFeedItem) {
        this.curationsFeedItem = curationsFeedItem;
    }


    @Override
    public void onImpression() {
        if (curationsFeedItem != null) {
            CurationsAnalyticsManager.sendUGCImpressionEvent(curationsFeedItem);
        }
    }

    @Override
    public void onConversion() {
        if (curationsFeedItem != null) {
            CurationsAnalyticsManager.sendUsedFeatureEventTapped(curationsFeedItem);
        }
    }
}
