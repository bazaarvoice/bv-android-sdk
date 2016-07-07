/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ConversationsRequestResponseBase<T> extends ConversationsResponseBase {
    @SerializedName("Offset")
    private Integer offset;
    @SerializedName("TotalResults")
    private Integer totalResults;
    @SerializedName("Limit")
    private Integer limit;
    @SerializedName("Locale")
    private String locale;
    @SerializedName("Results")
    private List<T> results;

    public List<T> getResults() {
        return results;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getLocale() {
        return locale;
    }

    static class IncludesResponseBase<T extends IncludeableContent> extends ConversationsRequestResponseBase<T> {
        @SerializedName("Includes")
        private ConversationsInclude includes;
        private boolean hasAssociatedIncludes = false;
        public ConversationsInclude getIncludes() {
            return includes;
        }

        @Override
        public List<T> getResults() {
            if (!hasAssociatedIncludes){
                for (T t : super.results){
                    t.setIncludedIn(this.includes);
                }
            }
            return super.results;
        }
    }
}