/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

class IncludeableContent {
    private transient ConversationsInclude includedIn;

    protected void setIncludedIn(ConversationsInclude includedIn) {
        this.includedIn = includedIn;
    }

    public ConversationsInclude getIncludedIn() {
        return includedIn;
    }
}