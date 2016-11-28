/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * Enums for possible filters and stats types on BulkRatingRequest results
 */
public class BulkRatingOptions {
    public enum Filter implements UGCOption {
        ContentLocale("ContentLocal");
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
        All("Reviews,NativeReviews");

        private String key;
        StatsType(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }
}