package com.bazaarvoice.test.DisplayTests;

import com.bazaarvoice.*;
import com.bazaarvoice.test.*;

import java.util.ArrayList;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: gary
 * Date: 4/26/12
 * Time: 10:25 PM
 */
public class CommentDisplayTest extends BaseTest {

    private final String tag = getClass().getSimpleName();

    public void testAllReviewComments() {
    //--------------------------------------
    //Requesting all review comments
    //--------------------------------------
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);
                //assert there are results
                assertTrue(response.getJSONArray("Results").length() > 0);
            }
        };

        request.sendDisplayRequest(RequestType.REVIEW_COMMENTS, null, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testAllStoryComments() {
    //--------------------------------------
    //Requesting all story comments
    //--------------------------------------
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);
                //assert there are results
                assertTrue(response.getJSONArray("Results").length() > 0);
            }
        };

        request.sendDisplayRequest(RequestType.STORY_COMMENTS, null, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testAllStoryCommentsForStoryId() {
    //--------------------------------------
    //Requesting all comments for a particular story
    //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("StoryId", Equality.EQUAL, "1593");

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);

                JSONArray results = response.getJSONArray("Results");
                //assert there are results
                assertTrue(results.length() > 0);

                //assert that each result has the correct story Id
                for(int i = 0; i < results.length(); i++)
                {
                    JSONObject commentResult = results.getJSONObject(i);
                    //assert the result has the correct ID
                    assertEquals(commentResult.getString("StoryId"), "1593");
                }
            }
        };

        request.sendDisplayRequest(RequestType.STORY_COMMENTS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }


    public void testAllReviewCommentsByReviewId() {
    //--------------------------------------
    //Requesting all comments for a particular review
    //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("ReviewId", Equality.EQUAL, "192548");

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                //assert there are results
                assertTrue(results.length() > 0);

                //assert that each result has the correct review Id
                for(int i = 0; i < results.length(); i++)
                {
                    JSONObject commentResult = results.getJSONObject(i);
                    //assert the result has the correct ID
                    assertEquals(commentResult.getString("ReviewId"), "192548");
                }

            }
        };

        request.sendDisplayRequest(RequestType.REVIEW_COMMENTS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testRequestStoryCommentByReviewId() {
    //--------------------------------------
    //Requesting a particular story comment
    //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("ReviewId", Equality.EQUAL, "192548");

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                for (int i = 0; i < results.length(); ++i)
                {
                    JSONObject commentResult = results.getJSONObject(i);
                    //assert the result has the correct ID
                    assertEquals(commentResult.getString("ReviewId"), "192548");
                }
            }
        };

        request.sendDisplayRequest(RequestType.STORY_COMMENTS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }


    public void testRequestReviewCommentByReviewId() {
        //--------------------------------------
        //Requesting a particular review comment
        //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("ReviewId", Equality.EQUAL, "192548");

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                for (int i = 0; i < results.length(); ++i)
                {
                    JSONObject commentResult = results.getJSONObject(i);
                    //assert the result has the correct ID
                    assertEquals(commentResult.getString("ReviewId"), "192548");
                }
            }
        };

        request.sendDisplayRequest(RequestType.REVIEW_COMMENTS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }



    public void testAdvancedCommentRequest() {
        //--------------------------------------
        //Advanced Comments Request
        //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("HasPhotos", "true");
        params.addFilter("ProductId", Equality.EQUAL, "1000001");

        params.addInclude(IncludeType.AUTHORS);
        params.addInclude(IncludeType.STORIES);
        params.addInclude(IncludeType.PRODUCTS);
        params.addInclude(IncludeType.CATEGORIES);

        params.addSort("TotalPositiveFeedbackCount", false);
        params.setLimit(4);

        params.setOffset(4);

         OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
             @Override
             public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONObject includesObject = response.getJSONObject("Includes");
                 JSONObject productsIncludesObject = includesObject.getJSONObject("Products");
                 //assert a product exist in the includes
                 assertTrue(productsIncludesObject != null);
                //assert product has the correct id
                assertTrue(productsIncludesObject.has("1000001"));

                JSONObject storiesIncludesObject = includesObject.getJSONObject("Stories");
                 //assert a stories exist in the includes
                 assertTrue(storiesIncludesObject != null);

                 JSONObject authorsIncludesObject = includesObject.getJSONObject("Authors");
                 //assert an author exist in the includes
                 assertTrue(authorsIncludesObject != null);

                JSONObject categoriesIncludesObject = includesObject.getJSONObject("Categories");
                //assert a category exist in the includes
                assertTrue(categoriesIncludesObject != null);

                JSONArray results = response.getJSONArray("Results");



                     //assert offset of 4
                     assertTrue(response.getLong("Offset") == 4);

                JSONObject storyResult1 = results.getJSONObject(0);

                for(int i = 1; i < results.length(); i++)
                {
                    JSONObject storyResult2 = results.getJSONObject(i);

                    assertTrue(storyResult1.getLong("TotalFeedbackCount") <= storyResult2.getLong("TotalFeedbackCount"));
                    storyResult1 = storyResult2;
                }
             }
         };

         request.sendDisplayRequest(RequestType.STORY_COMMENTS, params, bazaarResponse);
         bazaarResponse.waitForTestToFinish();
     }

}
