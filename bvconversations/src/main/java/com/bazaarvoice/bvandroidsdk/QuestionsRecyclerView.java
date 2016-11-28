/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public final class QuestionsRecyclerView extends ConversationsDisplayRecyclerView<QuestionAndAnswerRequest, QuestionAndAnswerResponse> {

    public QuestionsRecyclerView(Context context) {
        super(context);
    }

    public QuestionsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QuestionsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    String getProductIdFromRequest(QuestionAndAnswerRequest questionAndAnswerRequest) {
        return questionAndAnswerRequest.getProductId();
    }

    @Override
    MagpieBvProduct getMagpieBvProduct() {
        return MagpieBvProduct.QUESTIONS_AND_ANSWERS;
    }
}