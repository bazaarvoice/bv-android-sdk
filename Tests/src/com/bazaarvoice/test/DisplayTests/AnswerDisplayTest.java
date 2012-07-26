package com.bazaarvoice.test.DisplayTests;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;
import com.bazaarvoice.*;
import com.bazaarvoice.test.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: gary
 * Date: 4/26/12
 * Time: 10:25 PM
 */
public class AnswerDisplayTest extends BaseTest {

    private final String tag = getClass().getSimpleName();

    public void testAllAnswers() {
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);
                //assert there are results
                assertTrue(response.getJSONArray("Results").length() > 0);
            }
        };

        request.sendDisplayRequest(RequestType.ANSWERS, null, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testSingleAnswerById() {
        //--------------------------------------
        //Requesting an Answer by Answer ID
        //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("Id", Equality.EQUAL, "16369");

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                //assert there is only one result
                assertTrue(results.length() == 1);
                JSONObject answerResult = results.getJSONObject(0);
                //assert the result has the correct ID
                assertEquals(answerResult.getString("Id"), "16369");
            }
        };

        request.sendDisplayRequest(RequestType.ANSWERS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testSingleAnswerByIdIncludingQuestionInformation() {
        //--------------------------------------
        //Requesting a single review by Review ID including question information
        //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("Id", Equality.EQUAL, "16369");

        params.addInclude(IncludeType.QUESTIONS);

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                //assert we only have 1 result
                assertTrue(results.length() == 1);
                JSONObject answerResult = results.getJSONObject(0);
                //assert result has the right id
                assertEquals(answerResult.getString("Id"), "16369");

                JSONObject includesObject = response.getJSONObject("Includes");
                JSONObject questionIncludesObject = includesObject.getJSONObject("Questions");
                //assert a answer exist in the includes
                assertTrue(questionIncludesObject != null);
            }
        };

        request.sendDisplayRequest(RequestType.ANSWERS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testAllAnswersForACategoryIncludingQuestionInformation() {
        //--------------------------------------
        //Requesting a single review by Review ID including question information
        //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("CategoryId", Equality.EQUAL, "1020");

        params.addInclude(IncludeType.QUESTIONS);

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONObject includesObject = response.getJSONObject("Includes");
                //ensure we have "Questions"
                JSONObject questionsObject = includesObject.getJSONObject("Questions");
                //get the key values from the Questions
                Iterator questionKeys = questionsObject.keys();
                //ensure we have something to iterate over
                assertNotNull(questionKeys);
                assertTrue(questionKeys.hasNext());
            }
        };

        request.sendDisplayRequest(RequestType.ANSWERS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }


    public void testAllAnswersSubmittedByAuthorId() {
         //--------------------------------------
         //Requesting all answers submitted by a particular Author ID
         //--------------------------------------
         DisplayParams params = new DisplayParams();
            params.addFilter("AuthorId", Equality.EQUAL, "5jrptijda8");


         OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
             @Override
             public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");

                //assert we have results
                assertTrue(results.length() > 0);

                //assert that each result has comments
                for(int i = 0; i < results.length(); i++)
                {
                    JSONObject answerResult = results.getJSONObject(i);
                    assertEquals(answerResult.getString("AuthorId"), "5jrptijda8");
                }
             }
         };

         request.sendDisplayRequest(RequestType.ANSWERS, params, bazaarResponse);
         bazaarResponse.waitForTestToFinish();
     }
}
