/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class IncludesStoreResponse<IncludableContentType extends IncludeableStoresContent> extends ConversationsDisplayResponse<IncludableContentType> {
    @SerializedName("Includes")
    private ConversationsStoresInclude includes;
    private boolean hasAssociatedIncludes = false;

    public ConversationsStoresInclude getIncludes() {
        return includes;
    }

    @Override
    public List<IncludableContentType> getResults() {
        List<IncludableContentType> results = super.getResults();
        if (!hasAssociatedIncludes) {
            for (IncludableContentType result : results) {
                result.setIncludedIn(this.includes);
            }
        }
        return results;
    }
}
