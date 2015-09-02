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

@SuppressLint("NewApi")
public class CommentDisplayTest extends BaseTest {

	private final String tag = getClass().getSimpleName();

	public void testAllReviewComments() throws Throwable {
		// --------------------------------------
		// Requesting all review comments
		// --------------------------------------
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
				request.sendDisplayRequest(RequestType.REVIEW_COMMENTS, null, bazaarResponse);

			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testAllStoryComments() throws Throwable {
		// --------------------------------------
		// Requesting all story comments
		// --------------------------------------
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
				request.sendDisplayRequest(RequestType.STORY_COMMENTS, null, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testAllStoryCommentsForStoryId() throws Throwable {
		// --------------------------------------
		// Requesting all comments for a particular story
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("StoryId", Equality.EQUAL, "1593");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);

				JSONArray results = response.getJSONArray("Results");
				// assert there are results
				assertTrue(results.length() > 0);

				// assert that each result has the correct story Id
				for (int i = 0; i < results.length(); i++)
				{
					JSONObject commentResult = results.getJSONObject(i);
					// assert the result has the correct ID
					assertEquals(commentResult.getString("StoryId"), "1593");
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.STORY_COMMENTS, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testAllReviewCommentsByReviewId() throws Throwable {
		// --------------------------------------
		// Requesting all comments for a particular review
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("ReviewId", Equality.EQUAL, "192548");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert there are results
				assertTrue(results.length() > 0);

				// assert that each result has the correct review Id
				for (int i = 0; i < results.length(); i++)
				{
					JSONObject commentResult = results.getJSONObject(i);
					// assert the result has the correct ID
					assertEquals(commentResult.getString("ReviewId"), "192548");
				}

			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.REVIEW_COMMENTS, params, bazaarResponse);
			}
		});
		
		bazaarResponse.waitForTestToFinish();
	}

	public void testRequestStoryCommentByReviewId() throws Throwable {
		// --------------------------------------
		// Requesting a particular story comment
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("ReviewId", Equality.EQUAL, "192548");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				for (int i = 0; i < results.length(); ++i)
				{
					JSONObject commentResult = results.getJSONObject(i);
					// assert the result has the correct ID
					assertEquals(commentResult.getString("ReviewId"), "192548");
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.STORY_COMMENTS, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testRequestReviewCommentByReviewId() throws Throwable {
		// --------------------------------------
		// Requesting a particular review comment
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("ReviewId", Equality.EQUAL, "192548");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				for (int i = 0; i < results.length(); ++i)
				{
					JSONObject commentResult = results.getJSONObject(i);
					// assert the result has the correct ID
					assertEquals(commentResult.getString("ReviewId"), "192548");
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.REVIEW_COMMENTS, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testAdvancedCommentRequest() throws Throwable {
		// --------------------------------------
		// Advanced Comments Request
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("HasPhotos", Equality.EQUAL, "true");
		params.addFilter("ProductId", Equality.EQUAL, "1000001");

		params.addInclude(IncludeType.PROFILES);
		params.addInclude(IncludeType.STORIES);
		params.addInclude(IncludeType.PRODUCTS);
		params.addInclude(IncludeType.CATEGORIES);

		params.addSort("TotalPositiveFeedbackCount", false);
		params.setLimit(4);

		params.setOffset(4);

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONObject includesObject = response.getJSONObject("Includes");
				JSONObject productsIncludesObject = includesObject.getJSONObject("Products");
				// assert a product exist in the includes
				assertTrue(productsIncludesObject != null);
				// assert product has the correct id
				assertTrue(productsIncludesObject.has("1000001"));

				JSONObject storiesIncludesObject = includesObject.getJSONObject("Stories");
				// assert a stories exist in the includes
				assertTrue(storiesIncludesObject != null);

				JSONObject authorsIncludesObject = includesObject.getJSONObject("Authors");
				// assert an author exist in the includes
				assertTrue(authorsIncludesObject != null);

				JSONObject categoriesIncludesObject = includesObject.getJSONObject("Categories");
				// assert a category exist in the includes
				assertTrue(categoriesIncludesObject != null);

				JSONArray results = response.getJSONArray("Results");

				// assert offset of 4
				assertTrue(response.getLong("Offset") == 4);

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
				request.sendDisplayRequest(RequestType.STORY_COMMENTS, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

}
