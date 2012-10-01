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
public class ProductDisplayTest extends BaseTest {

    private final String tag = getClass().getSimpleName();

    public void testAllProducts()
    {
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);

                //assert there are results
                assertTrue(response.getJSONArray("Results").length() > 0);
            }
        };

        request.sendDisplayRequest(RequestType.PRODUCTS, null, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testRequestProductById() {
         //--------------------------------------
         //Requesting a product by  ID
         //--------------------------------------
         DisplayParams params = new DisplayParams();
         params.addFilter("Id", Equality.EQUAL, "test1");

         OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
             @Override
             public void onResponseHelper(JSONObject response)throws JSONException {
                 Log.i(tag, "Response = \n" + response);
                 JSONArray results = response.getJSONArray("Results");
                 //assert there is only one result
                 assertTrue(results.length() == 1);
                 JSONObject questionResult = results.getJSONObject(0);
                 //assert the result has the correct ID
                 assertEquals(questionResult.getString("Id"), "test1");
             }
         };

         request.sendDisplayRequest(RequestType.PRODUCTS, params, bazaarResponse);
         bazaarResponse.waitForTestToFinish();
     }

    public void testRequestProductByIdIncludingStatistics() {
         //--------------------------------------
         //Requesting a Product by ID with Statistics
         //--------------------------------------
         DisplayParams params = new DisplayParams();
         params.addFilter("Id", Equality.EQUAL, "test1");
         params.addStats("Reviews");

         OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
             @Override
             public void onResponseHelper(JSONObject response)throws JSONException {
                 Log.i(tag, "Response = \n" + response);
                 JSONArray results = response.getJSONArray("Results");
                 //assert there is only one result
                 assertTrue(results.length() == 1);
                 JSONObject questionResult = results.getJSONObject(0);
                 //assert the result has the correct ID
                 assertEquals(questionResult.getString("Id"), "test1");

                 JSONObject statisticsObject = questionResult.getJSONObject("ReviewStatistics");
                 //assert a statistics exist in the includes
                 assertTrue(statisticsObject != null);
             }
         };

         request.sendDisplayRequest(RequestType.PRODUCTS, params, bazaarResponse);
         bazaarResponse.waitForTestToFinish();
     }



    public void testAllProductsSortByAscendingId() {
         //--------------------------------------
         //Requesting products sorted by ascending ID
         //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("Id", Equality.EQUAL, "test1");

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);

                JSONArray results = response.getJSONArray("Results");

                //assert there are results
                assertTrue(results.length() > 0);

                 JSONObject productResult1 = results.getJSONObject(0);
                for(int i = 1; i < results.length(); i++)
                 {
                     JSONObject productResult2 = results.getJSONObject(i);

                     //assert that each answer is sorted by rating in descending order
                     assertTrue(productResult1.getString("Id").compareTo(productResult2.getString("Id")) <= 0);
                     productResult1 = productResult2;
                 }
            }
        };

        request.sendDisplayRequest(RequestType.PRODUCTS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }


    public void testAllProductsWithCategoryId() {
         //--------------------------------------
         //Request all Products in a given Category
         //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("CategoryId", Equality.EQUAL, "testCategory1011");

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);

                JSONArray results = response.getJSONArray("Results");

                //assert there are results
                assertTrue(results.length() > 0);

                 for(int i = 0; i < results.length(); i++)
                 {
                     JSONObject productResult = results.getJSONObject(i);
                     //assert that each result has the correct commentId
                     assertEquals(productResult.getString("CategoryId"), "testCategory1011");
                 }
            }
        };

        request.sendDisplayRequest(RequestType.PRODUCTS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }


    public void testAllProductsBySearchText() {
         //--------------------------------------
         //Request all Products with specified text
         //--------------------------------------
        DisplayParams params = new DisplayParams();

        ArrayList<String> searchTexts = new ArrayList<String>();
        searchTexts.add("Electric");
        searchTexts.add("Dryer");
        params.setSearch(searchTexts);

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);

                JSONArray results = response.getJSONArray("Results");

                //assert there are results
                assertTrue(results.length() > 0);

                 for(int i = 0; i < results.length(); i++)
                 {
                     JSONObject productResult = results.getJSONObject(i);

                      //assert that each result has the keywords we're searching for
                     assertTrue(productResult.getString("Name").contains("Electric") ||
                                productResult.getString("Name").contains("Dryer"));
                 }
            }
        };

        request.sendDisplayRequest(RequestType.PRODUCTS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }
    
    public void testProductsWithReviewSearch() {
        //--------------------------------------
        //Requesting a single question by Question ID including Answers
        //--------------------------------------
        DisplayParams params = new DisplayParams();

        ArrayList<String> includes = new ArrayList<String>();
            includes.add("Reviews");
        params.setIncludes(includes);
        params.addFilter("Id", Equality.EQUAL, "test2");
        params.addSearchType("Reviews", "Aenean leo enim");
        
        
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                //assert result has the right id
                JSONObject includesObject = response.getJSONObject("Includes");

                JSONObject reviewsIncludesObject = includesObject.getJSONObject("Reviews");
                //assert a product exist in the includes
                assertTrue(reviewsIncludesObject.length() > 0);
            }
        };

        request.sendDisplayRequest(RequestType.PRODUCTS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }


    public void testRequestQuestionByIdIncludingAnswers() {
         //--------------------------------------
         //Requesting a single question by Question ID including Answers
         //--------------------------------------
         DisplayParams params = new DisplayParams();
         params.addFilter("Id", Equality.EQUAL, "14898");

         ArrayList<String> includes = new ArrayList<String>();
             includes.add("Answers");
         params.setIncludes(includes);

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
    
    public void testRequestQuestionByIdIncludingAnswersAndSearchAnswers() {
        //--------------------------------------
        //Requesting a single question by Question ID including Answers
        //--------------------------------------
        DisplayParams params = new DisplayParams();

        ArrayList<String> includes = new ArrayList<String>();
            includes.add("Answers");
        params.setIncludes(includes);
        params.addSearchType("Answers", "Cras gravida accumsan eros");
        
        
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                JSONObject questionResult = results.getJSONObject(0);
                //assert result has the right id
                JSONObject includesObject = response.getJSONObject("Includes");

                JSONObject answersIncludesObject = includesObject.getJSONObject("Answers");
                //assert a product exist in the includes
                assertTrue(answersIncludesObject.length() == 1);
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
          params.addFilter("HasAnswers", "true");
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

         ArrayList<String> includes = new ArrayList<String>();
             includes.add("Answers");
         params.setIncludes(includes);

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
