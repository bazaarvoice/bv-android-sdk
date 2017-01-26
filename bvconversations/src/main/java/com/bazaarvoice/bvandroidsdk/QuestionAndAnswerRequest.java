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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Request to get {@link Question}s and {@link Answer}s
 * for a particular {@link Product}
 */
public class QuestionAndAnswerRequest extends ConversationsDisplayRequest {

    private final String productId;

    private QuestionAndAnswerRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
    }

    String getProductId() {
        return productId;
    }

    @Override
    String getEndPoint() {
        return "questions.json";
    }

    @Override
    BazaarException getError() {
        Builder builder = (Builder) super.getBuilder();

        if (builder.limit < 1 || builder.limit > 100) {
            return new BazaarException(String.format("Invalid `limit` value: Parameter 'limit' has invalid value: %d - must be between 1 and 100.", builder.limit));
        }
            return null;
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {
        Builder builder = (Builder) super.getBuilder();

        queryParams.put(kLIMIT, "" + builder.limit);
        queryParams.put(kOFFSET, "" + builder.offset);
        queryParams.put(kINCLUDE, INCLUDE_ANSWERS);

        if (!builder.questionSorts.isEmpty()){
            queryParams.put(kSORT, StringUtils.componentsSeparatedBy(builder.questionSorts, ","));
        }

        if (!builder.answerSorts.isEmpty()){
            queryParams.put(kSORT_ANSWERS, StringUtils.componentsSeparatedBy(builder.answerSorts, ","));
        }

        if (builder.searchPhrase != null) {
            queryParams.put(kSEARCH, builder.searchPhrase);
        }
    }

    public static final class Builder extends ConversationsDisplayRequest.Builder{

        private final List<Sort> questionSorts, answerSorts;
        private final int limit;
        private final int offset;
        private String searchPhrase;
        private String productId;

        public Builder(@NonNull String productId, int limit, int offset) {
            this.questionSorts = new ArrayList<>();
            this.answerSorts = new ArrayList<>();
            this.limit = limit;
            this.offset = offset;
            this.productId = productId;
            getFilters().add(new Filter(Filter.Type.ProductId, EqualityOperator.EQ, productId));

        }

        /**
         * @deprecated renamed to explicitly be the Question sorting option
         *
         * @param sort Question Sort Option
         * @param sortOrder Question Sort Order
         * @return Request Builder
         */
        public Builder addSort(QuestionOptions.Sort sort, SortOrder sortOrder) {
            this.questionSorts.add(new Sort(sort, sortOrder));
            return this;
        }

        /**
         * @param sort Question Sort Option
         * @param sortOrder Question Sort Order
         * @return Request Builder
         */
        public Builder addQuestionSort(QuestionOptions.Sort sort, SortOrder sortOrder) {
            this.questionSorts.add(new Sort(sort, sortOrder));
            return this;
        }

        /**
         * @param sort Question Sort Option
         * @param order Question Sort Order
         * @return Request Builder
         */
        public Builder addAnswerSort(@NonNull AnswerOptions.Sort sort, @NonNull SortOrder order) {
            answerSorts.add(new Sort(sort, order));
            return this;
        }

        public Builder addFilter(QuestionOptions.Filter filter, EqualityOperator equalityOperator, String value) {
            getFilters().add(new Filter(filter, equalityOperator, value));
            return this;
        }

        public Builder includeSearchPhrase(String search) {
            this.searchPhrase = search;
            return this;
        }

        public QuestionAndAnswerRequest build() {
            return new QuestionAndAnswerRequest(this);
        }
    }
}