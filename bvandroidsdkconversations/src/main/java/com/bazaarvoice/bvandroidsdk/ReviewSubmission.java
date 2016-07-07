/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * TODO: Describe file here.
 */
public class ReviewSubmission extends ConversationsSubmission{

    private static final String kPRODUCT_ID = "ProductId";
    private static final String kIS_RECOMMENDED = "IsRecommended";
    private static final String kSEND_EMAIL_COMMENTED = "SendEmailAlertWhenCommented";
    private static final String kRATING = "Rating";
    private static final String kNET_PROMOTER_SCORE = "NetPromoterScore";
    private static final String kTITLE = "Title";
    private static final String kREVIEW_TEXT = "ReviewText";
    private static final String kNET_PROMOTER_COMMENT = "NetPromoterComment";

    ReviewSubmission(Builder builder) {
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

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {
        Builder builder = (Builder) getBuilder();
        queryParams.put(kPRODUCT_ID, builder.productId);
        queryParams.put(kIS_RECOMMENDED, builder.isRecommended);
        queryParams.put(kSEND_EMAIL_COMMENTED, builder.sendEmailAlertWhenCommented);
        queryParams.put(kRATING, builder.rating);
        queryParams.put(kNET_PROMOTER_SCORE, builder.netPromoterScore);
        queryParams.put(kNET_PROMOTER_COMMENT, builder.netPromoterComment);
        queryParams.put(kTITLE, builder.title);
        queryParams.put(kREVIEW_TEXT, builder.reviewText);

        for (String key : builder.predefinedTags.keySet()) {
            queryParams.put(key, builder.predefinedTags.get(key));
        }

        for (String key : builder.freeFormTags.keySet()) {
            queryParams.put(key, builder.freeFormTags.get(key));
        }

        for (String key : builder.ratingSliders.keySet()) {
            queryParams.put("rating_" + key, builder.ratingSliders.get(key));
        }

        for (String key : builder.ratingQuestions.keySet()) {
            queryParams.put("rating_" + key, builder.ratingQuestions.get(key));
        }

        for (String key : builder.contextDataValues.keySet()) {
            queryParams.put("contextdatavalue_" + key, builder.contextDataValues.get(key));
        }

        for (String key : builder.additionalFields.keySet()) {
            queryParams.put("additionalfield_" + key, builder.additionalFields.get(key));
        }
    }

    public static final class Builder extends ConversationsSubmission.Builder<Builder>{

        private final String productId;
        private Boolean isRecommended;
        private Boolean sendEmailAlertWhenCommented;
        private Integer rating;
        private Integer netPromoterScore;
        private String title;
        private String reviewText;
        private String netPromoterComment;
        private final Map<String, String> freeFormTags = new HashMap<>();
        private final Map<String, String> predefinedTags = new HashMap<>();
        private final Map<String, String> additionalFields = new HashMap<>();
        private final Map<String, String> contextDataValues = new HashMap<>();
        private final Map<String, String> ratingSliders = new HashMap<>();
        private final Map<String, Integer> ratingQuestions = new HashMap<>();

        public Builder(Action action, String productId) {
            super(action);
            this.productId = productId;
        }

        public Builder isRecommended(Boolean isRecommended) {
            this.isRecommended = isRecommended;
            return this;
        }

        public Builder sendEmailAlertWhenCommented(Boolean sendEmailAlertWhenCommented) {
            this.sendEmailAlertWhenCommented = sendEmailAlertWhenCommented;
            return this;
        }

        public Builder rating(Integer rating) {
            this.rating = rating;
            return this;
        }

        public Builder netPromoterScore(Integer netPromoterScore) {
            this.netPromoterScore = netPromoterScore;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder reviewText(String reviewText) {
            this.reviewText = reviewText;
            return this;
        }
        public Builder netPromoterComment(String netPromoterComment) {
            this.netPromoterComment = netPromoterComment;
            return this;
        }

        public Builder addFreeFormTag(String questionId, String value) {
            String key = String.format(Locale.US, "tag_%s_%d", questionId, freeFormTags.size());
            freeFormTags.put(key, value);
            return this;
        }

        public Builder addPredefinedTag(String questionId, String tagId, String value) {
            String key = String.format(Locale.US, "tagid_%s/%s", questionId, tagId);
            predefinedTags.put(key, value);
            return this;
        }

        public Builder addAdditionalField(String fieldName, String value) {
            additionalFields.put(fieldName, value);
            return this;
        }

        public Builder addContextDataValueString(String dataValueName, String value) {
            contextDataValues.put(dataValueName, value);
            return this;
        }

        public Builder addContextDataValueString(String dataValueName, boolean value) {
            contextDataValues.put(dataValueName, (value) ? "true" : "false");
            return this;
        }

        public Builder addRatingQuestion(String questionName, int value) {
            ratingQuestions.put(questionName, value);
            return this;
        }

        public Builder addRatingSlider(String questionName, String value) {
            ratingSliders.put(questionName, value);
            return this;
        }

        public ReviewSubmission build() {
            return new ReviewSubmission(this);
        }

        @Override
        PhotoUpload.ContentType getPhotoContentType() {
            return PhotoUpload.ContentType.Review;
        }
    }
}