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
 * Enums for possible filters and stats types on BulkRatingRequest results
 */
public class BulkRatingOptions {
    public enum Filter implements UGCOption {
        ContentLocale("ContentLocale");
        private final String key;

        Filter(String key) {
            this.key = key;
        }
        public String getKey() {
            return this.key;
        }
    }

    public enum StatsType implements UGCOption{
        Reviews("Reviews"),
        NativeReviews("NativeReviews"),
        Questions("Questions"),
        Answers("Answers"),
        All("Reviews,NativeReviews,Questions,Answers");

        private String key;
        StatsType(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }
}