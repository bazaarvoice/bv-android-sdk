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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common options for a Conversations display request
 */
abstract class ConversationsDisplayRequest extends ConversationsRequest {
    static final String kFILTER = "Filter";
    static final String kSORT= "Sort";
    static final String kSORT_REVIEW = "Sort_Reviews";
    static final String kSORT_QUESTIONS = "Sort_Questions";
    static final String kSORT_ANSWERS= "Sort_Answers";
    static final String kINCLUDE = "Include";
    static final String kSTATS = "Stats";
    static final String kLIMIT = "Limit";
    static final String kOFFSET = "Offset";
    static final String kSEARCH = "Search";

    static final String INCLUDE_ANSWERS = "Answers";

    private final Builder builder;
    private final String urlQueryString;

    ConversationsDisplayRequest(Builder builder) {
        this.builder = builder;
        final Map<String, Object> queryParams = makeQueryParams();
        urlQueryString = createUrlQueryString(queryParams);
    }

    ConversationsDisplayRequest(List<String> productIds, Builder builder) {
        this.builder = builder;
        final Map<String, Object> queryParams = makeQueryParams();
        urlQueryString = createUrlQueryString(queryParams);
    }

    Builder getBuilder() {
        return this.builder;
    }

    Map<String, Object> makeQueryParams() {
        Map<String, Object> params = makeCommonQueryParams();
        addRequestQueryParams(params);
        return params;
    }

   private Map<String, Object> makeCommonQueryParams() {
        Map<String, Object> params = new HashMap<>();
        params.put(kAPI_VERSION, API_VERSION);
        params.put(kPASS_KEY, getAPIKey());
       AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
        params.put(kAPP_ID, analyticsManager.getPackageName());
        params.put(kAPP_VERSION, analyticsManager.getVersionName());
        params.put(kBUILD_NUM, analyticsManager.getVersionCode());
        params.put(kSDK_VERSION, BVSDK.SDK_VERSION);

       List<String> filterQueries = new ArrayList<>();
       for (Filter filter : builder.getFilters()) {
            filterQueries.add(filter.getQueryString());
       }
       params.put(kFILTER, StringUtils.componentsSeparatedBy(filterQueries, "&"));

       return params;
    }

    String getUrlQueryString() {
        return this.urlQueryString;
    }

    String getAPIKey(){
        return BVSDK.getInstance().getApiKeys().getApiKeyConversations();
    }

    abstract void addRequestQueryParams(Map<String, Object> queryParams);

    private String createUrlQueryString(Map<String, Object> queryParams) {

        StringBuilder builder = new StringBuilder();
        for (String key : queryParams.keySet()) {
            if (kFILTER.equals(key)){
                builder.append(queryParams.get(key));
            }else {
                builder.append(String.format("%s=%s", key, queryParams.get(key)));
            }
            builder.append("&");
        }

        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    static abstract class Builder{
        private final List<Filter> filters = new ArrayList<>();

        List<Filter> getFilters() {
            return filters;
        }
    }
}