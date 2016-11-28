/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

public class Review extends BaseReview {
    @Override
    public ConversationsIncludeProduct getIncludedIn() {
        return (ConversationsIncludeProduct) super.getIncludedIn();
    }
}