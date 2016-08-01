/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Request used to obtain information and statistics for a particular productId
 */
public class ProductDisplayPageRequest extends ConversationsDisplayRequest {


    private ProductDisplayPageRequest(Builder builder) {
        super(builder);
    }

    @Override
    public String getEndPoint() {
        return "products.json";
    }

    @Override
    BazaarException getError() {
        return null;
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {

        Builder builder = (Builder)super.getBuilder();

        if (!builder.reviewSorts.isEmpty()){
            queryParams.put(kSORT_REVIEW, StringUtils.componentsSeparatedBy(builder.reviewSorts, ","));
        }

        if (!builder.questionSorts.isEmpty()){
            queryParams.put(kSORT_QUESTIONS, StringUtils.componentsSeparatedBy(builder.questionSorts, ","));
        }

        if (!builder.answerSorts.isEmpty()){
            queryParams.put(kSORT_ANSWERS, StringUtils.componentsSeparatedBy(builder.answerSorts, ","));
        }

        if (!builder.includes.isEmpty()) {
            queryParams.put(kINCLUDE, StringUtils.componentsSeparatedBy(builder.includes, ","));
        }

        for (Include include : builder.includes) {
            if (include.getLimit() != null) {
                queryParams.put(include.getLimitParamKey(), include.getLimit());
            }
        }

        if (!builder.statistics.isEmpty()) {
            queryParams.put(kSTATS, StringUtils.componentsSeparatedBy(builder.statistics, ","));
        }
    }

    public static final class Builder extends ConversationsDisplayRequest.Builder{
        private final List<Sort> reviewSorts, questionSorts, answerSorts;
        private final List<Include> includes;
        private final List<PDPContentType> statistics;

        public Builder(@NonNull String productId) {
            this.reviewSorts = new ArrayList<>();
            this.questionSorts = new ArrayList<>();
            this.answerSorts = new ArrayList<>();
            this.includes = new ArrayList<>();
            this.statistics = new ArrayList<>();
            filters.add(new Filter(Filter.Type.Id, EqualityOperator.EQ, productId));
        }

        public Builder addReviewSort(ReviewOptions.Sort sort, SortOrder order) {
            reviewSorts.add(new Sort(sort, order));
            return this;
        }

        public Builder addQuestionSort(QuestionOptions.Sort sort, SortOrder order) {
            questionSorts.add(new Sort(sort, order));
            return this;
        }
        public Builder addAnswerSort(AnswerOptions.Sort sort, SortOrder order) {
            answerSorts.add(new Sort(sort, order));
            return this;
        }

        public Builder addIncludeContent(PDPContentType type, @Nullable Integer limit) {
            this.includes.add(new Include(type, limit));
            return this;
        }

        public Builder addIncludeStatistics(PDPContentType type) {
            this.statistics.add(type);
            return this;
        }

        public ProductDisplayPageRequest build() {
            return new ProductDisplayPageRequest(this);
        }

        @Override
        List<Filter> getFilters() {
            return filters;
        }
    }
}