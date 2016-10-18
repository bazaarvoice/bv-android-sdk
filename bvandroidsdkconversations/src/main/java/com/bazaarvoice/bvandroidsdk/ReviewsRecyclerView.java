/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public final class ReviewsRecyclerView extends ConversationsDisplayRecyclerView<ReviewsRequest, ReviewResponse> {

    public ReviewsRecyclerView(Context context) {
        super(context);
    }

    public ReviewsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReviewsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    String getProductIdFromRequest(ReviewsRequest reviewsRequest) {
        return reviewsRequest.getProductId();
    }

    @Override
    MagpieBvProduct getMagpieBvProduct() {
        return MagpieBvProduct.RATINGS_AND_REVIEWS;
    }

}
