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

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Bazaarvoice Provided {@link android.support.v7.widget.RecyclerView} to display {@link CurationsView} objects
 */
public final class CurationsRecyclerView extends BVRecyclerView implements CurationsFeedCallback {

    private String widgetId = "MainGrid";
    private String requestExternalId;
    private CurationsFeedRequest request;
    private WeakReference<CurationsFeedCallback> cbWeakRef;


    public CurationsRecyclerView(Context context) {
        super(context);
    }

    public CurationsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CurationsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Get curations feed using specified parameters
     * @param request The request is used to specify parameters which will be used in the Curations GET request
     * @param widgetId When presenting more than one BVCurationsCollectionView in a ViewController, provide unique names each each configuration type.
     * This is used for analytics reporting to calculate performance among different BVCurationsCollectionView configurations. If only providing one collection view, just set to nil.
     * @param cb Callback is used to handle successful and unsuccessful requests
     * <em>Note:</em> This method keeps a weak reference to the {@link CurationsFeedRequest} instance and will be
     * garbage collected if you do not keep a strong reference to it. }.
     */
    public void getCurationsFeedItems(final CurationsFeedRequest request, final String widgetId, final CurationsFeedCallback cb){
        this.requestExternalId = request.externalId;
        this.widgetId = widgetId;
        this.request = request;
        this.cbWeakRef = new WeakReference(cb);

        BVCurations curations = new BVCurations();
        curations.getCurationsFeedItems(request, this);
    }

    @Override
    public String getProductId() {
        return requestExternalId;
    }

    @Override
    public void onViewGroupInteractedWith() {
        CurationsAnalyticsManager.sendUsedFeatureEventScrolled(requestExternalId, ReportingGroup.RECYCLERVIEW);
    }

    @Override
    public void onVisibleOnScreenStateChanged(boolean onScreen) {
        if (onScreen) {
            CurationsAnalyticsManager.sendBvViewGroupAddedToHierarchyEvent(requestExternalId, widgetId, ReportingGroup.RECYCLERVIEW);
        }
    }

    @Override
    public void onSuccess(List<CurationsFeedItem> feedItems) {
        CurationsAnalyticsManager.sendEmbeddedPageView(request.externalId, ReportingGroup.RECYCLERVIEW);
        CurationsFeedCallback cb = cbWeakRef.get();
        if (cb != null) {
            cb.onSuccess(feedItems);
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        CurationsFeedCallback cb = cbWeakRef.get();
        if (cb != null) {
            cb.onFailure(throwable);
        }
    }
}
