package com.bazaarvoice.bvandroidsdk;

import com.bazaarvoice.bvandroidsdk.types.FeedbackContentType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackVoteType;
import com.bazaarvoice.bvandroidsdk_common.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19)
public class ConversationsUnitTest extends BVBaseTest{

    @Override
    void modifyPropertiesToInitSDK() {
        conversationsApiBaseUrl = "a different one";
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
        testParsing("product_catalog_prod_include_rev_stats.json", ProductDisplayPageResponse.class);
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
        testParsing("reviews_all_reviews.json", ReviewResponse.class);
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
    public void testFeedbackSubmitHelpfulVote() {

        FeedbackSubmissionRequest request = new FeedbackSubmissionRequest.Builder(Action.Preview, "contentId")
                .feedbackType(FeedbackType.HELPFULNESS)
                .feedbackContentType(FeedbackContentType.REVIEW)
                .feedbackVote(FeedbackVoteType.POSITIVE)
                .userId("theUserId")
                .build();

        assertTrue("Request should not contain error", request.getError() == null);
    }

    @Test
    public void testFeedbackSubmitInappropriateFeedback(){

        FeedbackSubmissionRequest request = new FeedbackSubmissionRequest.Builder(Action.Preview, "contentId")
                .feedbackType(FeedbackType.INAPPROPRIATE)
                .feedbackContentType(FeedbackContentType.QUESTION)
                .userId("theUserId")
                .build();

        assertTrue("Request should not contain error.", request.getError() == null);

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
}