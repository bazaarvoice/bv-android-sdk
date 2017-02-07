package com.bazaarvoice.bvandroidsdk;

import com.bazaarvoice.bvandroidsdk.types.FeedbackContentType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackVoteType;
import com.bazaarvoice.bvandroidsdk_common.BuildConfig;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19)
public class ConversationsUnitTest extends BVBaseTest{

    @Override
    protected void modifyPropertiesToInitSDK() {
        bazaarvoiceApiBaseUrl = "a different one";
        conversationApiKey = "a differnt key";
    }

    @Test
    public void testQnARequestOverLimitError() {
        QuestionAndAnswerRequest request = new QuestionAndAnswerRequest.Builder("testProductId", 101, 0)
                .build();

        assertTrue("Request contains error but was not found", request.getError() != null);
    }

    @Test
    public void testQnARequestValidLimit() {
        QuestionAndAnswerRequest request = new QuestionAndAnswerRequest.Builder("testProductId", 20, 0)
                .build();

        assertTrue("Request does not contain error but one was found", request.getError() == null);
    }

    @Test
    public void testBulkRatingsRequestOverLimitError() {

        List<String> prodIds = getProdIds(51);

        BulkRatingsRequest request = new BulkRatingsRequest.Builder(prodIds, BulkRatingOptions.StatsType.NativeReviews)
                .build();

        assertTrue("Request contains error but was not found", request.getError() != null);
    }

    @Test
    public void testBulkRatingsRequestValidLimit() {
        List<String> prodIds = getProdIds(20);

        BulkRatingsRequest request = new BulkRatingsRequest.Builder(prodIds, BulkRatingOptions.StatsType.NativeReviews)
                .build();

        assertTrue("Request does not contain error but one was found", request.getError() == null);
    }

    @Test
    public void testReviewsRequestOverLimitError() {

        ReviewsRequest request = new ReviewsRequest.Builder("testProductId", 101 , 0)
                .build();

        assertTrue("Request contains error but was not found", request.getError() != null);
    }

    @Test
    public void testReviewsRequestValidLimit() {

        ReviewsRequest request = new ReviewsRequest.Builder("testProductId", 20 , 0)
                .build();

        assertTrue("Request does not contain error but one was found", request.getError() == null);
    }

    @Test
    public void testSortToString() {
        Sort sort = new Sort(ReviewOptions.Sort.AuthorId, SortOrder.ASC);
        assertEquals("Sort to string incorrect", sort.toString(), "AuthorId:asc");
    }

    private <ResponseType> ResponseType testParsing(String filename, Class<ResponseType> responseClass) {
        String reviewsForProdResponse = jsonFileAsString(filename);
        return gson.fromJson(reviewsForProdResponse, responseClass);
    }

    @Test
    public void testPdpForAllProductsParsing() {
        testParsing("product_catalog_all_products.json", ProductDisplayPageResponse.class);
    }

    @Test
    public void testPdpForProductIdParsing() {
        testParsing("product_catalog_product_id.json", ProductDisplayPageResponse.class);
    }

    @Test
    public void testPdpForProductIdIncludeRevStatsParsing() {
        ProductDisplayPageResponse response = testParsing("product_catalog_prod_include_rev_stats.json", ProductDisplayPageResponse.class);

        ReviewStatistics reviewStats = response.getResults().get(0).getReviewStatistics();
        RatingDistribution ratingDistribution = reviewStats.getRatingDistribution();
        assertEquals(22, ratingDistribution.getOneStarCount().intValue());
        assertEquals(21, ratingDistribution.getTwoStarCount().intValue());
        assertEquals(24, ratingDistribution.getThreeStarCount().intValue());
        assertEquals(70, ratingDistribution.getFourStarCount().intValue());
        assertEquals(139, ratingDistribution.getFiveStarCount().intValue());

        Map<String, SecondaryRatingsAverages> secondaryRatingsAvgs =
                reviewStats.getSecondaryRatingsAverages();
        float qualityRating = secondaryRatingsAvgs.get("Quality").getAverageOverallRating();
        assertEquals(2.0f, qualityRating);
    }

    @Test
    public void testPdpForAllProductsSortedParsing() {
        testParsing("product_catalog_all_prods_sorted.json", ProductDisplayPageResponse.class);
    }

    @Test
    public void testPdpForAllProductsInCategoryParsing() {
        testParsing("product_catalog_all_prods_in_category.json", ProductDisplayPageResponse.class);
    }

    @Test
    public void testPdpForTextSearchParsing() {
        testParsing("product_catalog_text_search.json", ProductDisplayPageResponse.class);
    }

