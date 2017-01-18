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
        ConversationsAnalyticsManager.sendUsedFeatureInViewEvent(productId, MagpieBvProduct.QUESTIONS_AND_ANSWERS);
    }

    @Override
    public void onViewGroupInteractedWith() {
        ConversationsAnalyticsManager.sendUsedFeatureScrolledEvent(productId, MagpieBvProduct.QUESTIONS_AND_ANSWERS);
    }

    @Override
    public String getProductId() {
        return productId;
    }
}