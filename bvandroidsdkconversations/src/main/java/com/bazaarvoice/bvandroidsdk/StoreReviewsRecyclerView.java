/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

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
    MagpieBvProduct getMagpieBvProduct() {
        return MagpieBvProduct.RATINGS_AND_REVIEWS;
    }
}