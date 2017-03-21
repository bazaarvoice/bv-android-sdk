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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

/**
 * Request to get info and stats for a {@link Product}
 */
public class ProductDisplayPageRequest extends ConversationsDisplayRequest {
    private final List<Sort> reviewSorts, questionSorts, answerSorts;
    private final List<Include> includes;
    private final List<PDPContentType> statistics;

    private ProductDisplayPageRequest(Builder builder) {
        super(builder);
        reviewSorts = builder.reviewSorts;
        questionSorts = builder.questionSorts;
        answerSorts = builder.answerSorts;
        includes = builder.includes;
        statistics = builder.statistics;
    }

    List<Sort> getReviewSorts() {
        return reviewSorts;
    }

    List<Sort> getQuestionSorts() {
        return questionSorts;
    }

    List<Sort> getAnswerSorts() {
        return answerSorts;
    }

    List<Include> getIncludes() {
        return includes;
    }

    List<PDPContentType> getStatistics() {
        return statistics;
    }

    @Override
    public String getEndPoint() {
        return "data/products.json";
    }

    @Override
    BazaarException getError() {
        return null;
    }

    @Override
    HttpUrl toHttpUrl() {
        HttpUrl.Builder httpUrlBuilder = super.toHttpUrl().newBuilder();

        if (!reviewSorts.isEmpty()){
            httpUrlBuilder.addQueryParameter(kSORT_REVIEW, StringUtils.componentsSeparatedBy(reviewSorts, ","));
        }

        if (!questionSorts.isEmpty()){
            httpUrlBuilder.addQueryParameter(kSORT_QUESTIONS, StringUtils.componentsSeparatedBy(questionSorts, ","));
        }

        if (!answerSorts.isEmpty()){
            httpUrlBuilder.addQueryParameter(kSORT_ANSWERS, StringUtils.componentsSeparatedBy(answerSorts, ","));
        }

        if (!includes.isEmpty()) {
            httpUrlBuilder.addQueryParameter(kINCLUDE, StringUtils.componentsSeparatedBy(includes, ","));
        }

        for (Include include : includes) {
            if (include.getLimit() != null) {
                httpUrlBuilder.addQueryParameter(include.getLimitParamKey(), String.valueOf(include.getLimit()));
            }
        }

        if (!statistics.isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSTATS, StringUtils.componentsSeparatedBy(statistics, ","));
        }

        return httpUrlBuilder.build();
    }

    public static final class Builder extends ConversationsDisplayRequest.Builder<Builder> {
        private final List<Sort> reviewSorts, questionSorts, answerSorts;
        private final List<Include> includes;
        private final List<PDPContentType> statistics;

        public Builder(@NonNull String productId) {
            super();
            this.reviewSorts = new ArrayList<>();
            this.questionSorts = new ArrayList<>();
            this.answerSorts = new ArrayList<>();
            this.includes = new ArrayList<>();
            this.statistics = new ArrayList<>();
            addFilter(new Filter(Filter.Type.Id, EqualityOperator.EQ, productId));
        }

        public Builder addReviewSort(ReviewOptions.Sort sort, SortOrder order) {
            reviewSorts.add(new Sort(sort, order));
            return this;
        }

        public Builder addQuestionSort(QuestionOptions.Sort sort, SortOrder order) {
            questionSorts.add(new Sort(sort, order));
            return this;
        }

        /**
         * @deprecated Including answers is not supported on product calls.
         * @param sort Answer Sort options
         * @param order Sort Order
         * @return this builder
         */
        public Builder addAnswerSort(AnswerOptions.Sort sort, SortOrder order) {
            answerSorts.add(new Sort(sort, order));
            return this;
        }

        /**
         * Type of social content to inlcude with the product request.
         * NOTE: PDPContentType is only supported for statistics, not for Includes.
         *
         * @param type Type of CGC to include
         * @param limit Max number of items to include
         * @return this builder
         */
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
    }
}