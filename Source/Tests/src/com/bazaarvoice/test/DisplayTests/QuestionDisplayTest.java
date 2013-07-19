/*******************************************************************************
 * Copyright 2013 Bazaarvoice
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bazaarvoice.test.DisplayTests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bazaarvoice.DisplayParams;
import com.bazaarvoice.types.*;
import com.bazaarvoice.test.BaseTest;
import com.bazaarvoice.test.OnBazaarResponseHelper;

public class QuestionDisplayTest extends BaseTest {

    private final String tag = getClass().getSimpleName();

    public void testAllQuestions()
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

    public void testRequestQuestionById() {
         //--------------------------------------
         //Requesting a Question by Question ID
         //--------------------------------------
         DisplayParams params = new DisplayParams();
         params.addFilter("Id", Equality.EQUAL, "14902");

         OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
             @Override
             public void onResponseHelper(JSONObject response)throws JSONException {
                 Log.i(tag, "Response = \n" + response);
                 JSONArray results = response.getJSONArray("Results");
                 //assert there is only one result
                 assertTrue(results.length() == 1);
                 JSONObject questionResult = results.getJSONObject(0);
                 //assert the result has the correct ID
                 assertEquals(questionResult.getString("Id"), "14902");
             }
         };

         request.sendDisplayRequest(RequestType.QUESTIONS, params, bazaarResponse);
         bazaarResponse.waitForTestToFinish();
     }

    public void testRequestQuestionByIdIncludingAnswers() {
         //--------------------------------------
         //Requesting a single question by Question ID including Answers
         //--------------------------------------
         DisplayParams params = new DisplayParams();
         params.addFilter("Id", Equality.EQUAL, "14898");

         params.addInclude(IncludeType.ANSWERS);

         OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
             @Override
             public void onResponseHelper(JSONObject response)throws JSONException {
                 Log.i(tag, "Response = \n" + response);
                 JSONArray results = response.getJSONArray("Results");
                 //assert we only have 1 result
                 assertTrue(results.length() == 1);
                 JSONObject questionResult = results.getJSONObject(0);
                 //assert result has the right id
                 assertEquals(questionResult.getString("Id"), "14898");
                 JSONObject includesObject = response.getJSONObject("Includes");
                 JSONObject productIncludesObject = includesObject.getJSONObject("Answers");
                 //assert a product exist in the includes
                 assertTrue(productIncludesObject != null);
             }
         };

         request.sendDisplayRequest(RequestType.QUESTIONS, params, bazaarResponse);
         bazaarResponse.waitForTestToFinish();
     }

    public void testRequest25QuestionsThatHaveAnswersDescendingSubmissionTimeSort() {
          //--------------------------------------
          //Requesting 25 questions that have answers, sorted by Submission Time in descending order.
          //--------------------------------------
          DisplayParams params = new DisplayParams();
          params.setLimit(25);
          params.addFilter("HasAnswers", Equality.EQUAL, "true");
          params.addSort("SubmissionTime", false);

          OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
              @Override
              public void onResponseHelper(JSONObject response)throws JSONException {
                 Log.i(tag, "Response = \n" + response);
                 JSONArray results = response.getJSONArray("Results");

                 //assert that we have 25 results
                 assertTrue(results.length() == 25);

                 //assert that each result has comments
                 for(int i = 0; i < results.length(); i++)
                 {
                     JSONObject questionResult = results.getJSONObject(i);
                     assertTrue(questionResult.getLong("TotalAnswerCount") > 0);
                 }

                 //assert that each answer is sorted by rating in descending order
                 JSONObject questionResult1 = results.getJSONObject(0);
                 for(int i = 1; i < results.length(); i++)
                 {
                     JSONObject questionResult2 = results.getJSONObject(i);

                     assertTrue(questionResult1.getString("SubmissionTime").compareTo(questionResult2.getString("SubmissionTime")) >= 0);
                     questionResult1 = questionResult2;
                 }
              }
          };

          request.sendDisplayRequest(RequestType.QUESTIONS, params, bazaarResponse);
          bazaarResponse.waitForTestToFinish();
      }

    public void testQuestionsByProductThatHaveAnswersDescendingSubmissionTimeSort() {
          //--------------------------------------
          //Requesting all questions for a particular product sorted by Submission Time.
          //--------------------------------------
          DisplayParams params = new DisplayParams();
          params.addFilter("ProductId", Equality.EQUAL, "1000001");
          params.addSort("SubmissionTime", false);

          OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
              @Override
              public void onResponseHelper(JSONObject response)throws JSONException {
                 Log.i(tag, "Response = \n" + response);
                 JSONArray results = response.getJSONArray("Results");
                 //assert there are results
                 assertTrue(results.length() > 0);

                 //assert that each result has the correct product Id
                 for(int i = 0; i < results.length(); i++)
                 {
                     JSONObject questionResult = results.getJSONObject(i);
                     assertTrue(questionResult.getString("ProductId").equals("1000001"));
                 }

                 //assert that each answer is sorted by rating in descending order
                 JSONObject questionResult1 = results.getJSONObject(0);
                 for(int i = 1; i < results.length(); i++)
                 {
                     JSONObject questionResult2 = results.getJSONObject(i);

                     assertTrue(questionResult1.getString("SubmissionTime").compareTo(questionResult2.getString("SubmissionTime")) >= 0);
                     questionResult1 = questionResult2;
                 }
              }
          };

          request.sendDisplayRequest(RequestType.QUESTIONS, params, bazaarResponse);
          bazaarResponse.waitForTestToFinish();
      }

    public void testRequestQuestionsByCategoryIncludingAnswers() {
         //--------------------------------------
         //Requesting a single question by Question ID including Answers
         //--------------------------------------
         DisplayParams params = new DisplayParams();
         params.addFilter("CategoryId", Equality.EQUAL, "1020");

         params.addInclude(IncludeType.ANSWERS);

         OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
             @Override
             public void onResponseHelper(JSONObject response)throws JSONException {
                 Log.i(tag, "Response = \n" + response);
                 JSONArray results = response.getJSONArray("Results");
                 //assert we  have results
                 assertTrue(results.length() > 0);



                 for(int i = 0; i < results.length(); i++)
                 {
                     JSONObject questionResult = results.getJSONObject(i);
                     //assert result has the correct category id
                     assertTrue(questionResult.getString("CategoryId").equals("1020"));

                     JSONObject includesObject = response.getJSONObject("Includes");
                     JSONObject answerIncludesObject = includesObject.getJSONObject("Answers");
                     //assert results include answers
                     assertTrue(answerIncludesObject != null);

                 }
             }
         };

         request.sendDisplayRequest(RequestType.QUESTIONS, params, bazaarResponse);
         bazaarResponse.waitForTestToFinish();
     }
}
