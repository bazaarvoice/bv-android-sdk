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

/**
 * Enums for possible filters and sorts types on
 * {@link ProductDisplayPageRequest}
 */
public enum ProductOptions {
    ;
    public enum Sort implements UGCOption{
        Id("Id"),
        AverageOverallRating("AverageOverallRating"),
        CategoryId("CategoryId"),
        IsActive("IsActive"),
        IsDisabled("IsDisabled"),
        LastAnswerTime("LastAnswerTime"),
        LastQuestionTime("LastQuestionTime"),
        LastReviewTime("LastReviewTime"),
        LastStoryTime("LastStoryTime"),
        Name("Name"),
        Rating("Rating"),
        RatingsOnlyReviewCount("RatingsOnlyReviewCount"),
        TotalAnswerCount("TotalAnswerCount"),
        TotalQuestionCount("TotalQuestionCount"),
        TotalReviewCount("TotalReviewCount"),
        TotalStoryCount("TotalStoryCount");

        private final String key;

        Sort(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }

    public enum Filter implements UGCOption{
        Id("Id"),
        AverageOverallRating("AverageOverallRating"),
        CategoryAncestorId("CategoryAncestorId"),
        CategoryId("CategoryId"),
        IsActive("IsActive"),
        IsDisabled("IsDisabled"),
        LastAnswerTime("LastAnswerTime"),
        LastQuestionTime("LastQuestionTime"),
        LastReviewTime("LastReviewTime"),
        LastStoryTime("LastStoryTime"),
        Name("Name"),
        RatingsOnlyReviewCount("RatingsOnlyReviewCount"),
        TotalAnswerCount("TotalAnswerCount"),
        TotalQuestionCount("TotalQuestionCount"),
        TotalReviewCount("TotalReviewCount"),
        TotalStoryCount("TotalStoryCount");
        private final String key;

        Filter(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }
}
