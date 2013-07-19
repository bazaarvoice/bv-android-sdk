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

import com.bazaarvoice.*;
import com.bazaarvoice.types.*;
import com.bazaarvoice.test.*;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StatisticsDisplayTest extends BaseTest {

    private final String tag = getClass().getSimpleName();






    public void testNativeReviewStatisticsForOneProduct() {
        //--------------------------------------
        //Requesting native review statistics for one product
        //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("ProductId", Equality.EQUAL, "test1");
        params.addStats(IncludeStatsType.NATIVE_REVIEWS);


        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                //assert there is only one result
                assertTrue(results.length() == 1);
                JSONObject statisticsResult = results.getJSONObject(0);


                JSONObject productStatistics = statisticsResult.getJSONObject("ProductStatistics");
                //assert the result has the correct ID
                assertEquals(productStatistics.getString("ProductId"), "test1");
                //assert that we have the correct statistics
                assertTrue(productStatistics.getJSONObject("NativeReviewStatistics") != null);
            }
        };

        request.sendDisplayRequest(RequestType.STATISTICS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testReviewStatisticsForOneProduct() {
        //--------------------------------------
        //Requesting  review statistics for one product
        //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("ProductId", Equality.EQUAL, "test1");
        params.addStats(IncludeStatsType.REVIEWS);


        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                //assert there is only one result
                assertTrue(results.length() == 1);
                JSONObject statisticsResult = results.getJSONObject(0);


                JSONObject productStatistics = statisticsResult.getJSONObject("ProductStatistics");
                //assert the result has the correct ID
                assertEquals(productStatistics.getString("ProductId"), "test1");
                //assert that we have the correct statistics
                assertTrue(productStatistics.getJSONObject("ReviewStatistics") != null);
            }
        };

        request.sendDisplayRequest(RequestType.STATISTICS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testNativeAndReviewStatisticsForOneProduct() {
        //--------------------------------------
        //Requesting native review and review statistics for one product
        //--------------------------------------
        DisplayParams params = new DisplayParams();
        params.addFilter("ProductId", Equality.EQUAL, "test1");
        params.addStats(IncludeStatsType.NATIVE_REVIEWS);
        params.addStats(IncludeStatsType.REVIEWS);


        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                //assert there is only one result
                assertTrue(results.length() == 1);
                JSONObject statisticsResult = results.getJSONObject(0);


                JSONObject productStatistics = statisticsResult.getJSONObject("ProductStatistics");
                //assert the result has the correct ID
                assertEquals(productStatistics.getString("ProductId"), "test1");
                //assert that we have the correct statistics
                assertTrue(productStatistics.getJSONObject("ReviewStatistics") != null);
                assertTrue(productStatistics.getJSONObject("NativeReviewStatistics") != null);

            }
        };

        request.sendDisplayRequest(RequestType.STATISTICS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testNativeReviewStatisticsForMultipleProduct() {
        //--------------------------------------
        //Requesting native review statistics for multiple products
        //--------------------------------------
        DisplayParams params = new DisplayParams();

        String[] idArray = new String[] { "test1", "test2", "test3", "test4"};
        params.addFilter("ProductId", Equality.EQUAL, idArray);

        params.addStats(IncludeStatsType.NATIVE_REVIEWS);

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response)throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONArray results = response.getJSONArray("Results");
                //assert there are results
                assertTrue(results.length() > 0);

                //assert that each result has comments
                for(int i = 0; i < results.length(); i++)
                {
                    JSONObject statisticsResult = results.getJSONObject(i);

                    JSONObject productStatistics = statisticsResult.getJSONObject("ProductStatistics");


                    //assert the result has the correct ID
                    assertTrue(productStatistics.getString("ProductId").equals("test1") ||
                               productStatistics.getString("ProductId").equals("test2") ||
                               productStatistics.getString("ProductId").equals("test3") ||
                               productStatistics.getString("ProductId").equals("test4"));
                    //assert that we have the correct statistics
                    assertTrue(productStatistics.getJSONObject("NativeReviewStatistics") != null);
                }
            }
        };

        request.sendDisplayRequest(RequestType.STATISTICS, params, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }
}
