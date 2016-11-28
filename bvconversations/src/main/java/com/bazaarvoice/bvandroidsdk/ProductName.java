/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

enum ProductName {
    QUESTIONSANDANSWERS("AskAndAnswer"),
    RATINGSANDREVIEWS("RatingsAndReviews");

    private String productName;

    ProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return productName;
    }
}
