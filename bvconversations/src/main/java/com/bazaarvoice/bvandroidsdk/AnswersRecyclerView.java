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

import androidx.annotation.Nullable;

/**
 * {@link androidx.recyclerview.widget.RecyclerView} container for
 * many {@link AnswerView}s providing usage Analytic events.
 */
public final class AnswersRecyclerView extends BVRecyclerView {

    private String productId = "";

    public AnswersRecyclerView(Context context) {
        super(context);
    }

    public AnswersRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnswersRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onFirstTimeOnScreen() {
        // TODO: Need BVConfig instance from BVConversationsClient, not global one
        // As stated in getProductId(), there is never a request associated with
        // this class, and the request instances are where we get the BVConfig from
        // This applies to all Answer Views, however only AnswerContainerView and
        // AnswerRecyclerView are supported.
        ConversationsAnalyticsManager convAnalyticsManager = new ConversationsAnalyticsManager(
            BVSDK.getInstance().getBvPixel(),
            BVSDK.getInstance().getBvUserProvidedData().getBvConfig().getClientId());
        convAnalyticsManager.sendUsedFeatureInViewEvent(
            productId, "AnswersRecyclerView", BVEventValues.BVProductType.CONVERSATIONS_QANDA);
    }

    @Override
    public void onViewGroupInteractedWith() {
        ConversationsAnalyticsManager convAnalyticsManager = new ConversationsAnalyticsManager(
            BVSDK.getInstance().getBvPixel(),
            BVSDK.getInstance().getBvUserProvidedData().getBvConfig().getClientId());
        convAnalyticsManager.sendUsedFeatureScrolledEvent(productId, BVEventValues.BVProductType.CONVERSATIONS_QANDA);
    }

    @Override
    public String getProductId() {
        // TODO: This is never being set
        // With other conversations views there is a request associated with the
        // view, and the product id is pulled from that. There is no request associated
        // with this answer view as it's assumed the user has cached q&a response to
        // load up the answers view with content
        return productId;
    }
}