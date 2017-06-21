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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;

import static com.bazaarvoice.bvandroidsdk.Action.Submit;
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
    public void testReviewDisplayRequest() throws Exception {
        String productId = "productId";
        int limit = 50;
        int offset = 0;
        String authorId = "author1";
        String customKey = "a custom + key";
        String customValue = "a custom & value";

        ReviewsRequest request = new ReviewsRequest.Builder(productId, limit, offset)
            .addFilter(ReviewOptions.Filter.AuthorId, EqualityOperator.EQ, authorId)
            .addSort(ReviewOptions.Sort.IsFeatured, SortOrder.DESC)
            .addAdditionalField(customKey, customValue)
            .addIncludeContent(ReviewIncludeType.PRODUCTS)
            .addIncludeContent(ReviewIncludeType.COMMENTS)
            .build();
        String actualUrlStr = request.toHttpUrl().toString();

        String expectedTemplate = "https://examplesite/data/reviews.json?apiversion=%1$s&passkey=%2$s&_appId=%3$s&_appVersion=%4$s&_buildNumber=%5$s&_bvAndroidSdkVersion=%6$s&Filter=%7$s&Filter=%8$s&%9$s=%10$s&Limit=%11$s&Offset=%12$s&Include=%13$s&Sort=%14$s";
        String expectedStr = String.format(expectedTemplate,
            "5.4",
            bvUserProvidedData.getBvApiKeys().getApiKeyConversations(),
            packageName,
            versionName,
            versionCode,
            bvSdkVersion,
            "ProductId:eq:productId",
            "AuthorId:eq:author1",
            URLEncoder.encode(customKey, "UTF-8")
                .replaceAll("\\+", "%20"),
            URLEncoder.encode(customValue, "UTF-8")
                .replaceAll("\\+", "%20"),
            limit,
            offset,
            "products,comments",
            "IsFeatured:desc");

        assertEquals(expectedStr, actualUrlStr);
    }

    @Test
    public void basicReviewDisplayRequest() {
        ReviewsRequest request = new ReviewsRequest.Builder("product123", 20, 0)
            .addIncludeContent(ReviewIncludeType.PRODUCTS)
            .addIncludeContent(ReviewIncludeType.COMMENTS)
            .build();
        HttpUrl httpUrl = request.toHttpUrl();
        assertTrue(httpUrl.toString().contains("https://examplesite/data/reviews.json"));
        assertTrue(httpUrl.query().contains("Include=products"));
    }

    @Test
    public void basicStoreReviewDisplayRequest() {
        StoreReviewsRequest request = new StoreReviewsRequest.Builder("product123", 20, 0)
            .addIncludeContent(ReviewIncludeType.PRODUCTS)
            .addIncludeContent(ReviewIncludeType.COMMENTS)
            .build();
        HttpUrl httpUrl = request.toHttpUrl();
        assertTrue(httpUrl.toString().contains("https://examplesite/data/reviews.json"));
        assertTrue(httpUrl.query().contains("Include=products"));
    }

    @Test
    public void basicQuestionDisplayRequest() {
        QuestionAndAnswerRequest request = new QuestionAndAnswerRequest.Builder("product123", 20, 0).build();
        HttpUrl httpUrl = request.toHttpUrl();
        assertTrue(httpUrl.toString().contains("https://examplesite/data/questions.json"));
    }

    @Test
    public void basicPdpDisplayRequest() {
        ProductDisplayPageRequest request = new ProductDisplayPageRequest.Builder("product123").build();
        HttpUrl httpUrl = request.toHttpUrl();
        assertTrue(httpUrl.toString().contains("https://examplesite/data/products.json"));
    }

    @Test
    public void basicCommentsDisplayRequest() {
        CommentsRequest request = new CommentsRequest.Builder("review123", 20, 0).build();
        HttpUrl httpUrl = request.toHttpUrl();
        assertTrue(httpUrl.toString().contains("https://examplesite/data/reviewcomments.json"));
    }

    @Test
    public void basicBulkProductDisplayRequest() {
        BulkProductRequest request = new BulkProductRequest.Builder().build();
        HttpUrl httpUrl = request.toHttpUrl();
        assertTrue(httpUrl.toString().contains("https://examplesite/data/products.json"));
    }

    @Test
    public void basicBulkReviewDisplayRequest() {
        BulkRatingsRequest request = new BulkRatingsRequest.Builder(new ArrayList<String>(), BulkRatingOptions.StatsType.All).build();
        HttpUrl httpUrl = request.toHttpUrl();
        assertTrue(httpUrl.toString().contains("https://examplesite/data/statistics.json"));
    }

    @Test
    public void basicBulkStoreReviewDisplayRequest() {
        BulkStoreRequest request = new BulkStoreRequest.Builder(20, 0).build();
        HttpUrl httpUrl = request.toHttpUrl();
        assertTrue(httpUrl.toString().contains("https://examplesite/data/products.json"));
    }

    @Test
    public void basicAuthorDisplayRequest() {
        AuthorsRequest request = new AuthorsRequest.Builder("authorId").build();
        HttpUrl httpUrl = request.toHttpUrl();
        assertTrue(httpUrl.toString().contains("https://examplesite/data/authors.json"));
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
    public void testReviewsRequestIncludes() {
        ReviewsRequest request = new ReviewsRequest.Builder("testProductId", 20 , 0)
            .addIncludeContent(ReviewIncludeType.PRODUCTS)
            .addIncludeContent(ReviewIncludeType.COMMENTS)
            .build();

        String requestUrl = request.toHttpUrl().toString();
        assertTrue(requestUrl.contains("Include=products,comments"));
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
    public void testBulkProductsForAllProductsParsing() {
        testParsing("product_catalog_all_products.json", BulkProductResponse.class);
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
    public void testReviewsForAllReviewsIncludeFilteredRevStatsParsing() {
        ReviewResponse response = testParsing("reviews_all_reviews_include_filtered_rev_stats.json", ReviewResponse.class);
        List<Product> products = response.getIncludes().getProducts();
        assertEquals(1, products.size());
        Product product = products.get(0);
        ReviewStatistics reviewStatistics = product.getReviewStatistics();
        assertEquals(8, reviewStatistics.getTotalReviewCount().intValue());
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
        assertFalse("IsSyndicated should be false", firstReview.getSyndicated());
        assertNull(firstReview.getSyndicatedSource());
    }

    @Test
    public void testReviewForSyndicatedSource(){

        ReviewResponse response = testParsing("reviews_syndicated_source.json", ReviewResponse.class);
        Review firstReview = response.getResults().get(0);

        assertTrue("IsSyndicated should be true", firstReview.getSyndicated());
        assertNotNull(firstReview.getSyndicatedSource());
        assertEquals("bazaarvoice", firstReview.getSyndicatedSource().getName());
        assertEquals("https://foo.com/hihowareyou.png", firstReview.getSyndicatedSource().getLogoImageUrl());

    }

    @Test
    public void testReviewsForCommentsParsing() {
        ReviewResponse response = testParsing("reviews_include_comments.json", ReviewResponse.class);
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
    public void testCommentsForReviewParsing() {
        CommentsResponse response = testParsing("comments_for_review.json", CommentsResponse.class);
        Comment firstComment = response.getResults().get(0);
        assertEquals("338201", firstComment.getId());
        assertEquals("ferdinand255", firstComment.getUserNickname());
        assertEquals("Proin accumsan tempor orci, nec condimentum enim malesuadaet.\n\nMaecenas sagittis, purus ac pulvinar dignissim, urna lacus lobortis elit, ac mollis magna elit velest.", firstComment.getCommentText());
        assertEquals(Integer.valueOf(9), firstComment.getTotalFeedbackCount());
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
        assertFalse(firstQuestion.isSyndicated());

        Answer firstAnswer = firstQuestion.getAnswers().get(0);
        assertNotNull(firstAnswer.getBrandImageLogoUrl());
        assertFalse(firstAnswer.isSyndicated());
    }

    @Test
    public void testReviewSubmissionBuilder(){
        // Make sure the string is encoded properly
        ReviewSubmissionRequest submission = new ReviewSubmissionRequest.Builder(Submit, "123987")
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
            .addVideoUrl("https://www.website.com/path/to/video12.mp4", "how to farm taters")
            .addVideoUrl("https://www.website.com/path/to/video8.mp4", "how to market taters")
            .build();

        String formBody = submission.createUrlQueryString(submission.makeQueryParams());
        assertTrue(formBody.contains("fp=abcdef%2B123345%2Fham%2Bbacon%2Beggs%2Bcaseylovestaters"));
        assertTrue(formBody.contains("UserNickname=nickname"));
        assertTrue(formBody.contains("_appVersion=" + versionName));
        assertTrue(formBody.contains("passkey=" + bvUserProvidedData.getBvApiKeys().getApiKeyConversations()));
        assertTrue(formBody.contains("Rating=5"));
        assertTrue(formBody.contains("ReviewText=This+is+the+review+text+the+user+adds+about+how+great+the+product+is%21"));
        assertTrue(formBody.contains("agreedToTermsAndConditions=true"));
        assertTrue(formBody.contains("Title=Android+SDK+Testing"));
        assertTrue(formBody.contains("apiversion=5.4"));
        assertTrue(formBody.contains("ProductId=123987"));
        assertTrue(formBody.contains("UserEmail=foo%40bar.com"));
        assertTrue(formBody.contains("SendEmailAlertWhenCommented=true"));
        assertTrue(formBody.contains("UserId=user1234"));
        assertTrue(formBody.contains("action=Submit"));
        assertTrue(formBody.contains("_appId=" + packageName));
        assertTrue(formBody.contains("_bvAndroidSdkVersion=" + bvSdkVersion));
        assertTrue(formBody.contains("sendemailalertwhenpublished=true"));
        assertTrue(formBody.contains("_buildNumber=" + versionCode));
        assertTrue(formBody.contains("VideoUrl_1=https%3A%2F%2Fwww.website.com%2Fpath%2Fto%2Fvideo12.mp4"));
        assertTrue(formBody.contains("VideoUrl_2=https%3A%2F%2Fwww.website.com%2Fpath%2Fto%2Fvideo8.mp4"));
        assertTrue(formBody.contains("VideoCaption_1=how+to+farm+taters"));
        assertTrue(formBody.contains("VideoCaption_2=how+to+market+taters"));
    }

    @Test
    public void testQuestionSubmissionBuilder() throws UnsupportedEncodingException {
        String userNickname = "nickname";
        String productId = "123987";
        String fp = "abcdef+123345/ham+taters";
        String userEmail = "foo@bar.com";
        Action action = Action.Submit;

        // Make sure the string is encoded properly
        String expectedResult = "_appVersion=" + versionName + "&passkey=" + bvUserProvidedData.getBvApiKeys().getApiKeyConversations() + "&UserNickname=nickname&apiversion=5.4&fp=abcdef%2B123345%2Fham%2Bbacon%2Beggs%2Bcaseylovestaters&ProductId=123987&UserEmail=foo%40bar.com&action=Submit&_appId=" + packageName + "&_bvAndroidSdkVersion=" + bvSdkVersion + "&_buildNumber=" + versionCode;

        QuestionSubmissionRequest submission = new QuestionSubmissionRequest.Builder(Submit, "123987")
            .fingerPrint("abcdef+123345/ham+bacon+eggs+caseylovestaters")
            .userNickname("nickname")
            .userEmail("foo@bar.com")
            .build();

        String testString = submission.createUrlQueryString(submission.makeQueryParams());
        assertEquals(expectedResult, testString);

    }

    @Test
    public void testQuestionAnswerBuilder() throws Exception {
        String userNickname = "nickname";
        String questionId = "123987";
        String fp = "abcdef+123345/ham+taters";
        String userEmail = "foo@bar.com";
        Action action = Action.Submit;
        String answerText = "Let me google that for you....";

        // Make sure the string is encoded properly
        String expectedResult = "_appVersion=" + versionName + "&passkey=" + bvUserProvidedData.getBvApiKeys().getApiKeyConversations() + "&UserNickname=nickname&QuestionId=123987&action=Submit&apiversion=5.4&_appId=" + packageName + "&fp=abcdef%2B123345%2Fham%2Bbacon%2Beggs%2Bcaseylovestaters&AnswerText=Let+me+google+that+for+you....&UserEmail=foo%40bar.com&_bvAndroidSdkVersion=" + bvSdkVersion + "&_buildNumber=" + versionCode;

        AnswerSubmissionRequest submission = new AnswerSubmissionRequest.Builder(Submit, "123987", "Let me google that for you....")
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
        ConversationsResponse errorResponse = testParsing("conversations_error_no_apikey.json", ReviewResponse.class);

        assertTrue(errorResponse.getHasErrors());
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
            .addIncludeContent(AuthorIncludeType.REVIEWS, 10)
            .addIncludeContent(AuthorIncludeType.QUESTIONS, 11)
            .addIncludeContent(AuthorIncludeType.ANSWERS, 12)
            .addIncludeContent(AuthorIncludeType.COMMENTS, 4)
            .addReviewSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC)
            .addQuestionSort(QuestionOptions.Sort.SubmissionTime, SortOrder.ASC)
            .addAnswerSort(AnswerOptions.Sort.AuthorId, SortOrder.DESC)
            .addIncludeStatistics(AuthorIncludeType.REVIEWS)
            .addIncludeStatistics(AuthorIncludeType.QUESTIONS)
            .addIncludeStatistics(AuthorIncludeType.ANSWERS)
            .addIncludeStatistics(AuthorIncludeType.COMMENTS)
            .build();

        HttpUrl url = request.toHttpUrl();

        assertEquals("Reviews,Questions,Answers,Comments", url.queryParameter("Include"));
        assertEquals(Integer.valueOf(10), Integer.valueOf(url.queryParameter("Limit_Reviews")));
        assertEquals(Integer.valueOf(11), Integer.valueOf(url.queryParameter("Limit_Questions")));
        assertEquals(Integer.valueOf(12), Integer.valueOf(url.queryParameter("Limit_Answers")));
        assertEquals(Integer.valueOf(4), Integer.valueOf(url.queryParameter("Limit_Comments")));
        assertEquals("Reviews,Questions,Answers", url.queryParameter("Stats"));
        assertEquals("SubmissionTime:desc", url.queryParameter("Sort_Reviews"));
        assertEquals("SubmissionTime:asc", url.queryParameter("Sort_Questions"));
        assertEquals("AuthorId:desc", url.queryParameter("Sort_Answers"));
    }
}