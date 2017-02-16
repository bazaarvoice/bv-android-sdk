/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
final class CurationsView extends BVView {

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

    /**
     * Setting the CurationsFeedItem is required to provide proper analytics.
     * @param curationsFeedItem The item being shown in the view.
     */
    public void setCurationsFeedItem(CurationsFeedItem curationsFeedItem) {
        this.curationsFeedItem = curationsFeedItem;
    }

    @Override
    public String getProductId() {
        if (curationsFeedItem == null) {
            throw new IllegalStateException("Must associate CurationsFeedItem with CurationsView");
        }
        return curationsFeedItem.getProductId();
    }

    @Override
    public void onAddedToViewHierarchy() {
        CurationsAnalyticsManager.sendUGCImpressionEvent(curationsFeedItem);
    }

    @Override
    public void onTap() {
        if (curationsFeedItem == null) {
            throw new IllegalStateException("Must associate CurationsFeedItem with CurationsView");
        }
        CurationsAnalyticsManager.sendUsedFeatureEventTapped(curationsFeedItem);
    }
}
