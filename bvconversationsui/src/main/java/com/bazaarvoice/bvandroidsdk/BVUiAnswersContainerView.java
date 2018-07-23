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
import android.util.AttributeSet;

/**
 * {@link android.widget.FrameLayout} container for many {@link BVUiAnswerView}s
 * providing usage Analytic events.
 */
public final class BVUiAnswersContainerView extends BVContainerView implements EventView.EventViewListener<BVUiAnswersContainerView>, EventView.ProductView {
    private String productId = "";

    public BVUiAnswersContainerView(Context context) {
        super(context);
    }

    public BVUiAnswersContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BVUiAnswersContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BVUiAnswersContainerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {
        super.init();
        EventView.bind(this, this, this);
    }

    @Override
    public void onVisibleOnScreenStateChanged(boolean onScreen) {

    }

    @Override
    public void onFirstTimeOnScreen() {
        ConversationsAnalyticsManager convAnalyticsManager = new ConversationsAnalyticsManager(
            BVSDK.getInstance().getBvPixel(),
            BVSDK.getInstance().getBvUserProvidedData().getBvConfig().getClientId());
        convAnalyticsManager.sendUsedFeatureInViewEvent(
            productId, "AnswersContainerView", BVEventValues.BVProductType.CONVERSATIONS_QANDA);
    }

    @Override
    public String getProductId() {
        return productId;
    }
}
