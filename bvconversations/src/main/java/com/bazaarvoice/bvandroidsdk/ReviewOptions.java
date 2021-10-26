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
 * Enums for possible filters and sorts types on {@link Review}s
 */
public enum ReviewOptions {
    ;
    public enum Sort implements UGCOption{
        Id("Id"),
        AuthorId("AuthorId"),
        CampaignId("CampaignId"),
        ContentLocale("ContentLocale"),
        HasComments("HasComments"),
        HasPhotos("HasPhotos"),
        HasTags("HasTags"),
        HasVideos("HasVideos"),
        Helpfulness("Helpfulness"),
        IsFeatured("IsFeatured"),
        IsRatingsOnly("IsRatingsOnly"),
        IsRecommended("IsRecommended"),
        IsSubjectActive("IsSubjectActive"),
        IsSyndicated("IsSyndicated"),
        LastModeratedTime("LastModeratedTime"),
        LastModificationTime("LastModificationTime"),
        ProductId("ProductId"),
        Rating("Rating"),
        SubmissionId("SubmissionId"),
        SubmissionTime("SubmissionTime"),
        TotalCommentCount("TotalCommentCount"),
        TotalFeedbackCount("TotalFeedbackCount"),
        TotalNegativeFeedbackCount("TotalNegativeFeedbackCount"),
        TotalPositiveFeedbackCount("TotalPositiveFeedbackCount"),
        UserLocation("UserLocation");

        private final String key;

        Sort(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }

    public enum PrimaryFilter implements UGCOption {
        AuthorId("AuthorId"),
        Id("Id"),
        ProductId("ProductId"),
        SubmissionId("SubmissionId"),
        CategoryAncestorId("CategoryAncestorId"),
        ContentLocale("ContentLocale");

        private final String key;

        PrimaryFilter(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }

    }

    public enum Filter implements UGCOption{
        Id("Id"),
        AuthorId("AuthorId"),
        CampaignId("CampaignId"),
        CategoryAncestorId("CategoryAncestorId"),
        ContentLocale("ContentLocale"),
        HasComments("HasComments"),
        HasPhotos("HasPhotos"),
        HasTags("HasTags"),
        HasVideos("HasVideos"),
        IsFeatured("IsFeatured"),
        IsRatingsOnly("IsRatingsOnly"),
        IsRecommended("IsRecommended"),
        IsSubjectActive("IsSubjectActive"),
        IsSyndicated("IsSyndicated"),
        LastModeratedTime("LastModeratedTime"),
        LastModificationTime("LastModificationTime"),
        ModeratorCode("ModeratorCode"),
        ProductId("ProductId"),
        Rating("Rating"),
        SubmissionId("SubmissionId"),
        SubmissionTime("SubmissionTime"),
        TotalCommentCount("TotalCommentCount"),
        TotalFeedbackCount("TotalFeedbackCount"),
        TotalNegativeFeedbackCount("TotalNegativeFeedbackCount"),
        TotalPositiveFeedbackCount("TotalPositiveFeedbackCount"),
        UserLocation("UserLocation");

        private final String key;

        Filter(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }
}