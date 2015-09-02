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
import com.bazaarvoice.types.IncludeType;
import com.bazaarvoice.types.RequestType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi") public class StoryDisplayTest extends BaseTest {

	private final String tag = getClass().getSimpleName();

	public void testAllStories() throws Throwable {
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
				request.sendDisplayRequest(RequestType.STORIES, null, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testRequestAllStoriesByProductId() throws Throwable {
		// --------------------------------------
		// Requesting all stories for a particular product
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("ProductId", Equality.EQUAL, "1000001");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert there are results
				assertTrue(results.length() > 0);

				// assert that each result has the correct product Id
				for (int i = 0; i < results.length(); i++)
				{
					JSONObject storyResult = results.getJSONObject(i);
					// assert the result has the correct ID
					assertEquals(storyResult.getString("ProductId"), "1000001");
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.STORIES, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testRequestAllStoriesByCategoryId() throws Throwable {
		// --------------------------------------
		// Requesting all stories for a particular category
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("CategoryId", Equality.EQUAL, "1020");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert there are results
				assertTrue(results.length() > 0);

				// assert that each result has the correct product Id
				for (int i = 0; i < results.length(); i++)
				{
					JSONObject storyResult = results.getJSONObject(i);
					// assert the result has the correct ID
					assertEquals(storyResult.getString("CategoryId"), "1020");
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.STORIES, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	// --------------------------------------
	// Requesting a particular story
	// --------------------------------------

	// SKIPPING THIS - example shows this just requesting the same productId as
	// two tests above, so I'm ignoring it

	// TODO this test just doesn't come back with this info (has no photos etc)
	public void testAdvancedStoryRequest() throws Throwable {
		// --------------------------------------
		// Advanced Stories Request
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("HasPhotos", Equality.EQUAL, "true");
		params.addFilter("ProductId", Equality.EQUAL, "1000001");

		params.addInclude(IncludeType.PROFILES);
		params.addInclude(IncludeType.COMMENTS);
		params.addInclude(IncludeType.PRODUCTS);
		params.addInclude(IncludeType.CATEGORIES);

		params.addSort("TotalPositiveFeedbackCount", false);
		params.setLimit(4);
		params.setOffset(4);

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");

				// assert that we 4 results
				assertTrue(results.length() == 4);

				// assert that each result has photos
				for (int i = 0; i < results.length(); i++)
				{
					JSONObject storyResult = results.getJSONObject(i);
					// assert results have the corect product id
					assertEquals(storyResult.getString("ProductId"), "1000001");

					// assert results have photos
					assertTrue((storyResult.getJSONArray("Photos")).length() > 0);

					JSONObject includesObject = response.getJSONObject("Includes");

					JSONObject authorsIncludesObject = includesObject.getJSONObject("Authors");
					// assert an author exist in the includes
					assertTrue(authorsIncludesObject != null);

					// JSONObject commentsIncludesObject =
					// includesObject.getJSONObject("Comments");
					// assert a comments exist in the includes
					// assertTrue(commentsIncludesObject != null);

					JSONObject productsIncludesObject = includesObject.getJSONObject("Products");
					// assert a product exist in the includes
					assertTrue(productsIncludesObject != null);

					JSONObject categoriesIncludesObject = includesObject.getJSONObject("Categories");
					// assert a category exist in the includes
					assertTrue(categoriesIncludesObject != null);
				}

				// for(int i = 1; i < results.length(); i++)
				// {
				// JSONObject storyResult = results.getJSONObject(i);
				// //assert offset of 4
				// assertTrue(storyResult.getLong("Offset") == 4);
				// }

				// assert that each result is sorted by TotalFeedbackCount in
				// descending order
				JSONObject storyResult1 = results.getJSONObject(0);
				for (int i = 1; i < results.length(); i++)
				{
					JSONObject storyResult2 = results.getJSONObject(i);

					assertTrue(storyResult1.getLong("TotalFeedbackCount") <= storyResult2.getLong("TotalFeedbackCount"));
					storyResult1 = storyResult2;
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.STORIES, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

}
