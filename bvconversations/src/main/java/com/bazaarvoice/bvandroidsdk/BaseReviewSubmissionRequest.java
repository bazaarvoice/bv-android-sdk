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

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Common Builder to submit an Review
 */
abstract class BaseReviewSubmissionRequest extends ConversationsSubmissionRequest  {

    private static final String kPRODUCT_ID = "ProductId";
    private static final String kIS_RECOMMENDED = "IsRecommended";
    private static final String kSEND_EMAIL_COMMENTED = "SendEmailAlertWhenCommented";
    private static final String kRATING = "Rating";
    private static final String kNET_PROMOTER_SCORE = "NetPromoterScore";
    private static final String kTITLE = "Title";
    private static final String kREVIEW_TEXT = "ReviewText";
    private static final String kNET_PROMOTER_COMMENT = "NetPromoterComment";
    private static final String VIDEO_URL_TEMPLATE = "VideoUrl_%d";
    private static final String VIDEO_CAPTION_TEMPLATE = "VideoCaption_%d";

    BaseReviewSubmissionRequest(BaseReviewBuilder builder) {
        super(builder);
    }

    @Override
    String getEndPoint() {
        return "submitreview.json";
    }

    @Override
    BazaarException getError() {
        return null;
    }

    String getProductId() {
        Map<String, Object> queryParams = makeQueryParams();
        return queryParams.containsKey(kPRODUCT_ID) ? (String) queryParams.get(kPRODUCT_ID) : "";
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {
        BaseReviewBuilder builder = (BaseReviewBuilder) getBuilder();
        queryParams.put(kPRODUCT_ID, builder.productId);
        queryParams.put(kIS_RECOMMENDED, builder.isRecommended);
        queryParams.put(kSEND_EMAIL_COMMENTED, builder.sendEmailAlertWhenCommented);
        queryParams.put(kRATING, builder.rating);
        queryParams.put(kNET_PROMOTER_SCORE, builder.netPromoterScore);
        queryParams.put(kNET_PROMOTER_COMMENT, builder.netPromoterComment);
        queryParams.put(kTITLE, builder.title);
        queryParams.put(kREVIEW_TEXT, builder.reviewText);

        Set<String> predefinedTagKeys = builder.predefinedTags.keySet();
        for (String key : predefinedTagKeys) {
            queryParams.put(key, builder.predefinedTags.get(key));
        }

        Set<String> freeFormTagKeys = builder.freeFormTags.keySet();
        for (String key : freeFormTagKeys) {
            queryParams.put(key, builder.freeFormTags.get(key));
        }

        Set<String> ratingSliderKeys = builder.ratingSliders.keySet();
        for (String key : ratingSliderKeys) {
            queryParams.put("rating_" + key, builder.ratingSliders.get(key));
        }

        Set<String> ratingQuestionsKeys = builder.ratingQuestions.keySet();
        for (String key : ratingQuestionsKeys) {
            queryParams.put("rating_" + key, builder.ratingQuestions.get(key));
        }

        Set<String> contextDataValueKeys = builder.contextDataValues.keySet();
        for (String key : contextDataValueKeys) {
            queryParams.put("contextdatavalue_" + key, builder.contextDataValues.get(key));
        }

        Set<String> additionalFieldsKeys = builder.additionalFields.keySet();
        for (String key : additionalFieldsKeys) {
            queryParams.put("additionalfield_" + key, builder.additionalFields.get(key));
        }

        List<VideoSubmissionData> videoDataList = builder.videoSubmissionData;
        for (int i=0; i<videoDataList.size(); i++) {
            VideoSubmissionData videoData = videoDataList.get(i);
            int submitIndex = i+1;
            queryParams.put(String.format(VIDEO_URL_TEMPLATE, submitIndex), videoData.getVideoUrl());
            if (videoData.getVideoCaption() != null) {
                queryParams.put(String.format(VIDEO_CAPTION_TEMPLATE, submitIndex), videoData.getVideoCaption());
            }
        }
    }
}
