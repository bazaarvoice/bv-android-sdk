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

@SuppressLint("NewApi") public class ProfileDisplayTest extends BaseTest {

	private final String tag = getClass().getSimpleName();

	public void testAllProfiles() throws Throwable
	{
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
				request.sendDisplayRequest(RequestType.QUESTIONS, null, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testRequestRecommendedProfiles() throws Throwable {
		// --------------------------------------
		// Requesting profiles with recommended reviews
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilterOnIncludedType(IncludeType.REVIEWS, "IsRecommended", Equality.EQUAL, "true");
		params.addInclude(IncludeType.REVIEWS);

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert there are results
				assertTrue(results.length() > 0);

				JSONObject includesObject = response.getJSONObject("Includes");
				JSONObject reviewIncludesObject = includesObject.getJSONObject("Reviews");
				JSONArray reviewIds = includesObject.getJSONArray("ReviewsOrder");

				reviewIds.toString();

				// assert that each result has comments
				for (int i = 0; i < reviewIds.length(); i++)
				{
					JSONObject reviewObject = reviewIncludesObject.getJSONObject(reviewIds.getString(i));

					reviewObject.toString();

					// assert results are recommended
					assertTrue(reviewObject.getBoolean("IsRecommended"));
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.PROFILES, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testRequestProfileById() throws Throwable {
		// --------------------------------------
		// Requesting profile by Id
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("Id", Equality.EQUAL, "yums");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert there is one result
				assertTrue(results.length() == 1);

				JSONObject profileResult = results.getJSONObject(0);
				// assert correct id
				assertTrue(profileResult.getString("Id").equals("yums"));
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.PROFILES, params, bazaarResponse);
			}
		});
		bazaarResponse.waitForTestToFinish();
	}

	public void testRequestProfileByIdWithQuestionsAndReviewsWithDescendingSubmissionTime() throws Throwable {
		// --------------------------------------
		// Requesting an Author by Id along with Questions and Reviews written
		// by Author, sorted by Submission Time
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("Id", Equality.EQUAL, "yums");

		params.addInclude(IncludeType.QUESTIONS);
		params.addInclude(IncludeType.REVIEWS);

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert there is one result
				assertTrue(results.length() == 1);

				JSONObject profileResult = results.getJSONObject(0);
				// assert correct id
				assertTrue(profileResult.getString("Id").equals("yums"));

				JSONObject questionResult1 = results.getJSONObject(0);
				for (int i = 1; i < results.length(); i++)
				{
					JSONObject questionResult2 = results.getJSONObject(i);

					// assert that each answer is sorted by rating in descending
					// order
					assertTrue(questionResult1.getString("SubmissionTime").compareTo(questionResult2.getString("SubmissionTime")) >= 0);
					questionResult1 = questionResult2;
				}

				JSONObject includesObject = response.getJSONObject("Includes");

				JSONObject reviewIncludesObject = includesObject.getJSONObject("Reviews");
				// assert a review exist in the includes
				assertTrue(reviewIncludesObject != null);

			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.PROFILES, params, bazaarResponse);
			}
		});
		bazaarResponse.waitForTestToFinish();
	}
}
