/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.util.AttributeSet;

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
