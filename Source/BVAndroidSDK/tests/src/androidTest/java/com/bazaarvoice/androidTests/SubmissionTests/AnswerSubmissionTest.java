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
package com.bazaarvoice.androidTests.SubmissionTests;

import android.annotation.SuppressLint;
import android.util.Log;

import com.bazaarvoice.BazaarEnvironment;
import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.SubmissionParams;
import com.bazaarvoice.androidTests.BaseTest;
import com.bazaarvoice.androidTests.OnBazaarResponseHelper;
import com.bazaarvoice.types.Action;
import com.bazaarvoice.types.ApiVersion;
import com.bazaarvoice.types.RequestType;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi") public class AnswerSubmissionTest extends BaseTest {
	private final String tag = getClass().getSimpleName();

	public void testAnswerSubmit() throws Throwable {

		final String answerText = "This is my answer text.";
		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				assertFalse("" + response, response.getBoolean("HasErrors"));

				JSONObject review = response.getJSONObject("Answer");
				Log.i(tag, "Response = \n" + review);
				// assert there are results

				assertEquals(answerText, review.getString("AnswerText"));
			}
		};

		submit = new BazaarRequest(
                "apitestcustomer",
				"1wtp4lx7aww42x4154oly21ae",
                BazaarEnvironment.staging,
                ApiVersion.FIVE_FOUR);

		final SubmissionParams submissionParams = new SubmissionParams();
		submissionParams.setCategoryId("1000001");
		submissionParams.setQuestionId("6104");
		submissionParams.setAction(Action.PREVIEW);
		submissionParams.setAnswerText(answerText);
		submissionParams.setUserId("gpezz");
		submissionParams.setUserNickname(Long.toString(System.currentTimeMillis()));

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				submit.postSubmission(RequestType.ANSWERS, submissionParams, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

}