    @Test
    public void testReviewsForAllReviewsParsing() {
        ReviewResponse response = testParsing("reviews_all_reviews.json", ReviewResponse.class);

        assertEquals(10, response.getResults().size());

        Review firstReview = response.getResults().get(0);
        Map<String, Badge> firstReviewBadges = firstReview.getBadges();
        Assert.assertNotNull(firstReviewBadges);
        Badge top10Badge = firstReviewBadges.get("top10");
        assertEquals("REVIEW", top10Badge.getContentType());
        assertEquals("top10", top10Badge.getId());
        assertEquals("Merit", top10Badge.getType().name());
    }

    @Test
    public void testReviewsForProdParsing() {
        testParsing("reviews_for_prod.json", ReviewResponse.class);
    }

    @Test
    public void testReviewsForAllReviewsIncludeRevStatsParsing() {
        testParsing("reviews_all_reviews_include_rev_stats.json", ReviewResponse.class);
    }

    @Test
    public void testReviewsForSingleReviewParsing() {
        testParsing("reviews_single_review.json", ReviewResponse.class);
    }

    @Test
    public void testReviewsForSingleReviewIncludeProdStatsParsing() {
        testParsing("reviews_single_review_include_prod_stats.json", ReviewResponse.class);
    }

    @Test
    public void testReviewsForSomeReviewsParsing() {
        ReviewResponse response = testParsing("reviews_some_reviews.json", ReviewResponse.class);
        Review firstReview = response.getResults().get(0);
        assertNotNull(firstReview.getAuthorId());
        assertNotNull(firstReview.getProductId());
        assertNotNull(firstReview.getModerationStatus());
        assertNotNull(firstReview.getBadges());
        assertNotNull(firstReview.getLastModificationDate());
        assertNotNull(firstReview.getLastModeratedDate());
        assertNotNull(firstReview.getClientResponses());
        assertNotNull(firstReview.getCampaignId());
        assertNotNull(firstReview.getFeatured());
    }

    @Test
    public void testQandAForSomeQuestionsWithAnswersParsing() {
        QuestionAndAnswerResponse response = testParsing("qanda_some_questions_with_answers.json", QuestionAndAnswerResponse.class);
        Question firstQuestion = response.getResults().get(0);
        assertNotNull(firstQuestion.getQuestionDetails());
        assertNotNull(firstQuestion.getQuestionSummary());
        assertNotNull(firstQuestion.getTagDimensions());
        assertNotNull(firstQuestion.getTagDimensions());
        assertNotNull(firstQuestion.getTotalAnswerCount());
        assertNotNull(firstQuestion.getTotalInappropriateFeedbackCount());
        assertNotNull(firstQuestion.getAdditionalFields());
        assertNotNull(firstQuestion.getAuthorId());
        assertNotNull(firstQuestion.getBadges());
        assertNotNull(firstQuestion.getCampaignId());
        assertNotNull(firstQuestion.getContentLocale());
        assertNotNull(firstQuestion.getContextDataValues());
        assertNotNull(firstQuestion.getFeatured());
        assertNotNull(firstQuestion.getId());
        assertNotNull(firstQuestion.getLastModeratedDate());
        assertNotNull(firstQuestion.getLastModificationDate());
        assertNotNull(firstQuestion.getModerationStatus());
        assertNotNull(firstQuestion.getProductId());

        Answer firstAnswer = firstQuestion.getAnswers().get(0);
        assertNotNull(firstAnswer.getBrandImageLogoUrl());
    }

    @Test
    public void testReviewSubmissionBuilder(){

        // Make sure the string is encoded properly
        String expectedResult = "bvAndroidSdkVersion=" + bvSdkVersion + "&passkey=a+differnt+key&UserNickname=nickname&Rating=5&ReviewText=This+is+the+review+text+the+user+adds+about+how+great+the+product+is%21&agreedToTermsAndConditions=true&Title=Android+SDK+Testing&apiversion=5.4&fp=abcdef%2B123345%2Fham%2Bbacon%2Beggs%2Bcaseylovestaters&ProductId=123987&UserEmail=foo%40bar.com&SendEmailAlertWhenCommented=true&UserId=user1234&action=Submit&sendemailalertwhenpublished=true";

        ReviewSubmissionRequest submission = new ReviewSubmissionRequest.Builder(Action.Submit, "123987")
                .fingerPrint("abcdef+123345/ham+bacon+eggs+caseylovestaters")
                .userNickname("nickname")
                .userEmail("foo@bar.com")
                .userId("user1234") // Creating a random user id to avoid duplicated -- FOR TESTING ONLY!!!
                .rating(5)
                .title("Android SDK Testing")
                .reviewText("This is the review text the user adds about how great the product is!")
                .sendEmailAlertWhenCommented(true)
                .sendEmailAlertWhenPublished(true)
                .agreedToTermsAndConditions(true)
                .build();

        String testString = submission.createUrlQueryString(submission.makeQueryParams());
        assertEquals(expectedResult, testString);

    }

