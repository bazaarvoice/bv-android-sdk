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
 * Request to get {@link Author}s
 */
public final class AuthorsRequest extends ConversationsDisplayRequest {
    private static final String ENDPOINT = "authors.json";

    private AuthorsRequest(Builder builder) {
        super(builder);
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {
        AuthorsRequest.Builder builder = (AuthorsRequest.Builder)super.getBuilder();

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

    @Override
    String getEndPoint() {
        return ENDPOINT;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ConversationsDisplayRequest.Builder {
        private final List<Sort> reviewSorts, questionSorts, answerSorts;
        private final List<Include> includes;
        private final List<PDPContentType> statistics;

        public Builder(@NonNull String authorId) {
            this.reviewSorts = new ArrayList<>();
            this.questionSorts = new ArrayList<>();
            this.answerSorts = new ArrayList<>();
            includes = new ArrayList<>();
            this.statistics = new ArrayList<>();
            getFilters().add(new Filter(Filter.Type.Id, EqualityOperator.EQ, authorId));
        }

        public Builder addReviewSort(@NonNull ReviewOptions.Sort sort, @NonNull SortOrder order) {
            reviewSorts.add(new Sort(sort, order));
            return this;
        }

        public Builder addQuestionSort(@NonNull QuestionOptions.Sort sort, @NonNull SortOrder order) {
            questionSorts.add(new Sort(sort, order));
            return this;
        }

        public Builder addAnswerSort(@NonNull AnswerOptions.Sort sort, @NonNull SortOrder order) {
            answerSorts.add(new Sort(sort, order));
            return this;
        }

        public Builder addIncludeContent(@NonNull PDPContentType type, int limit) {
            this.includes.add(new Include(type, limit));
            return this;
        }

        public Builder addIncludeStatistics(@NonNull PDPContentType type) {
            this.statistics.add(type);
            return this;
        }

        public AuthorsRequest build() {
            return new AuthorsRequest(this);
        }
    }
}
