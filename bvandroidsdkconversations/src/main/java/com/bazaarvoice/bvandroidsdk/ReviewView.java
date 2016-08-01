/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.util.AttributeSet;

public final class ReviewView extends ConversationsUgcView {
    ReviewView(Context context) {
        super(context);
    }

    ReviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    ReviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    ReviewView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    String getViewName() {
        return ReviewView.class.getSimpleName();
    }
}