    @Test
    public void testQuestionSubmissionBuilder(){

        // Make sure the string is encoded properly
        String expectedResult = "bvAndroidSdkVersion=" + bvSdkVersion + "&passkey=a+differnt+key&UserNickname=nickname&action=Submit&apiversion=5.4&fp=abcdef%2B123345%2Fham%2Bbacon%2Beggs%2Bcaseylovestaters&ProductId=123987&UserEmail=foo%40bar.com";

        QuestionSubmissionRequest submission = new QuestionSubmissionRequest.Builder(Action.Submit, "123987")
                .fingerPrint("abcdef+123345/ham+bacon+eggs+caseylovestaters")
                .userNickname("nickname")
                .userEmail("foo@bar.com")
                .build();

        String testString = submission.createUrlQueryString(submission.makeQueryParams());
        assertEquals(expectedResult, testString);

    }

    @Test
    public void testQuestionAnswerBuilder(){

        // Make sure the string is encoded properly
        String expectedResult = "bvAndroidSdkVersion=" + bvSdkVersion + "&passkey=a+differnt+key&UserNickname=nickname&QuestionId=123987&action=Submit&apiversion=5.4&fp=abcdef%2B123345%2Fham%2Bbacon%2Beggs%2Bcaseylovestaters&AnswerText=Let+me+google+that+for+you....&UserEmail=foo%40bar.com";

       AnswerSubmissionRequest submission = new AnswerSubmissionRequest.Builder(Action.Submit, "123987", "Let me google that for you....")
                .fingerPrint("abcdef+123345/ham+bacon+eggs+caseylovestaters")
                .userNickname("nickname")
                .userEmail("foo@bar.com")
                .build();

        String testString = submission.createUrlQueryString(submission.makeQueryParams());
        assertEquals(expectedResult, testString);

    }


    @Test
    public void testFeedbackSubmitHelpfulVote() {

        FeedbackSubmissionRequest request = new FeedbackSubmissionRequest.Builder("contentId")
                .feedbackType(FeedbackType.HELPFULNESS)
                .feedbackContentType(FeedbackContentType.REVIEW)
                .feedbackVote(FeedbackVoteType.POSITIVE)
                .userId("theUserId")
                .build();

        assertTrue("Request should not contain error", request.getError() == null);
    }

    @Test
    public void testFeedbackSubmitInappropriateFeedback(){

        FeedbackSubmissionRequest request = new FeedbackSubmissionRequest.Builder("contentId")
                .feedbackType(FeedbackType.INAPPROPRIATE)
                .feedbackContentType(FeedbackContentType.QUESTION)
                .userId("theUserId")
                .build();

        assertTrue("Request should not contain error.", request.getError() == null);

    }

    @Test
    public void testProductDisplayPageRequest() {
        ProductDisplayPageRequest pdpRequest = new ProductDisplayPageRequest.Builder("product_abc123")
                .addIncludeContent(PDPContentType.Answers, 20)
                .addIncludeContent(PDPContentType.Questions, 20)
                .addIncludeContent(PDPContentType.Reviews, 20)
                .addAnswerSort(AnswerOptions.Sort.SubmissionTime, SortOrder.DESC)
                .addQuestionSort(QuestionOptions.Sort.SubmissionTime, SortOrder.DESC)
                .addReviewSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC)
                .build();

