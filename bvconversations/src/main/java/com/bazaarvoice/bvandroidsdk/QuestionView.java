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
 * {@link android.widget.FrameLayout} container for a single {@link Question}
 * providing usage Analytic events.
 */
public final class QuestionView extends ConversationsUgcView {
    QuestionView(Context context) {
        super(context);
    }

    QuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    QuestionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    QuestionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    String getViewName() {
        return QuestionView.class.getSimpleName();
    }
}
