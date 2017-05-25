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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Common options for building a Review Request.
 *
 * @param <ChildBuilderType> Type of the final Builder class implementation
 */
abstract class BaseReviewBuilder<ChildBuilderType extends BaseReviewBuilder> extends ConversationsSubmissionRequest.Builder<ChildBuilderType> {
    protected final String productId;
    protected Boolean isRecommended;
    protected Boolean sendEmailAlertWhenCommented;
    protected int rating;
    protected Integer netPromoterScore;
    protected String title;
    protected String reviewText;
    protected  String netPromoterComment;
    protected  final Map<String, String> freeFormTags = new HashMap<>();
    protected  final Map<String, String> predefinedTags = new HashMap<>();
    protected  final Map<String, String> additionalFields = new HashMap<>();
    protected  final Map<String, String> contextDataValues = new HashMap<>();
    protected  final Map<String, String> ratingSliders = new HashMap<>();
    protected  final Map<String, Integer> ratingQuestions = new HashMap<>();

    BaseReviewBuilder(Action action, String productId) {
        super(action);
        this.productId = productId;
    }

    public ChildBuilderType isRecommended(Boolean isRecommended) {
        this.isRecommended = isRecommended;
        return (ChildBuilderType) this;
    }

    public ChildBuilderType sendEmailAlertWhenCommented(Boolean sendEmailAlertWhenCommented) {
        this.sendEmailAlertWhenCommented = sendEmailAlertWhenCommented;
        return (ChildBuilderType) this;
    }

    public ChildBuilderType rating(int rating) {
        this.rating = rating;
        return (ChildBuilderType) this;
    }

    public ChildBuilderType netPromoterScore(Integer netPromoterScore) {
        this.netPromoterScore = netPromoterScore;
        return (ChildBuilderType) this;
    }

    public ChildBuilderType title(String title) {
        this.title = title;
        return (ChildBuilderType) this;
    }

    public ChildBuilderType reviewText(String reviewText) {
        this.reviewText = reviewText;
        return (ChildBuilderType) this;
    }
    public ChildBuilderType netPromoterComment(String netPromoterComment) {
        this.netPromoterComment = netPromoterComment;
        return (ChildBuilderType) this;
    }

    public ChildBuilderType addFreeFormTag(String questionId, String value) {
        String key = String.format(Locale.US, "tag_%s_%d", questionId, freeFormTags.size());
        freeFormTags.put(key, value);
        return (ChildBuilderType) this;
    }

    public ChildBuilderType addPredefinedTag(String questionId, String tagId, String value) {
        String key = String.format(Locale.US, "tagid_%s/%s", questionId, tagId);
        predefinedTags.put(key, value);
        return (ChildBuilderType) this;
    }

    public ChildBuilderType addAdditionalField(String fieldName, String value) {
        additionalFields.put(fieldName, value);
        return (ChildBuilderType) this;
    }

    public ChildBuilderType addContextDataValueString(String dataValueName, String value) {
        contextDataValues.put(dataValueName, value);
        return (ChildBuilderType) this;
    }

    public ChildBuilderType addContextDataValueString(String dataValueName, boolean value) {
        contextDataValues.put(dataValueName, (value) ? "true" : "false");
        return (ChildBuilderType) this;
    }

    public ChildBuilderType addRatingQuestion(String questionName, int value) {
        ratingQuestions.put(questionName, value);
        return (ChildBuilderType) this;
    }

    public ChildBuilderType addRatingSlider(String questionName, String value) {
        ratingSliders.put(questionName, value);
        return (ChildBuilderType) this;
    }

    @Override
    PhotoUpload.ContentType getPhotoContentType() {
        return PhotoUpload.ContentType.REVIEW;
    }
}