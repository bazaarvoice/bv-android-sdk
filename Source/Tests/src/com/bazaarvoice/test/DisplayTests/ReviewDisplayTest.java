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

public class ReviewDisplayTest extends BaseTest {

    private final String tag = getClass().getSimpleName();

    public void testAllReviews() {
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);
                //assert there are results
                assertTrue(response.getJSONArray("Results").length() > 0);
            }
        };

        request.sendDisplayRequest(RequestType.REVIEWS, null, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testSingleReviewById() {
        //--------------------------------------
        //Requesting a single review by Review ID
        //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("Id", Equality.EQUAL, "192612");

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                //assert there is only one result
                assertTrue(results.length() == 1);
                JSONObject reviewResult = results.getJSONObject(0);
                //assert the result has the correct ID
                assertEquals(reviewResult.getString("Id"), "192612");
            }
        };

        request.sendDisplayRequest(RequestType.REVIEWS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testSingleReviewByIdIncludingProductInformation() {
        //--------------------------------------
        //Requesting a single review by Review ID including product informdation
        //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("Id", Equality.EQUAL, "192612");

        params.addInclude(IncludeType.PRODUCTS);

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                //assert we only have 1 result
                assertTrue(results.length() == 1);
                JSONObject reviewResult = results.getJSONObject(0);
                //assert result has the right id
                assertEquals(reviewResult.getString("Id"), "192612");
                JSONObject includesObject = response.getJSONObject("Includes");
                JSONObject productIncludesObject = includesObject.getJSONObject("Products");
                //assert a product exist in the includes
                assertTrue(productIncludesObject != null);
            }
        };

        request.sendDisplayRequest(RequestType.REVIEWS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testRequest10ReviewsThatHaveCommentsAscendingRatingsSort() {
         //--------------------------------------
         //Requesting 10 reviews, sorted by rating in ascending order
         //--------------------------------------
         DisplayParams params = new DisplayParams();
         params.setLimit(10);
         params.addFilter("HasComments", Equality.EQUAL, "true");
         params.addSort("Rating", false);

         OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
             @Override
             public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");

                //assert that we have ten results
                assertTrue(results.length() == 10);

                //assert that each result has comments
                for(int i = 0; i < results.length(); i++)
                {
                    JSONObject reviewResult = results.getJSONObject(i);
                    assertTrue(reviewResult.getLong("TotalCommentCount") > 0);
                }

                //assert that each result is sorted by rating in ascending order
                JSONObject reviewResult1 = results.getJSONObject(0);
                for(int i = 1; i < results.length(); i++)
                {
                    JSONObject reviewResult2 = results.getJSONObject(i);

                    assertTrue(reviewResult1.getLong("Rating") >= reviewResult2.getLong("Rating"));
                    reviewResult1 = reviewResult2;
                }
             }
         };

         request.sendDisplayRequest(RequestType.REVIEWS, params, bazaarResponse);
         bazaarResponse.waitForTestToFinish();
     }

    public void testRequestAllReviewsForAProduct() {
         //--------------------------------------
         //Requesting all reviews for a particular product
         //--------------------------------------
        DisplayParams params = new DisplayParams();

        params.addFilter("ProductId", Equality.EQUAL, "1000001");

         OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
             @Override
             public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");

                //assert that each result has the correct product ID
                for(int i = 0; i < results.length(); i++)
                {
                    JSONObject reviewResult = results.getJSONObject(i);
                    assertTrue(reviewResult.getString("ProductId").equals("1000001"));
                }
             }
         };

         request.sendDisplayRequest(RequestType.REVIEWS, params, bazaarResponse);
         bazaarResponse.waitForTestToFinish();
     }
}
