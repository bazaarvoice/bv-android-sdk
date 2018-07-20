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
import java.util.Date;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19)
public class ConversationsUnitTest extends BVBaseTest{
    private static final String UTF_8 = "UTF-8";

    @Override
    protected void modifyPropertiesToInitSDK() {
        bazaarvoiceApiBaseUrl = "https://examplesite/";
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

    @Test
    public void testBulkProductsForAllProductsParsing() throws Exception {
        parseJsonResourceFile("product_catalog_all_products.json", BulkProductResponse.class, gson);
    }

    @Test
    public void testPdpForProductIdParsing() throws Exception {
        parseJsonResourceFile("product_catalog_product_id.json", ProductDisplayPageResponse.class, gson);
    }

    @Test
    public void testPdpForProductIdIncludeRevStatsParsing() throws Exception {
        ProductDisplayPageResponse response = parseJsonResourceFile("product_catalog_prod_include_rev_stats.json", ProductDisplayPageResponse.class, gson);

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
    public void testPdpForAllProductsSortedParsing() throws Exception {
        parseJsonResourceFile("product_catalog_all_prods_sorted.json", ProductDisplayPageResponse.class, gson);
    }

    @Test
    public void testPdpForAllProductsInCategoryParsing() throws Exception {
        parseJsonResourceFile("product_catalog_all_prods_in_category.json", ProductDisplayPageResponse.class, gson);
    }

    @Test
    public void testPdpForTextSearchParsing() throws Exception {
        parseJsonResourceFile("product_catalog_text_search.json", ProductDisplayPageResponse.class, gson);
    }

    @Test
    public void testReviewsForAllReviewsParsing() throws Exception {
        ReviewResponse response = parseJsonResourceFile("reviews_all_reviews.json", ReviewResponse.class, gson);

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
    public void testReviewsForProdParsing() throws Exception {
        parseJsonResourceFile("reviews_for_prod.json", ReviewResponse.class, gson);
    }

    @Test
    public void testReviewsForAllReviewsIncludeRevStatsParsing() throws Exception {
        parseJsonResourceFile("reviews_all_reviews_include_rev_stats.json", ReviewResponse.class, gson);
    }

    @Test
    public void testReviewsForAllReviewsIncludeFilteredRevStatsParsing() throws Exception {
        ReviewResponse response = parseJsonResourceFile("reviews_all_reviews_include_filtered_rev_stats.json", ReviewResponse.class, gson);
        List<Product> products = response.getIncludes().getProducts();
        assertEquals(1, products.size());
        Product product = products.get(0);
        ReviewStatistics reviewStatistics = product.getReviewStatistics();
        assertEquals(8, reviewStatistics.getTotalReviewCount().intValue());
    }

    @Test
    public void testReviewsForSingleReviewParsing() throws Exception {
        parseJsonResourceFile("reviews_single_review.json", ReviewResponse.class, gson);
    }

    @Test
    public void testReviewsForSingleReviewIncludeProdStatsParsing() throws Exception {
        parseJsonResourceFile("reviews_single_review_include_prod_stats.json", ReviewResponse.class, gson);
    }

    @Test
    public void testReviewsForSomeReviewsParsing() throws Exception {
        ReviewResponse response = parseJsonResourceFile("reviews_some_reviews.json", ReviewResponse.class, gson);
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
        assertFalse("IsSyndicated should be false", firstReview.getSyndicated());
        assertNull(firstReview.getSyndicatedSource());
    }

    @Test
    public void testReviewForSyndicatedSource() throws Exception {

        ReviewResponse response = parseJsonResourceFile("reviews_syndicated_source.json", ReviewResponse.class, gson);
        Review firstReview = response.getResults().get(0);

        assertTrue("IsSyndicated should be true", firstReview.getSyndicated());
        assertNotNull(firstReview.getSyndicatedSource());
        assertEquals("bazaarvoice", firstReview.getSyndicatedSource().getName());
        assertEquals("https://foo.com/hihowareyou.png", firstReview.getSyndicatedSource().getLogoImageUrl());

    }

    @Test
    public void testReviewsForCommentsParsing() throws Exception {
        ReviewResponse response = parseJsonResourceFile("reviews_include_comments.json", ReviewResponse.class, gson);
        Review review = response.getResults().get(0);
        List<Comment> comments = review.getComments();
        Comment firstComment = comments.get(0);

        assertEquals("338201", firstComment.getId());
        assertEquals("16970457", firstComment.getReviewId());
        assertEquals("data-gen-user-poaouvr127us1ijhpafkfacb9", firstComment.getAuthorId());
        assertEquals("ferdinand255", firstComment.getUserNickname());
        assertEquals("Proin accumsan tempor orci, nec condimentum enim malesuadaet.", firstComment.getCommentText());
        assertEquals("Aliquam lacinia auctoraccumsan.", firstComment.getTitle());

        Date submissionDate = firstComment.getSubmissionDate();
    }

    @Test
    public void testCommentsForReviewParsing() throws Exception {
        CommentsResponse response = parseJsonResourceFile("comments_for_review.json", CommentsResponse.class, gson);
        Comment firstComment = response.getResults().get(0);
        assertEquals("338201", firstComment.getId());
        assertEquals("ferdinand255", firstComment.getUserNickname());
        assertEquals("Proin accumsan tempor orci, nec condimentum enim malesuadaet.\n\nMaecenas sagittis, purus ac pulvinar dignissim, urna lacus lobortis elit, ac mollis magna elit velest.", firstComment.getCommentText());
        assertEquals(Integer.valueOf(9), firstComment.getTotalFeedbackCount());
        assertTrue(firstComment.isSyndicated());
        assertEquals("https://www.website.com/content/here", firstComment.getSyndicatedSource().getContentLink());
        assertEquals("https://www.website.com/image.png", firstComment.getSyndicatedSource().getLogoImageUrl());
        assertEquals("SourceName", firstComment.getSyndicatedSource().getName());
    }

    @Test
    public void testQandAForSomeQuestionsWithAnswersParsing() throws Exception {
        QuestionAndAnswerResponse response = parseJsonResourceFile("qanda_some_questions_with_answers.json", QuestionAndAnswerResponse.class, gson);
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
        assertTrue(firstQuestion.isSyndicated());
        assertEquals("https://www.website.com/content/here", firstQuestion.getSyndicatedSource().getContentLink());
        assertEquals("https://www.website.com/image.png", firstQuestion.getSyndicatedSource().getLogoImageUrl());
        assertEquals("SourceName", firstQuestion.getSyndicatedSource().getName());

        Answer firstAnswer = firstQuestion.getAnswers().get(0);
        assertNotNull(firstAnswer.getBrandImageLogoUrl());
        assertTrue(firstAnswer.isSyndicated());
        assertEquals("https://www.website.com/content/here", firstAnswer.getSyndicatedSource().getContentLink());
        assertEquals("https://www.website.com/image.png", firstAnswer.getSyndicatedSource().getLogoImageUrl());
        assertEquals("SourceName", firstAnswer.getSyndicatedSource().getName());
    }

    @Test
    public void testReviewFormCdvParsing() throws Exception {
        ReviewSubmissionResponse response = parseJsonResourceFile("review_submit_form.json", ReviewSubmissionResponse.class, gson);
        assertFalse(response.getHasErrors());

        List<FormField> formFields = response.getFormFields();
        assertNotNull(formFields);

        FormField ageFormField = formFields.get(12);
        assertNotNull(ageFormField);
        assertFalse(ageFormField.isRequired());

        assertTrue(ageFormField.getFormInputType() == FormInputType.SELECT);

        List<FormFieldOption> ageOptions = ageFormField.getFormFieldOptions();
        assertEquals(8, ageOptions.size());

        FormFieldOption ageOption = ageOptions.get(7);
        assertEquals("65orOver", ageOption.getValue());
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
    public void testProductDisplayPageRequestWithSort() {
        ProductDisplayPageRequest pdpRequest = new ProductDisplayPageRequest.Builder("product_abc123")
            .addIncludeContent(PDPContentType.Answers, 20)
            .addIncludeContent(PDPContentType.Questions, 20)
            .addIncludeContent(PDPContentType.Reviews, 20)
            .addSort(ProductOptions.Sort.Id, SortOrder.ASC)
            .build();

        assertTrue("Request should not contain error", pdpRequest.getError() == null);
    }

    @Test
    public void testProductDisplayPageRequestWithFilter() {
        ProductDisplayPageRequest pdpRequest = new ProductDisplayPageRequest.Builder("product_abc123")
            .addIncludeContent(PDPContentType.Answers, 20)
            .addIncludeContent(PDPContentType.Questions, 20)
            .addIncludeContent(PDPContentType.Reviews, 20)
            .addFilter(ProductOptions.Filter.CategoryId, EqualityOperator.EQ, "123")
            .build();

        assertTrue("Request should not contain error", pdpRequest.getError() == null);
    }

    @Test
    public void testParsingFeedbackHelpfulnessResponse() throws Exception {

        FeedbackSubmissionResponse response = parseJsonResourceFile("feedback_helpfulness_response.json", FeedbackSubmissionResponse.class, gson);

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
    public void testParsingFeedbackInappropriateResponse() throws Exception {

        FeedbackSubmissionResponse response = parseJsonResourceFile("feedback_inappropriate_response.json", FeedbackSubmissionResponse.class, gson);

        assertEquals(response.getHasErrors(), Boolean.FALSE);
        assertNotNull(response.getLocale());
        assertNull(response.getSubmissionId());
        assertNull(response.getTypicalHoursToPost());
        assertEquals(response.getErrors().size(), 0);
        assertNull(response.getFeedback().getHelpfulnessFeedback());

        assertEquals(response.getFeedback().getInappropriateFeedback().getAuthorId(), "alphauser");
        assertEquals(response.getFeedback().getInappropriateFeedback().getReasonText(), "This is where the reason text would go");
    }

    private List<String> getProdIds(int limit){
        List<String> prodIds = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            prodIds.add("testProductId" + i);
        }

        return prodIds;
    }

    @Test
    public void testParsingAuthorsByIdWithIncludesSorted() throws Exception {
        AuthorsResponse response = parseJsonResourceFile("author_by_id_with_options.json", AuthorsResponse.class, gson);
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

}