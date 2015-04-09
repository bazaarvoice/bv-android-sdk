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

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.bazaarvoice.BaseTest;
import com.bazaarvoice.OnBazaarResponseHelper;

import com.bazaarvoice.DisplayParams;
import com.bazaarvoice.types.Equality;
import com.bazaarvoice.types.IncludeType;
import com.bazaarvoice.types.RequestType;

@SuppressLint("NewApi")
public class AnswerDisplayTest extends BaseTest {

	private final String tag = getClass().getSimpleName();

	public void testAllAnswers() throws Throwable {
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
				request.sendDisplayRequest(RequestType.ANSWERS, null, bazaarResponse);
			}
		});
		bazaarResponse.waitForTestToFinish();
	}

	public void testSingleAnswerById() throws Throwable {
		// --------------------------------------
		// Requesting an Answer by Answer ID
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("Id", Equality.EQUAL, "16369");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert there is only one result
				assertTrue(results.length() == 1);
				JSONObject answerResult = results.getJSONObject(0);
				// assert the result has the correct ID
				assertEquals(answerResult.getString("Id"), "16369");
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.ANSWERS, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testSingleAnswerByIdIncludingQuestionInformation() throws Throwable {
		// --------------------------------------
		// Requesting a single review by Review ID including question
		// information
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("Id", Equality.EQUAL, "16369");
		params.addInclude(IncludeType.QUESTIONS);

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");
				// assert we only have 1 result
				assertTrue(results.length() == 1);
				JSONObject answerResult = results.getJSONObject(0);
				// assert result has the right id
				assertEquals(answerResult.getString("Id"), "16369");

				JSONObject includesObject = response.getJSONObject("Includes");
				JSONObject questionIncludesObject = includesObject.getJSONObject("Questions");
				// assert a answer exist in the includes
				assertTrue(questionIncludesObject != null);
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.ANSWERS, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testAllAnswersForACategoryIncludingQuestionInformation() throws Throwable {
		// --------------------------------------
		// Requesting a single review by Review ID including question
		// information
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("CategoryId", Equality.EQUAL, "1020");

		ArrayList<String> includes = new ArrayList<String>();
		includes.add("Questions");
		params.addInclude(IncludeType.QUESTIONS);
		params.setExcludeFamily(true);

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONObject includesObject = response.getJSONObject("Includes");
				// ensure we have "Questions"
				JSONObject questionsObject = includesObject.getJSONObject("Questions");
				// get the key values from the Questions
				@SuppressWarnings("rawtypes")
				Iterator questionKeys = questionsObject.keys();
				// ensure we have something to iterate over
				assertNotNull(questionKeys);
				assertTrue(questionKeys.hasNext());
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.ANSWERS, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testAllAnswersSubmittedByAuthorId() throws Throwable {
		// --------------------------------------
		// Requesting all answers submitted by a particular Author ID
		// --------------------------------------
		final DisplayParams params = new DisplayParams();
		params.addFilter("AuthorId", Equality.EQUAL, "5jrptijda8");

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONArray results = response.getJSONArray("Results");

				// assert we have results
				assertTrue(results.length() > 0);

				// assert that each result has comments
				for (int i = 0; i < results.length(); i++)
				{
					JSONObject answerResult = results.getJSONObject(i);
					assertEquals("5jrptijda8", answerResult.getString("AuthorId"));
				}
			}
		};

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				request.sendDisplayRequest(RequestType.ANSWERS, params, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}
}