        assertTrue("Request should not contain error", pdpRequest.getError() == null);
    }

    @Test
    public void testParsingFeedbackHelpfulnessResponse() {

        FeedbackSubmissionResponse response = testParsing("feedback_helpfulness_response.json", FeedbackSubmissionResponse.class);

        assertEquals(response.getHasErrors(), Boolean.FALSE);
        assertNotNull(response.getLocale());
        assertNull(response.getSubmissionId());
        assertNull(response.getTypicalHoursToPost());
        assertEquals(response.getErrors().size(), 0);
        assertNull(response.getFeedback().getInappropriateFeedback());

        assertEquals(response.getFeedback().getHelpfulnessFeedback().getAuthorId(), "alphauser");
        assertEquals(response.getFeedback().getHelpfulnessFeedback().getVote(), "POSITIVE");

    }

    @Test
    public void testParsingFeedbackInappropriateResponse() {

        FeedbackSubmissionResponse response = testParsing("feedback_inappropriate_response.json", FeedbackSubmissionResponse.class);

        assertEquals(response.getHasErrors(), Boolean.FALSE);
        assertNotNull(response.getLocale());
        assertNull(response.getSubmissionId());
        assertNull(response.getTypicalHoursToPost());
        assertEquals(response.getErrors().size(), 0);
        assertNull(response.getFeedback().getHelpfulnessFeedback());

        assertEquals(response.getFeedback().getInappropriateFeedback().getAuthorId(), "alphauser");
        assertEquals(response.getFeedback().getInappropriateFeedback().getReasonText(), "This is where the reason text would go");
    }

    @Test
    public void testErrorResponseNoAPIKey() {

        ConversationsResponse errorResponse = testParsing("conversations_error_no_apikey.json", ConversationsResponse.class);

        assertEquals(errorResponse.getHasErrors(), Boolean.TRUE);
        assertEquals(errorResponse.getErrors().size(), 1);

        Error firstError = errorResponse.getErrors().get(0);

        assertEquals(firstError.getCode(), "ERROR_PARAM_INVALID_API_KEY");
        assertEquals(firstError.getMessage(), "The passKey provided is invalid.");

    }

    private List<String> getProdIds(int limit){
        List<String> prodIds = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            prodIds.add("testProductId" + i);
        }

        return prodIds;
    }

    @Test
    public void testParsingAuthorsByIdWithIncludesSorted() {
        AuthorsResponse response = testParsing("author_by_id_with_options.json", AuthorsResponse.class);
        List<Author> authors = response.getResults();
        assertEquals(1, authors.size());
        assertEquals(1, response.getTotalResults().intValue());
        assertFalse(response.getHasErrors());

        Author firstAuthor = authors.get(0);
        assertEquals("tondra", firstAuthor.getUserNickname());
        Map<String, ContextDataValue> contextDataValueMap = firstAuthor.getContextDataValues();
        assertEquals(4, contextDataValueMap.size());
        ContextDataValue experienceDataValue = contextDataValueMap.get("Experience");
        ContextDataValue primaryUseDataValue = contextDataValueMap.get("PrimaryUse");
        ContextDataValue ageDataValue = contextDataValueMap.get("Age");
        ContextDataValue genderDataValue = contextDataValueMap.get("Gender");
        assertNotNull(experienceDataValue);
        assertNotNull(primaryUseDataValue);
        assertNotNull(ageDataValue);
        assertNotNull(genderDataValue);
        assertEquals("Beginner", experienceDataValue.getValue());
        assertEquals("Racing", primaryUseDataValue.getValue());
        assertEquals("25to34", ageDataValue.getValue());
        assertEquals("Female", genderDataValue.getValue());
        assertEquals("uayhe5twq2b3dokikrsqamtlc", firstAuthor.getId());
        assertEquals("Albany, GA, United States", firstAuthor.getLocation());
        assertEquals(Integer.valueOf(0), firstAuthor.getReviewStatistics().getTotalReviewCount());
        assertEquals(Integer.valueOf(1), firstAuthor.getQaStatistics().getTotalQuestionCount());
        assertEquals(Integer.valueOf(0), firstAuthor.getQaStatistics().getTotalAnswerCount());
        Calendar submissionDateCalendar = Calendar.getInstance();
        submissionDateCalendar.setTime(firstAuthor.getSubmissionDate());
        assertEquals(2016, submissionDateCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.MAY, submissionDateCalendar.get(Calendar.MONTH));
        assertEquals(17, submissionDateCalendar.get(Calendar.DAY_OF_MONTH));

        firstAuthor.getUserNickname();
        firstAuthor.getId();
        firstAuthor.getSubmissionDate();
        firstAuthor.getLastModeratedDate();
        firstAuthor.getTagDimensions();
        firstAuthor.getPhotos();
        firstAuthor.getContextDataValues();
        firstAuthor.getVideos();
        firstAuthor.getSubmissionId();
        firstAuthor.getUserLocation();
        firstAuthor.getBadges();
        firstAuthor.getSecondaryRatings();
        firstAuthor.getIncludedIn().getReviewsList();
        firstAuthor.getIncludedIn().getQuestions();
        firstAuthor.getIncludedIn().getAnswers();
        firstAuthor.getReviewStatistics();
        firstAuthor.getQaStatistics();
    }

    @Test
    public void testAuthorRequest() {
        AuthorsRequest request = new AuthorsRequest.Builder("authorId")
                .addIncludeContent(PDPContentType.Reviews, 10)
                .addIncludeContent(PDPContentType.Questions, 11)
                .addIncludeContent(PDPContentType.Answers, 12)
                .addReviewSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC)
                .addQuestionSort(QuestionOptions.Sort.SubmissionTime, SortOrder.DESC)
                .addAnswerSort(AnswerOptions.Sort.AuthorId, SortOrder.DESC)
                .addIncludeStatistics(PDPContentType.Reviews)
                .addIncludeStatistics(PDPContentType.Questions)
                .addIncludeStatistics(PDPContentType.Answers)
                .build();
    }
}