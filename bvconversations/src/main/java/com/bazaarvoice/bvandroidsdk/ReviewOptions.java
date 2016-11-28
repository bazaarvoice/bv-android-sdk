/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * Enums for possible filters and sorts types on ReviewsRequest results
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