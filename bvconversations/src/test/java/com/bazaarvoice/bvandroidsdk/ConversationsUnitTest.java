package com.bazaarvoice.bvandroidsdk;

import com.bazaarvoice.bvandroidsdk.types.FeedbackContentType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackVoteType;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 24)
public class ConversationsUnitTest extends BVBaseTest {
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

        ReviewsRequest request = new ReviewsRequest.Builder("testProductId", 101, 0)
                .build();

        assertTrue("Request contains error but was not found", request.getError() != null);
    }

    @Test
    public void testReviewsRequestValidLimit() {

        ReviewsRequest request = new ReviewsRequest.Builder("testProductId", 20, 0)
                .addIncentivizedStats(true)
                .includeSearchPhrase("test")
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
    public void testPdpForProductIdIncludeRevStatsWithZeroRating() throws Exception {
        ProductDisplayPageResponse response = parseJsonResourceFile("product_catalog_prod_include_rev_stats_zero.json", ProductDisplayPageResponse.class, gson);

        ReviewStatistics reviewStats = response.getResults().get(0).getReviewStatistics();
        RatingDistribution ratingDistribution = reviewStats.getRatingDistribution();
        assertEquals(22, ratingDistribution.getOneStarCount().intValue());
        assertEquals(21, ratingDistribution.getTwoStarCount().intValue());
        assertEquals(24, ratingDistribution.getThreeStarCount().intValue());
        assertEquals(70, ratingDistribution.getFourStarCount().intValue());
        assertEquals(139, ratingDistribution.getFiveStarCount().intValue());
    }

    @Test
    public void testPdpForProductIdIncludeRevStatsWithEmptyRatingDistriubtion() throws Exception {
        ProductDisplayPageResponse response = parseJsonResourceFile("product_catalog_prod_include_rev_stats_empty.json", ProductDisplayPageResponse.class, gson);
        ReviewStatistics reviewStats = response.getResults().get(0).getReviewStatistics();
        RatingDistribution ratingDistribution = reviewStats.getRatingDistribution();
        assertNotNull(ratingDistribution);
        assertEquals(0, ratingDistribution.getOneStarCount().intValue());
        assertEquals(0, ratingDistribution.getTwoStarCount().intValue());
        assertEquals(0, ratingDistribution.getThreeStarCount().intValue());
        assertEquals(0, ratingDistribution.getFourStarCount().intValue());
        assertEquals(0, ratingDistribution.getFiveStarCount().intValue());
    }

    @Test
    public void testPdpForProductIdIncludeRevStatsWithNoStats() throws Exception {
        ProductDisplayPageResponse response = parseJsonResourceFile("product_catalog_prod_include_rev_stats_none.json", ProductDisplayPageResponse.class, gson);
        ReviewStatistics reviewStats = response.getResults().get(0).getReviewStatistics();
        assertNull(reviewStats);
    }

    @Test
    public void revStatsMapShouldHaveAllValues() throws Exception {
        ProductDisplayPageResponse response = parseJsonResourceFile("product_catalog_prod_include_rev_stats_zero.json", ProductDisplayPageResponse.class, gson);

        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 15);
        map.put(1, 22);
        map.put(2, 21);
        map.put(3, 24);
        map.put(4, 70);
        map.put(5, 139);
        ReviewStatistics reviewStats = response.getResults().get(0).getReviewStatistics();
        assert reviewStats != null;
        assertEquals(map, reviewStats.getRatingDistributionMap());
    }

    @Test
    public void revStatsMapShouldHaveNoNullValues() throws Exception {
        ProductDisplayPageResponse response = parseJsonResourceFile("product_catalog_prod_include_rev_stats_empty.json", ProductDisplayPageResponse.class, gson);
        Map<Integer, Integer> map = new HashMap<>();
        ReviewStatistics reviewStats = response.getResults().get(0).getReviewStatistics();
        assert reviewStats != null;
        assertEquals(map, reviewStats.getRatingDistributionMap());
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
    public void testReviewsForIncentivizedReviewParsing() throws Exception {
        ReviewResponse response = parseJsonResourceFile("review_incentivized.json", ReviewResponse.class, gson);

        assertEquals(20, response.getResults().size());

        Review firstReview = response.getResults().get(10);

        assertNotNull(response.getIncludes().getProducts().get(0).getReviewStatistics());

        assertNotNull(firstReview.getAuthorId());

        Map<String, Badge> reviewBadges = firstReview.getBadges();
        assertNotNull(reviewBadges);
        Badge top10Badge = reviewBadges.get("incentivizedReview");
        assertEquals("REVIEW", top10Badge.getContentType());
        assertEquals("incentivizedReview", top10Badge.getId());
        assertEquals("Custom", top10Badge.getType().name());


        for (Product includes : response.getIncludes().getProducts()) {

            assertNotNull(includes.getId());
            assertEquals(includes.getId(), "data-gen-moppq9ekthfzbc6qff3bqokie");

            // Review Statistics assertions
            assertNotNull(includes.getReviewStatistics());
            assertNotNull(includes.getReviewStatistics().getIncentivizedReviewCount());
            assertEquals(15, includes.getReviewStatistics().getIncentivizedReviewCount().intValue());
            assertNotNull(includes.getReviewStatistics().getContextDataDistribution().get("IncentivizedReview"));

            assertEquals("IncentivizedReview", includes.getReviewStatistics().getContextDataDistribution().get("IncentivizedReview").getId());
            assertEquals("Received an incentive for this review", includes.getReviewStatistics().getContextDataDistribution().get("IncentivizedReview").getLabel());
            assertEquals(1, includes.getReviewStatistics().getContextDataDistribution().get("IncentivizedReview").getValues().size());

        }

        assertNotNull(response.getIncludes().getProducts().get(0).getReviewStatistics());

    }

    @Test
    public void testReviewsWithDateOfUXParsing() throws Exception {
        ReviewResponse response = parseJsonResourceFile("reviews_all_reviews_dateofux.json", ReviewResponse.class, gson);
        Review review = response.getResults().get(0);
        Map<String, Object> additionalFields = review.getAdditionalFields();

        assertNotNull(additionalFields.get("DateOfUserExperience"));

        Map<String, String> dateOfConsumerExperienceField = (Map<String, String>) additionalFields.get("DateOfUserExperience");

        assertEquals("DateOfUserExperience", dateOfConsumerExperienceField.get("Id"));
        assertEquals("Date of user experience", dateOfConsumerExperienceField.get("Label"));
        assertNotNull(dateOfConsumerExperienceField.get("Value"));
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
        assertNotNull(firstReview.getTagDimensions());
        assertNotNull(firstReview.getTagDimensions().get("Pro").getValues());
        assertNotNull(firstReview.getTagDimensions().get("Con").getValues());
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
        assertEquals("testcust-bazaarvoice", firstReview.getSourceClient());
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
        assertEquals("conciergeapidocumentation", firstComment.getSourceClient());
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
        assertEquals("testcust-bazaarvoice", firstQuestion.getSourceClient());
        assertEquals("https://www.website.com/content/here", firstQuestion.getSyndicatedSource().getContentLink());
        assertEquals("https://www.website.com/image.png", firstQuestion.getSyndicatedSource().getLogoImageUrl());
        assertEquals("SourceName", firstQuestion.getSyndicatedSource().getName());

        Answer firstAnswer = firstQuestion.getAnswers().get(0);
        assertNotNull(firstAnswer.getBrandImageLogoUrl());
        assertTrue(firstAnswer.isSyndicated());
        assertEquals("testcust-bazaarvoice", firstAnswer.getSourceClient());
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
    public void testReviewDateOfConsumerExperienceFormFieldParsing() throws Exception {
        ReviewSubmissionResponse response = parseJsonResourceFile("review_submit_form_dateofux.json", ReviewSubmissionResponse.class, gson);

        List<FormField> formFields = response.getFormFields();
        assertNotNull(formFields);

        FormField dateOfUXFormField = formFields.get(43);
        assertNotNull(dateOfUXFormField);
        assertEquals("additionalfield_DateOfUserExperience", dateOfUXFormField.getId());
    }

    @Test
    public void testReviewSubmissionRequestForInvalidDateOfUXField() throws Exception {

        ReviewSubmissionResponse response = parseJsonResourceFile("review_submit_review_invalid_dateofux.json", ReviewSubmissionResponse.class, gson);

        assertNotNull(response.getFieldErrors());

        List<FieldError> fieldErrors = response.getFieldErrors();
        assertEquals(1, fieldErrors.size());

        FieldError invalidDateOfUX = fieldErrors.get(0);
        assertEquals("ERROR_FORM_PATTERN_MISMATCH", invalidDateOfUX.getCode());
        assertEquals(SubmissionErrorCode.ERROR_FORM_PATTERN_MISMATCH, invalidDateOfUX.getErrorCode());
        assertEquals("additionalfield_DateOfUserExperience", invalidDateOfUX.getField());
    }

    @Test
    public void testReviewSubmissionRequestForFutureDateOfUXField() throws Exception {

        ReviewSubmissionResponse response = parseJsonResourceFile("review_submit_review_future_dateofux.json", ReviewSubmissionResponse.class, gson);

        assertNotNull(response.getFieldErrors());

        List<FieldError> fieldErrors = response.getFieldErrors();
        assertEquals(1, fieldErrors.size());

        FieldError futureDateOfUX = fieldErrors.get(0);
        assertEquals("ERROR_FORM_FUTURE_DATE", futureDateOfUX.getCode());
        assertEquals(SubmissionErrorCode.ERROR_FORM_FUTURE_DATE, futureDateOfUX.getErrorCode());
        assertEquals("additionalfield_DateOfUserExperience", futureDateOfUX.getField());
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
    public void testFeedbackSubmitInappropriateFeedback() {

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

    @Test
    public void shouldGetOverallAverageRatingSafelyIfRevStatsIsNull() throws Exception {
        ReviewResponse response = parseJsonResourceFile("reviews_with_no_review_stats.json", ReviewResponse.class, gson);
        assertNull(response.getIncludes().getProducts().get(0).getReviewStatistics());
        assertEquals(0f, response.getIncludes().getProducts().get(0).getAverageRating());
    }

    private List<String> getProdIds(int limit) {
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

    @Test
    public void testReviewHighlightsParsing() throws Exception {
        ReviewHighlightsResponse response = parseJsonResourceFile("review_highlight_response.json", ReviewHighlightsResponse.class, gson);
        // check not null
        assertNotNull(response.getReviewHighlights());
        assertNotNull(response.getReviewHighlights().getNegatives());
        assertNotNull(response.getReviewHighlights().getPositives());
        // check not empty
        assertFalse(response.getReviewHighlights().getPositives().isEmpty());
        assertFalse(response.getReviewHighlights().getNegatives().isEmpty());
        // check count <= 5
        assertTrue(response.getReviewHighlights().getPositives().size() <= 5);
        assertTrue(response.getReviewHighlights().getNegatives().size() <= 5);
    }

    @Test
    public void testReviewHighlightsOnlyPros() throws Exception {
        ReviewHighlightsResponse response = parseJsonResourceFile("review_highlight_reponse_only_pros.json", ReviewHighlightsResponse.class, gson);
        // check not null
        assertNotNull(response.getReviewHighlights());
        assertNotNull(response.getReviewHighlights().getPositives());
        assertNotNull(response.getReviewHighlights().getNegatives());
        // check not empty
        assertTrue(response.getReviewHighlights().getNegatives().isEmpty());
        assertFalse(response.getReviewHighlights().getPositives().isEmpty());
        // check count <= 5
        assertTrue(response.getReviewHighlights().getPositives().size() <= 5);
    }

    @Test
    public void testReviewHighlightsOnlyCons() throws Exception {
        ReviewHighlightsResponse response = parseJsonResourceFile("review_highlight_reponse_only_cons.json", ReviewHighlightsResponse.class, gson);
        // check not null
        assertNotNull(response.getReviewHighlights());
        assertNotNull(response.getReviewHighlights().getNegatives());
        assertNotNull(response.getReviewHighlights().getPositives());
        // check not empty
        assertTrue(response.getReviewHighlights().getPositives().isEmpty());
        assertFalse(response.getReviewHighlights().getNegatives().isEmpty());
        // check count <= 5
        assertTrue(response.getReviewHighlights().getNegatives().size() <= 5);
    }

    @Test
    public void testReviewHighlightsSequence() throws Exception {
        //expected cons
        List<String> expectedNegatives = new ArrayList<String>(Arrays.asList("small", "large"));
        //expeted pros
        List<String> expectedPositives = new ArrayList<String>(Arrays.asList("cleaning", "satisfaction","ease of use"));

        ReviewHighlightsResponse response = parseJsonResourceFile("review_highlight_response.json", ReviewHighlightsResponse.class, gson);
        List<ReviewHighlight> negatives = response.getReviewHighlights().getNegatives();
        List<ReviewHighlight> positives = response.getReviewHighlights().getPositives();
        // check not null title & sequence
        int i = 0;
        for (ReviewHighlight positive : positives) {
            assertNotNull(positive.title);
            assertEquals(positive.title,expectedPositives.get(i));
            i++;
        }

        i = 0;
        for (ReviewHighlight negetive : negatives) {
            assertNotNull(negetive.title);
            assertEquals(negetive.title,expectedNegatives.get(i));
            i++;
        }
    }

    @Test
    public void testBulkRatingParsing() throws Exception {
        BulkRatingsResponse response = parseJsonResourceFile("bulk_ratings_response.json", BulkRatingsResponse.class, gson);
        //check count and result
        assertEquals(1, response.getTotalResults().intValue());
        assertNotNull(response.getResults());
        for (Statistics stats : response.getResults()) {

            ProductStatistics productStats = stats.getProductStatistics();
            ReviewStatistics reviewStats = productStats.getReviewStatistics();
            ReviewStatistics nativeStats = productStats.getNativeReviewStatistics();
            //check incentivized count
            assertEquals(15, reviewStats.getIncentivizedReviewCount().intValue());
            assertEquals(15, nativeStats.getIncentivizedReviewCount().intValue());
            //check response properties count
            assertEquals(4.3818f, reviewStats.getAverageOverallRating());
            assertEquals(4.3818f, nativeStats.getAverageOverallRating());
            assertEquals(55, reviewStats.getTotalReviewCount().intValue());
            assertEquals(55, nativeStats.getTotalReviewCount().intValue());
            assertEquals(5, reviewStats.getOverallRatingRange().intValue());
            assertEquals(5, nativeStats.getOverallRatingRange().intValue());
        }

    }

    @Test
    public void testProductIncentivizedResponse() throws Exception {
        ProductDisplayPageResponse response = parseJsonResourceFile("product_page_display_incentivized_response.json", ProductDisplayPageResponse.class, gson);

        ReviewStatistics reviewStats = response.getResults().get(0).getReviewStatistics();

        //check for IR count
        assertNotNull(reviewStats.getIncentivizedReviewCount().intValue());
        assertNotNull(reviewStats.getContextDataDistribution().get("IncentivizedReview"));
        assertEquals(15,reviewStats.getIncentivizedReviewCount().intValue());

        //check for IR in ContextDataDistribution
        assertEquals(1, reviewStats.getContextDataDistribution().get("IncentivizedReview").getValues().size());
        assertEquals("IncentivizedReview", reviewStats.getContextDataDistribution().get("IncentivizedReview").getId());
        assertEquals("Received an incentive for this review", reviewStats.getContextDataDistribution().get("IncentivizedReview").getLabel());

        //check for IR in ContextDataDistribution values
        List<DistributionValue> distributionValue = reviewStats.getContextDataDistribution().get("IncentivizedReview").getValues();
        assertEquals(15,distributionValue.get(0).getCount().intValue());
        assertEquals("True",distributionValue.get(0).getValue());
    }

    @Test
    public void testBulkProductIncentivizedResponse() throws Exception {
        BulkProductResponse response = parseJsonResourceFile("product_page_display_incentivized_response.json", BulkProductResponse.class, gson);

        ReviewStatistics reviewStats = response.getResults().get(0).getReviewStatistics();

        //check for IR count
        assertNotNull(reviewStats.getIncentivizedReviewCount().intValue());
        assertNotNull(reviewStats.getContextDataDistribution().get("IncentivizedReview"));
        assertEquals(15,reviewStats.getIncentivizedReviewCount().intValue());

        //check for IR in ContextDataDistribution
        assertEquals(1, reviewStats.getContextDataDistribution().get("IncentivizedReview").getValues().size());
        assertEquals("IncentivizedReview", reviewStats.getContextDataDistribution().get("IncentivizedReview").getId());
        assertEquals("Received an incentive for this review", reviewStats.getContextDataDistribution().get("IncentivizedReview").getLabel());

        //check for IR in ContextDataDistribution values
        List<DistributionValue> distributionValue = reviewStats.getContextDataDistribution().get("IncentivizedReview").getValues();
        assertEquals(15,distributionValue.get(0).getCount().intValue());
        assertEquals("True",distributionValue.get(0).getValue());
    }

    @Test
    public void testParsingReviewRequestWithAuthorInclude() throws Exception {
        ReviewResponse response = parseJsonResourceFile("review_request_include_author.json", ReviewResponse.class, gson);

        Author author = response.getIncludes().getAuthors().get(0);
        assertNotNull(author);
        assertEquals(0, author.getReviewStatistics().getHelpfulVoteCount().intValue());
        assertEquals(1, author.getReviewStatistics().getTotalReviewCount().intValue());

    }

    @Test
    public void testTopicFeatureResponse() throws Exception {
        FeaturesResponse response = parseJsonResourceFile("topic_feature_response.json", FeaturesResponse.class, gson);

        assertNotNull(response.getResults());
        assertEquals("XYZ123-prod-3-4-ExternalId", response.getResults().get(0).getProductId());
        assertEquals("speed", response.getResults().get(0).getFeatures().get(0).getFeature());
        assertEquals("speed", response.getResults().get(0).getFeatures().get(0).getLocalizedFeature());


    }

    @Test
    public void testReviewsForSecondaryRatingsReviewParsing() throws Exception {
        ReviewResponse response = parseJsonResourceFile("review_secondary_stats.json", ReviewResponse.class, gson);
        for (Product includes : response.getIncludes().getProducts()) {
            assertNotNull(includes.getReviewStatistics().getSecondaryRatingsDistribution());
        }
        assertEquals("WhatSizeIsTheProduct", response.getIncludes().getProducts().get(0).getReviewStatistics().getSecondaryRatingsDistribution().get("WhatSizeIsTheProduct").getId());
        assertEquals(2, response.getIncludes().getProducts().get(0).getReviewStatistics().getSecondaryRatingsDistribution().get("WhatSizeIsTheProduct").getValues().get(0).getValue().intValue());
        assertEquals(6, response.getIncludes().getProducts().get(0).getReviewStatistics().getSecondaryRatingsDistribution().get("WhatSizeIsTheProduct").getValues().get(0).getCount().intValue());
        assertEquals("Medium", response.getIncludes().getProducts().get(0).getReviewStatistics().getSecondaryRatingsDistribution().get("WhatSizeIsTheProduct").getValues().get(0).getValueLabel());
    }

    @Test
    public void testReviewsForTagStatsReviewParsing() throws Exception {
        ReviewResponse response = parseJsonResourceFile("review_tag_stats.json", ReviewResponse.class, gson);
        for (Product includes : response.getIncludes().getProducts()) {
            assertNotNull(includes.getReviewStatistics().getTagDistribution());
        }
        assertEquals("ProductVariant", response.getIncludes().getProducts().get(0).getReviewStatistics().getTagDistribution().get("ProductVariant").getId());
        assertEquals("Gray", response.getIncludes().getProducts().get(0).getReviewStatistics().getTagDistribution().get("ProductVariant").getValues().get(0).getValue());
        assertEquals(9, response.getIncludes().getProducts().get(0).getReviewStatistics().getTagDistribution().get("ProductVariant").getValues().get(0).getCount().intValue());
    }

    @Test
    public void testValueLabelReviewResponse() throws Exception {
        ReviewResponse response = parseJsonResourceFile("reviews_include_value_label.json", ReviewResponse.class, gson);

        for (Product includes : response.getIncludes().getProducts()) {
            assertEquals("17 or under", includes.getReviewStatistics().getContextDataDistribution().get("Age").getValues().get(0).getValueLabel());
        }
    }

    @Test
    public void testValueLabelSecondaryRatingsAveragesReviewResponse() throws Exception {
        ReviewResponse response = parseJsonResourceFile("review_value_lable_secondary_ratings_averages.json", ReviewResponse.class, gson);
        for (Product includes : response.getIncludes().getProducts()) {
            assertNotNull(includes.getReviewStatistics().getSecondaryRatingsAverages());
        }
        assertEquals("WhatSizeIsTheProduct", response.getIncludes().getProducts().get(0).getReviewStatistics().getSecondaryRatingsAverages().get("WhatSizeIsTheProduct").getId());
        assertEquals("SLIDER", response.getIncludes().getProducts().get(0).getReviewStatistics().getSecondaryRatingsAverages().get("WhatSizeIsTheProduct").getDisplayType());
        assertEquals(3, response.getIncludes().getProducts().get(0).getReviewStatistics().getSecondaryRatingsAverages().get("WhatSizeIsTheProduct").getValueRange().intValue());
        assertEquals("Large", response.getIncludes().getProducts().get(0).getReviewStatistics().getSecondaryRatingsAverages().get("WhatSizeIsTheProduct").getMaxLabel());
        assertEquals("Small", response.getIncludes().getProducts().get(0).getReviewStatistics().getSecondaryRatingsAverages().get("WhatSizeIsTheProduct").getMinLabel());
        assertEquals("What size is the product?", response.getIncludes().getProducts().get(0).getReviewStatistics().getSecondaryRatingsAverages().get("WhatSizeIsTheProduct").getLabel());
        assertEquals("Small", response.getIncludes().getProducts().get(0).getReviewStatistics().getSecondaryRatingsAverages().get("WhatSizeIsTheProduct").getValueLabel().get(0));
    }
    
    @Test
    public void testQAStatsStatisticsResponse() throws Exception {
        BulkRatingsResponse response = parseJsonResourceFile("statistics_qa_stats.json", BulkRatingsResponse.class, gson);

        assertEquals(3, response.getResults().get(0).getProductStatistics().getQAStatistics().getTotalQuestionCount().intValue());
        assertEquals(5, response.getResults().get(0).getProductStatistics().getQAStatistics().getTotalAnswerCount().intValue());

    }

    @Test
    public void testReviewForOriginalProductName() throws Exception {

        ReviewResponse response = parseJsonResourceFile("reviews_with_original_product_name.json", ReviewResponse.class, gson);
        Review firstReview = response.getResults().get(0);

        assertEquals("14K White Gold Diamond Teardrop Necklace", firstReview.getOriginalProductName());

    }

    @Test
    public void testReivewSummaryResponse() throws Exception {
        ReviewSummaryResponse response = parseJsonResourceFile("review_summary_response.json", ReviewSummaryResponse.class, gson);

        assertNotNull(response.getReviewSummary());
        assertEquals("This outdoor jacket receives enthusiastic reviews for its versatile design and performance features. Users praise its lightweight, breathable fabric and water-resistant properties. The fit accommodates various body types comfortably. Reviewers appreciate its functionality for different activities and climates. Many highlight the stylish look and durability after repeated use. Customers find it great value and often purchase multiple colors. Overall, users highly recommend this jacket for its blend of style, comfort, and practicality.", response.getReviewSummary().getSummary());
        assertEquals("TEXT", response.getReviewSummary().getType());
        assertEquals("REVIEW_SUMMARY", response.getReviewSummary().getTitle());
        assertEquals("PARAGRAPH_SUMMARY", response.getReviewSummary().getDetail());
        assertEquals("This summary is generated by artificial intelligence based on customer reviews", response.getReviewSummary().getDisclaimer());

    }

    @Test
    public void testReviewTokensResponseParsing() throws Exception {
        ReviewTokensResponse response = parseJsonResourceFile("reviews_tokens.json", ReviewTokensResponse.class, gson);

        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getData());
        assertEquals(5, response.getData().size());

        // Verify the token values
        assertTrue(response.getData().contains("Durability"));
        assertTrue(response.getData().contains("Features"));
        assertTrue(response.getData().contains("Packaging"));
        assertTrue(response.getData().contains("Quality"));
        assertTrue(response.getData().contains("Value"));
    }

    @Test
    public void testMatchedTokensResponseParsing() throws Exception {
        MatchedTokensResponse response = parseJsonResourceFile("matched_tokens.json", MatchedTokensResponse.class, gson);

        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getData());

        // Verify matched tokens structure
        List<String> matchedTokens = response.getData();
        assertFalse(matchedTokens.isEmpty());
    }
}