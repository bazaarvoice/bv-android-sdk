/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

class IncludeableContent<ConversationsIncludeType extends ConversationsInclude> {
    private transient ConversationsIncludeType includedIn;

    protected void setIncludedIn(ConversationsIncludeType includedIn) {
        this.includedIn = includedIn;
    }

    public ConversationsIncludeType getIncludedIn() {
        return includedIn;
    }
}