/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

public class StoreReview extends BaseReview {
    @Override
    public ConversationsIncludeStore getIncludedIn() {
        return (ConversationsIncludeStore) super.getIncludedIn();
    }


}