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
package com.bazaarvoice.DisplayTests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.bazaarvoice.BaseTest;
import com.bazaarvoice.OnBazaarResponseHelper;

import com.bazaarvoice.DisplayParams;
import com.bazaarvoice.types.Equality;
import com.bazaarvoice.types.IncludeStatsType;
import com.bazaarvoice.types.IncludeType;
import com.bazaarvoice.types.RequestType;

@SuppressLint("NewApi")
public class ProductDisplayTest extends BaseTest {

	private final String tag = getClass().getSimpleName();

	public void testAllProducts() throws Throwable
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
				request.sendDisplayRequest(RequestType.PRODUCTS, null, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testRequestProductById() throws Throwable {
		// --------------------------------------
		// Requesting a product by ID
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("Id", Equality.EQUAL, "test1");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert there is only one result
				assertTrue(results.length() == 1);
				JSONObject questionResult = results.getJSONObject(0);
				// assert the result has the correct ID
				assertEquals(questionResult.getString("Id"), "test1");
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.PRODUCTS, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testRequestProductByIdIncludingStatistics() throws Throwable {
		// --------------------------------------
		// Requesting a Product by ID with Statistics
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("Id", Equality.EQUAL, "test1");
		params.addStats(IncludeStatsType.REVIEWS);

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert there is only one result
				assertTrue(results.length() == 1);
				JSONObject questionResult = results.getJSONObject(0);
				// assert the result has the correct ID
				assertEquals(questionResult.getString("Id"), "test1");

				JSONObject statisticsObject = questionResult.getJSONObject("ReviewStatistics");
				// assert a statistics exist in the includes
				assertTrue(statisticsObject != null);
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.PRODUCTS, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testAllProductsSortByAscendingId() throws Throwable {
		// --------------------------------------
		// Requesting products sorted by ascending ID
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("Id", Equality.EQUAL, "test1");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);

				JSONArray results = response.getJSONArray("Results");

				// assert there are results
				assertTrue(results.length() > 0);

				JSONObject productResult1 = results.getJSONObject(0);
				for (int i = 1; i < results.length(); i++)
				{
					JSONObject productResult2 = results.getJSONObject(i);

					// assert that each answer is sorted by rating in descending
					// order
					assertTrue(productResult1.getString("Id").compareTo(productResult2.getString("Id")) <= 0);
					productResult1 = productResult2;
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.PRODUCTS, params, bazaarResponse);
			}
		});
		bazaarResponse.waitForTestToFinish();
	}

	public void testAllProductsWithCategoryId() throws Throwable {
		// --------------------------------------
		// Request all Products in a given Category
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("CategoryId", Equality.EQUAL, "testCategory1011");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);

				JSONArray results = response.getJSONArray("Results");

				// assert there are results
				assertTrue(results.length() > 0);

				for (int i = 0; i < results.length(); i++)
				{
					JSONObject productResult = results.getJSONObject(i);
					// assert that each result has the correct commentId
					assertEquals(productResult.getString("CategoryId"), "testCategory1011");
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.PRODUCTS, params, bazaarResponse);
			}
		});
		bazaarResponse.waitForTestToFinish();
	}

	public void testAllProductsBySearchText() throws Throwable {
		// --------------------------------------
		// Request all Products with specified text
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.setSearch("Electric Dryer");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);

				JSONArray results = response.getJSONArray("Results");

				// assert there are results
				assertTrue(results.length() > 0);

				for (int i = 0; i < results.length(); i++)
				{
					JSONObject productResult = results.getJSONObject(i);

					// assert that each result has the keywords we're searching
					// for
					assertTrue(productResult.getString("Name").contains("Electric") ||
							productResult.getString("Name").contains("Dryer"));
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.PRODUCTS, params, bazaarResponse);
			}
		});
		bazaarResponse.waitForTestToFinish();
	}

	public void testProductsWithReviewSearch() throws Throwable {
		// --------------------------------------
		// Requesting a single question by Question ID including Answers
		// --------------------------------------
		final DisplayParams params = new DisplayParams();

		params.addInclude(IncludeType.REVIEWS);
		params.addFilter("Id", Equality.EQUAL, "test2");
		params.addSearchOnIncludedType(IncludeType.REVIEWS, "Aenean leo enim");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				// JSONArray results = response.getJSONArray("Results");
				// assert result has the right id
				JSONObject includesObject = response.getJSONObject("Includes");

				JSONObject reviewsIncludesObject = includesObject.getJSONObject("Reviews");
				// assert a product exist in the includes
				assertTrue(reviewsIncludesObject.length() > 0);
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.PRODUCTS, params, bazaarResponse);
			}
		});
		bazaarResponse.waitForTestToFinish();
	}

	public void testRequestQuestionByIdIncludingAnswers() throws Throwable {
		// --------------------------------------
		// Requesting a single question by Question ID including Answers
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("Id", Equality.EQUAL, "14898");

		params.addInclude(IncludeType.ANSWERS);

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert we only have 1 result
				assertTrue(results.length() == 1);
				JSONObject questionResult = results.getJSONObject(0);
				// assert result has the right id
				assertEquals(questionResult.getString("Id"), "14898");
				JSONObject includesObject = response.getJSONObject("Includes");
				JSONObject productIncludesObject = includesObject.getJSONObject("Answers");
				// assert a product exist in the includes
				assertTrue(productIncludesObject != null);
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.QUESTIONS, params, bazaarResponse);
			}
		});
		bazaarResponse.waitForTestToFinish();
	}

	public void testRequestQuestionByIdIncludingAnswersAndSearchAnswers() throws Throwable {
		// --------------------------------------
		// Requesting a single question by Question ID including Answers
		// --------------------------------------
		final DisplayParams params = new DisplayParams();

		params.addInclude(IncludeType.ANSWERS);
		params.addSearchOnIncludedType(IncludeType.ANSWERS, "Cras gravida accumsan eros");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				// JSONArray results = response.getJSONArray("Results");
				// JSONObject questionResult = results.getJSONObject(0);
				// assert result has the right id
				JSONObject includesObject = response.getJSONObject("Includes");

				JSONObject answersIncludesObject = includesObject.getJSONObject("Answers");
				// assert a product exist in the includes
				assertTrue(answersIncludesObject.length() == 1);
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.QUESTIONS, params, bazaarResponse);
			}
		});
		bazaarResponse.waitForTestToFinish();
	}

	public void testRequest25QuestionsThatHaveAnswersDescendingSubmissionTimeSort() throws Throwable {
		// --------------------------------------
		// Requesting 25 questions that have answers, sorted by Submission Time
		// in descending order.
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.setLimit(25);
		params.addFilter("HasAnswers", Equality.EQUAL, "true");
		params.addSort("SubmissionTime", false);

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");

				// assert that we have 25 results
				assertTrue(results.length() == 25);

				// assert that each result has comments
				for (int i = 0; i < results.length(); i++)
				{
					JSONObject questionResult = results.getJSONObject(i);
					assertTrue(questionResult.getLong("TotalAnswerCount") > 0);
				}

				// assert that each answer is sorted by rating in descending
				// order
				JSONObject questionResult1 = results.getJSONObject(0);
				for (int i = 1; i < results.length(); i++)
				{
					JSONObject questionResult2 = results.getJSONObject(i);

					assertTrue(questionResult1.getString("SubmissionTime").compareTo(questionResult2.getString("SubmissionTime")) >= 0);
					questionResult1 = questionResult2;
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.QUESTIONS, params, bazaarResponse);
			}
		});
		bazaarResponse.waitForTestToFinish();
	}

	public void testQuestionsByProductThatHaveAnswersDescendingSubmissionTimeSort() throws Throwable {
		// --------------------------------------
		// Requesting all questions for a particular product sorted by
		// Submission Time.
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("ProductId", Equality.EQUAL, "1000001");
		params.addSort("SubmissionTime", false);

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
					JSONObject questionResult = results.getJSONObject(i);
					assertTrue(questionResult.getString("ProductId").equals("1000001"));
				}

				// assert that each answer is sorted by rating in descending
				// order
				JSONObject questionResult1 = results.getJSONObject(0);
				for (int i = 1; i < results.length(); i++)
				{
					JSONObject questionResult2 = results.getJSONObject(i);

					assertTrue(questionResult1.getString("SubmissionTime").compareTo(questionResult2.getString("SubmissionTime")) >= 0);
					questionResult1 = questionResult2;
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.QUESTIONS, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testRequestQuestionsByCategoryIncludingAnswers() throws Throwable {
		// --------------------------------------
		// Requesting a single question by Question ID including Answers
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("CategoryId", Equality.EQUAL, "1020");

		params.addInclude(IncludeType.ANSWERS);

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert we have results
				assertTrue(results.length() > 0);

				for (int i = 0; i < results.length(); i++)
				{
					JSONObject questionResult = results.getJSONObject(i);
					// assert result has the correct category id
					assertTrue(questionResult.getString("CategoryId").equals("1020"));

					JSONObject includesObject = response.getJSONObject("Includes");
					JSONObject answerIncludesObject = includesObject.getJSONObject("Answers");
					// assert results include answers
					assertTrue(answerIncludesObject != null);

				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.QUESTIONS, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}
}
