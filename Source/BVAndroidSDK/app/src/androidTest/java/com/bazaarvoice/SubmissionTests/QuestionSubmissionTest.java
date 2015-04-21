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
package com.bazaarvoice.SubmissionTests;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.bazaarvoice.BaseTest;
import com.bazaarvoice.BazaarEnvironment;
import com.bazaarvoice.OnBazaarResponseHelper;

import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.SubmissionParams;
import com.bazaarvoice.types.Action;
import com.bazaarvoice.types.ApiVersion;
import com.bazaarvoice.types.RequestType;

@SuppressLint("NewApi") public class QuestionSubmissionTest extends BaseTest {
	private final String tag = getClass().getSimpleName();

	public void testQuestionSubmit() throws Throwable {

		final String questionText = "This is my question text.uaskhakdhakshdakhsdkahsdkajhsdkahskdhakdjhakjshdakjhdakjhdakjsdhkajsdajkhdkajhsdkajhsdkajhsdkjahskdjhakdjhaksjh";
		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				assertFalse("" + response, response.getBoolean("HasErrors"));

				JSONObject review = response.getJSONObject("Question");
				Log.i(tag, "Response = \n" + review);
				// assert there are results

				assertEquals(questionText, review.getString("QuestionSummary"));
			}
		};

		submit = new BazaarRequest(
                "apitestcustomer",
				"1wtp4lx7aww42x4154oly21ae",
                BazaarEnvironment.staging,
                ApiVersion.FIVE_FOUR);

		final SubmissionParams submissionParams = new SubmissionParams();
		// //Log.e(TAG,submissionParams.getEncryptedUser());
		submissionParams.setProductId("1000001");
		submissionParams.setAction(Action.PREVIEW);
		submissionParams.setQuestionSummary(questionText);
		submissionParams.setUserId("cwod");
		submissionParams.setUserNickname("randomnickname");

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				submit.postSubmission(RequestType.QUESTIONS, submissionParams, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

}
