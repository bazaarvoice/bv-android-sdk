/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.util.List;

public class ConversationsIncludeProduct extends ConversationsInclude<Product, Review> {
    public List<Product> getProducts() {
        return super.getItems();
    }

    public List<Review> getReviews() {
        return getReviewsList();
    }
}