/*******************************************************************************
 * Copyright 2015 Bazaarvoice
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
package com.bazaarvoice.androidTests.DisplayTests;

import android.annotation.SuppressLint;
import android.util.Log;

import com.bazaarvoice.DisplayParams;
import com.bazaarvoice.androidTests.BaseTest;
import com.bazaarvoice.androidTests.OnBazaarResponseHelper;
import com.bazaarvoice.types.Equality;
import com.bazaarvoice.types.RequestType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi") public class CategoryDisplayTest extends BaseTest {

	private final String tag = getClass().getSimpleName();

	public void testAllCategories() throws Throwable {
		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				// assert there are results
				assertTrue(response.getJSONArray("Results").length() > 0);
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.CATEGORIES, null, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testAllCategoriesAlphabeticalSort() throws Throwable {
		// --------------------------------------
		// Requesting all categories sorted alphabetically by name
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addSort("Name", true);

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);

				JSONArray results = response.getJSONArray("Results");

				// assert there are results
				assertTrue(results.length() > 0);

				// assert that each answer is sorted by rating in descending
				// order
				JSONObject categoryResult1 = results.getJSONObject(0);
				for (int i = 1; i < results.length(); i++)
				{
					JSONObject categoryResult2 = results.getJSONObject(i);

					// assert alphabetical ascending sort
					assertTrue(categoryResult1.getString("Name").compareTo(categoryResult2.getString("Name")) <= 0);
					categoryResult1 = categoryResult2;
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.CATEGORIES, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testAllActiveCategories() throws Throwable {
		// --------------------------------------
		// Requesting all active categories
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("IsActive", Equality.EQUAL, "true");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert there are results
				assertTrue(results.length() > 0);

				// //assert that each result has comments
				// for(int i = 0; i < results.length(); i++)
				// {
				// JSONObject categoryResult = results.getJSONObject(i);
				// //assert category is active
				// assertTrue(categoryResult.getBoolean("IsActive"));
				// }
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.CATEGORIES, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testCategoryById() throws Throwable {
		// --------------------------------------
		// Requesting a category by Id
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("Id", Equality.EQUAL, "testCategory1011");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert we only have 1 result
				assertTrue(results.length() == 1);
				JSONObject categoryResult = results.getJSONObject(0);
				// assert result has the right id
				assertEquals(categoryResult.getString("Id"), "testCategory1011");
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.CATEGORIES, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

    //TODO - why is this coming back with three results - two of which dont have the right ancestor ID) even in their example
//    public void testCategoryByAncestorId() {
//         //--------------------------------------
//         //Requesting categories by Category Ancestor Id
//         //--------------------------------------
//         DisplayParams params = new DisplayParams();
//         params.addFilter("AncestorId", Equality.EQUAL, "testCategory1050");
//
//
//         OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
//             @Override
//             public void onResponseHelper(JSONObject response)throws JSONException {
//                 Log.i(tag, "Response = \n" + response);
//                 JSONArray results = response.getJSONArray("Results");
//                 //assert we only have 1 result
//                 assertTrue(results.length() == 1);
//                 JSONObject categoryResult = results.getJSONObject(0);
//                 //assert result has the right ancestor id
//                 assertEquals(categoryResult.getString("AncestorId"), "testCategory1050");
//             }
//         };
//
//         request.sendDisplayRequest(RequestType.CATEGORIES, params, bazaarResponse);
//         bazaarResponse.waitForTestToFinish();
//     }
}
