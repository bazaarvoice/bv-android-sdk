/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ConversationsDisplayResponse<ResultType> extends ConversationsResponse {
    @SerializedName("Offset")
    private Integer offset;
    @SerializedName("TotalResults")
    private Integer totalResults;
    @SerializedName("Limit")
    private Integer limit;
    @SerializedName("Locale")
    private String locale;
    @SerializedName("Results")
    private List<ResultType> results;

    public List<ResultType> getResults() {
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

}