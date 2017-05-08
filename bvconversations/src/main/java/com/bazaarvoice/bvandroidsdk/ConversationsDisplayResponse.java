/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Common options for a Conversations display response
 */
public abstract class ConversationsDisplayResponse<ResultType> extends ConversationsResponse {
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