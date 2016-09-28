/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

class IncludeableStoresContent {
    private transient ConversationsStoresInclude includedIn;

    protected void setIncludedIn(ConversationsStoresInclude includedIn) {
        this.includedIn = includedIn;
    }

    public ConversationsStoresInclude getIncludedIn() {
        return includedIn;
    }

    
}