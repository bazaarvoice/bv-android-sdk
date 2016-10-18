/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.util.List;

public class ConversationsIncludeStore extends ConversationsInclude<Store, StoreReview> {

    public List<Store> getStores() {
        return super.getItems();
    }

    public List<StoreReview> getReviewList() {
        return super.getReviewsList();
    }
}