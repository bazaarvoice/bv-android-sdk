package com.bazaarvoice.bvandroidsdk;

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
@Config(manifest = Config.NONE)
public class ConversationsUnitTest extends BVBaseTest{

    @Override
    void modifyPropertiesToInitSDK() {
        conversationsApiBaseUrl = "a different one";
        conversationApiKey = "a differnt key";
    }

    @Test
    public void testQnARequestOverLimitError() {
        QuestionAndAnswerRequest request = new QuestionAndAnswerRequest.Builder("testProductId", 50, 0)
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

        ReviewsRequest request = new ReviewsRequest.Builder("testProductId", 50 , 0)
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

    private List<String> getProdIds(int limit){
        List<String> prodIds = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            prodIds.add("testProductId" + i);
        }

        return prodIds;
    }
}