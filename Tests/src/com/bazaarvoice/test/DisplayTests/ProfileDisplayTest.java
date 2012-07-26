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
 * Time: 10:50 PM
 */
public class ProfileDisplayTest extends BaseTest {

    private final String tag = getClass().getSimpleName();

    public void testAllProfiles()
    {
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);

                //assert there are results
                assertTrue(response.getJSONArray("Results").length() > 0);
            }
        };

        request.sendDisplayRequest(RequestType.QUESTIONS, null, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }


    public void testRequestRecommendedProfiles() {
         //--------------------------------------
         //Requesting profiles with recommended reviews
         //--------------------------------------
         DisplayParams params = new DisplayParams();
         params.addFilterType(IncludeType.REVIEWS, "IsRecommended", Equality.EQUAL, "true");
         params.addInclude(IncludeType.REVIEWS);

         OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
             @Override
             public void onResponseHelper(JSONObject response)throws JSONException {
                 Log.i(tag, "Response = \n" + response);
                 JSONArray results = response.getJSONArray("Results");
                 //assert there are results
                 assertTrue(results.length() > 0);

                 JSONObject includesObject = response.getJSONObject("Includes");
                 JSONObject reviewIncludesObject = includesObject.getJSONObject("Reviews");
                 JSONArray reviewIds = includesObject.getJSONArray("ReviewsOrder");

                 reviewIds.toString();

                 //assert that each result has comments
                 for(int i = 0; i < reviewIds.length(); i++)
                 {
                     JSONObject reviewObject = reviewIncludesObject.getJSONObject(reviewIds.getString(i));

                     reviewObject.toString();

                     //assert results are recommended
                     assertTrue(reviewObject.getBoolean("IsRecommended"));
                 }
             }
         };

         request.sendDisplayRequest(RequestType.PROFILES, params, bazaarResponse);
         bazaarResponse.waitForTestToFinish();
     }

    public void testRequestProfileById() {
         //--------------------------------------
         //Requesting profile by Id
         //--------------------------------------
         DisplayParams params = new DisplayParams();
         params.addFilter("Id", Equality.EQUAL, "yums");

         OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
             @Override
             public void onResponseHelper(JSONObject response)throws JSONException {
                 Log.i(tag, "Response = \n" + response);
                 JSONArray results = response.getJSONArray("Results");
                 //assert there is one result
                 assertTrue(results.length() == 1);

                 JSONObject profileResult = results.getJSONObject(0);
                 //assert correct id
                 assertTrue(profileResult.getString("Id").equals("yums"));
             }
         };

         request.sendDisplayRequest(RequestType.PROFILES, params, bazaarResponse);
         bazaarResponse.waitForTestToFinish();
     }

    public void testRequestProfileByIdWithQuestionsAndReviewsWithDescendingSubmissionTime() {
        //--------------------------------------
        //Requesting an Author by Id along with Questions and Reviews written by Author, sorted by Submission Time
        //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("Id", Equality.EQUAL, "yums");

        params.addInclude(IncludeType.QUESTIONS);
        params.addInclude(IncludeType.REVIEWS);

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                //assert there is one result
                assertTrue(results.length() == 1);

                JSONObject profileResult = results.getJSONObject(0);
                //assert correct id
                assertTrue(profileResult.getString("Id").equals("yums"));

                JSONObject questionResult1 = results.getJSONObject(0);
                for(int i = 1; i < results.length(); i++)
                {
                    JSONObject questionResult2 = results.getJSONObject(i);

                    //assert that each answer is sorted by rating in descending order
                    assertTrue(questionResult1.getString("SubmissionTime").compareTo(questionResult2.getString("SubmissionTime")) >= 0);
                    questionResult1 = questionResult2;
                }

                JSONObject includesObject = response.getJSONObject("Includes");

                JSONObject reviewIncludesObject = includesObject.getJSONObject("Reviews");
                //assert a review exist in the includes
                assertTrue(reviewIncludesObject != null);

            }
        };

        request.sendDisplayRequest(RequestType.PROFILES, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
     }
}
