/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.StoreReview;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

public class DemoStoreReviewsAdapter extends DemoReviewsAdapter<StoreReview> {

    public DemoStoreReviewsAdapter(Picasso picasso, PrettyTime prettyTime, BVConversationsClient bvConversationsClient) {
        super(picasso, prettyTime, bvConversationsClient);
    }

}