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

/**
 * Common Builder to submit an Review
 */
abstract class BaseReviewSubmissionRequest extends ConversationsSubmissionRequest  {
    private final String productId;
    private final Boolean isRecommended;
    private final Boolean sendEmailAlertWhenCommented;
    private final int rating;
    private final Integer netPromoterScore;
    private final String title;
    private final String reviewText;
    private final String netPromoterComment;
    private final Map<String, String> freeFormTags;
    private final List<PredefinedTag> predefinedTags;
    private final Map<String, String> additionalFields;
    private final Map<String, String> contextDataValues;
    private final Map<String, String> ratingSliders;
    private final Map<String, Integer> ratingQuestions;
    private final List<VideoSubmissionData> videoSubmissionData;
    
    BaseReviewSubmissionRequest(BaseReviewBuilder builder) {
        super(builder);
        productId = builder.productId;
        isRecommended = builder.isRecommended;
        sendEmailAlertWhenCommented = builder.sendEmailAlertWhenCommented;
        rating = builder.rating;
        netPromoterScore = builder.netPromoterScore;
        title = builder.title;
        reviewText = builder.reviewText;
        netPromoterComment = builder.netPromoterComment;
        freeFormTags = builder.freeFormTags;
        predefinedTags = builder.predefinedTags;
        additionalFields = builder.additionalFields;
        contextDataValues = builder.contextDataValues;
        ratingSliders = builder.ratingSliders;
        ratingQuestions = builder.ratingQuestions;
        videoSubmissionData = builder.videoSubmissionData;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    String getProductId() {
        return productId;
    }

    Boolean getRecommended() {
        return isRecommended;
    }

    Boolean getSendEmailAlertWhenCommented() {
        return sendEmailAlertWhenCommented;
    }

    int getRating() {
        return rating;
    }

    Integer getNetPromoterScore() {
        return netPromoterScore;
    }

    String getTitle() {
        return title;
    }

    String getReviewText() {
        return reviewText;
    }

    String getNetPromoterComment() {
        return netPromoterComment;
    }

    Map<String, String> getFreeFormTags() {
        return freeFormTags;
    }

    List<PredefinedTag> getPredefinedTags() {
        return predefinedTags;
    }

    Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    Map<String, String> getContextDataValues() {
        return contextDataValues;
    }

    Map<String, String> getRatingSliders() {
        return ratingSliders;
    }

    Map<String, Integer> getRatingQuestions() {
        return ratingQuestions;
    }

    List<VideoSubmissionData> getVideoSubmissionData() {
        return videoSubmissionData;
    }

    /**
     * Internal class for predefined tag values
     */
    static final class PredefinedTag {
        private final String questionId;
        private final String tagId;
        private final String value;

        public PredefinedTag(String questionId, String tagId, String value) {
            this.questionId = questionId;
            this.tagId = tagId;
            this.value = value;
        }

        public String getQuestionId() {
            return questionId;
        }

        public String getTagId() {
            return tagId;
        }

        public String getValue() {
            return value;
        }
    }
}
