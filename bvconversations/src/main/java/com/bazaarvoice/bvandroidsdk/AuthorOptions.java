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
 * {@link AuthorsRequest} results
 */
public enum AuthorOptions {
    ;

    public enum SortOptions {
        ID("Id"),
        CONTENT_LOCALE("ContentLocale"),
        HAS_PHOTOS("HasPhotos"),
        HAS_VIDEOS("HasVideos"),
        LAST_MODERATED_TIME("LastModeratedTime"),
        SUBMISSION_TIME("SubmissionTime"),
        TOTAL_ANSWER_COUNT("TotalAnswerCount"),
        TOTAL_QUESTION_COUNT("TotalQuestionCount"),
        TOTAL_REVIEW_COUNT("TotalReviewCount"),
        USER_LOCATION("UserLocation");

        private String value;

        SortOptions(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Filter implements UGCOption {
        ID("Id"),
        CONTENT_LOCALE("ContentLocale"),
        HAS_PHOTOS("HasPhotos"),
        HAS_VIDEOS("HasVideos"),
        LAST_MODERATED_TIME("LastModeratedTime"),
        MODERATOR_CODE("ModeratorCode"),
        SUBMISSION_TIME("SubmissionTime"),
        TOTAL_ANSWER_COUNT("TotalAnswerCount"),
        TOTAL_QUESTION_COUNT("TotalQuestionCount"),
        TOTAL_REVIEW_COUNT("TotalReviewCount"),
        USER_LOCATION("UserLocation");

        private final String key;

        Filter(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }

        //                AdditionalField_[FIELD_NAME]
//        Additional field to filter by, e.g., filter=AdditionalField_[FIELD_NAME]:eq:[FIELD_VALUE]

//                ContextDataValue_[DIMENSION_EXTERNAL_ID]
//        The context data value for the content. DIMENSION_EXTERNAL_ID can be age, gender, etc. e.g. filter=contextdatavalue_age:under21&filter=contextdatavalue_gender:male

    }

}
