/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Describe file here.
 */
abstract class ConversationsRequest extends ConversationsBase{
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

    ConversationsRequest(Builder builder) {
        this.builder = builder;
        final Map<String, Object> queryParams = makeQueryParams();
        urlQueryString = createUrlQueryString(queryParams);
    }

    ConversationsRequest(List<String> productIds, Builder builder) {
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
        params.put(kPASS_KEY, BVSDK.getInstance().getApiKeyConversations());
        params.put(kAPP_ID, BVSDK.getInstance().analyticsManager.getPackageName());
        params.put(kAPP_VERSION, BVSDK.getInstance().analyticsManager.getVersionName());
        params.put(kBUILD_NUM, BVSDK.getInstance().analyticsManager.getVersionCode());
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
        protected final List<Filter> filters = new ArrayList<>();
        abstract List<Filter> getFilters();
    }
}