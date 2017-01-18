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
 * {@link QuestionAndAnswerRequest} results
 */
public enum QuestionOptions {
    ;
    public enum Sort implements UGCOption{
        Id("Id"),
        AuthorId("AuthorId"),
        CampaignId("CampaignId"),
        ContentLocale("ContentLocale"),
        HasAnswers("HasAnswers"),
        HasBestAnswer("HasBestAnswer"),
        HasPhotos("HasPhotos"),
        HasStaffAnswers("HasStaffAnswers"),
        HasVideos("HasVideos"),
        IsFeatured("IsFeatured"),
        IsSubjectActive("IsSubjectActive"),
        LastApprovedAnswerSubmissionTime("LastApprovedAnswerSubmissionTime"),
        LastModeratedTime("LastModeratedTime"),
        LastModificationTime("LastModificationTime"),
        ProductId("ProductId"),
        SubmissionId("SubmissionId"),
        SubmissionTime("SubmissionTime"),
        Summary("Summary"),
        TotalAnswerCount("TotalAnswerCount"),
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
        CategoryId("CategoryId"),
        ContentLocale("ContentLocale"),
        HasAnswers("HasAnswers"),
        HasBestAnswer("HasBestAnswer"),
        HasBrandAnswers("HasBrandAnswers"),
        HasPhotos("HasPhotos"),
        HasStaffAnswers("HasStaffAnswers"),
        HasTags("HasTags"),
        HasVideos("HasVideos"),
        IsFeatured("IsFeatured"),
        IsSubjectActive("IsSubjectActive"),
        LastApprovedAnswerSubmissionTime("LastApprovedAnswerSubmissionTime"),
        LastModeratedTime("LastModeratedTime"),
        LastModificationTime("LastModificationTime"),
        ModeratorCode("ModeratorCode"),
        SubmissionId("SubmissionId"),
        SubmissionTime("SubmissionTime"),
        Summary("Summary"),
        TotalAnswerCount("TotalAnswerCount"),
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
