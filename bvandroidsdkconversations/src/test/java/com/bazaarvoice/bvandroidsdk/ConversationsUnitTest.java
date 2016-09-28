package com.bazaarvoice.bvandroidsdk;

import com.bazaarvoice.bvandroidsdk_common.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */

@RunWith(RobolectricTestRunner.class)
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

    private void testParsing(String filename, Class responseClass) {
        String reviewsForProdResponse = jsonFileAsString(filename);
        gson.fromJson(reviewsForProdResponse, responseClass);
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
        testParsing("reviews_some_reviews.json", ReviewResponse.class);
    }

    @Test
    public void testQandAForSomeQuestionsWithAnswersParsing() {
        testParsing("qanda_some_questions_with_answers.json", QuestionAndAnswerResponse.class);
    }

    private List<String> getProdIds(int limit){
        List<String> prodIds = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            prodIds.add("testProductId" + i);
        }

        return prodIds;
    }
}