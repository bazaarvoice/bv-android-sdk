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

/**
 * {@link android.support.v7.widget.RecyclerView} container for
 * many {@link ReviewView}s providing usage Analytic events.
 */
public class StoreReviewsRecyclerView extends ConversationsDisplayRecyclerView<StoreReviewsRequest, StoreReviewResponse> {
    public StoreReviewsRecyclerView(Context context) {
        super(context);
    }

    public StoreReviewsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StoreReviewsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    String getProductIdFromRequest(StoreReviewsRequest storeReviewsRequest) {
        return storeReviewsRequest.getStoreId();
    }

    @Override
    BVEventValues.BVProductType getBVProductType() {
        return BVEventValues.BVProductType.CONVERSATIONS_REVIEWS;
    }

    @Override
    String getContainerId() {
        return "StoreReviewsRecyclerView";
    }

    @Override
    BVEventValues.BVProductType getBvProductType() {
        return BVEventValues.BVProductType.CONVERSATIONS_REVIEWS; // TODO GOATZ This matches iOS but seems weird
    }
}