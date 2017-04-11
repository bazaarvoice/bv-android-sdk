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

import okhttp3.HttpUrl;

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

  private final List<Filter> filters;
  private final Map<String, String> extraParams;

  ConversationsDisplayRequest(Builder builder) {
    filters = builder.filters;
    extraParams = builder.extraParams;
  }

  HttpUrl toHttpUrl() {
    String rootBvApiUrl = BVSDK.getInstance().getBvWorkerData().getRootApiUrls().getBazaarvoiceApiRootUrl();

    HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(rootBvApiUrl)
        .newBuilder()
        .addPathSegments(getEndPoint());

    addCommonQueryParams(httpUrlBuilder);
    addFilterQueryParams(httpUrlBuilder);
    addExtraQueryParams(httpUrlBuilder);

    return httpUrlBuilder.build();
  }

  private void addCommonQueryParams(HttpUrl.Builder httpUrlBuilder) {
    BVMobileInfo mobileInfo = BVSDK.getInstance().getBvUserProvidedData().getBvMobileInfo();

    httpUrlBuilder.addQueryParameter(kAPI_VERSION, API_VERSION)
        .addQueryParameter(kPASS_KEY, getAPIKey())
        .addQueryParameter(kAPP_ID, mobileInfo.getMobileAppIdentifier())
        .addQueryParameter(kAPP_VERSION, mobileInfo.getMobileAppVersion())
        .addQueryParameter(kBUILD_NUM, mobileInfo.getMobileAppCode())
        .addQueryParameter(kSDK_VERSION, mobileInfo.getBvSdkVersion());
  }

  private void addFilterQueryParams(HttpUrl.Builder httpUrlBuilder) {
    for (Filter filter : filters) {
      httpUrlBuilder
          .addEncodedQueryParameter(kFILTER, filter.toString());
    }
  }

  private void addExtraQueryParams(HttpUrl.Builder httpUrlBuilder) {
    for (Map.Entry<String, String> extraEntry : extraParams.entrySet()) {
      httpUrlBuilder
          .addQueryParameter(extraEntry.getKey(), extraEntry.getValue());
    }
  }

  /**
   * TODO: Remove when stores code is separated from conversations module
   * @return api key for the correct service, typically just plain conversations
   */
  protected String getAPIKey(){
    return BVSDK.getInstance().getBvUserProvidedData().getBvApiKeys().getApiKeyConversations();
  }

  static abstract class Builder<BuilderType> {
    private final List<Filter> filters;
    private final Map<String, String> extraParams;

    public Builder() {
      filters = new ArrayList<>();
      extraParams = new HashMap<>();
    }

    protected void addFilter(Filter filter) {
      filters.add(filter);
    }

    public BuilderType addAdditionalField(String key, String value) {
      extraParams.put(key, value);
      return (BuilderType) this;
    }
  }
}