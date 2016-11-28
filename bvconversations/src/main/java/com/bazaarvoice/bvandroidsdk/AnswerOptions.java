package com.bazaarvoice.bvandroidsdk;

/**
 * Enums for possible sorts on QuestionAndAnswersRequest results
 */
public enum AnswerOptions {
    ;

    public enum Sort implements UGCOption {
        Id("Id"),
        AuthorId("AuthorId"),
        CampaignId("CampaignId"),
        ContentLocale("ContentLocale"),
        HasPhotos("HasPhotos"),
        IsBestAnswer("IsBestAnswer"),
        IsFeatured("IsFeatured"),
        LastModeratedTime("LastModeratedTime"),
        LastModificationTime("LastModificationTime"),
        ProductId("ProductId"),
        QuestionId("QuestionId"),
        SubmissionId("SubmissionId"),
        SubmissionTime("SubmissionTime"),
        TotalFeedbackCount("TotalFeedbackCount"),
        TotalNegativeFeedbackCount("TotalNegativeFeedbackCount"),
        TotalPositiveFeedbackCount("TotalPositiveFeedbackCount"),
        UserLocation("UserLocation");

        private final String key;

        Sort(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return this.key;
        }
    }
}
