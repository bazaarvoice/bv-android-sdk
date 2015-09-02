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

import com.bazaarvoice.SubmissionParams;
import com.bazaarvoice.androidTests.BaseTest;
import com.bazaarvoice.androidTests.OnBazaarResponseHelper;
import com.bazaarvoice.types.FeedbackContentType;
import com.bazaarvoice.types.FeedbackType;
import com.bazaarvoice.types.FeedbackVoteType;
import com.bazaarvoice.types.RequestType;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class FeedbackSubmissionTest extends BaseTest {
	private final String tag = getClass().getSimpleName();
	private final String reasonText = "This post was not nice.";

	public void testFeedbackSubmit() throws Throwable {

		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.e(tag, "End of Feedback Submission submit transmission : END " + System.currentTimeMillis());
				Log.i(tag, "FeedbackResponse = \n" + response);
				JSONObject feedback = response.getJSONObject("Feedback");

				// assert there are results
				assertEquals(reasonText, feedback.getJSONObject("Inappropriate").getString("ReasonText"));
			}
		};

		final SubmissionParams submissionParams = new SubmissionParams();
		submissionParams.setContentType(FeedbackContentType.REVIEW);
		submissionParams.setContentId("83964");
		submissionParams.setUserId("123abc");
		submissionParams.setFeedbackType(FeedbackType.INAPPROPRIATE);
		submissionParams.setReasonText("This post was not nice.");

		Log.e(tag, "Begin of Feedback Submission submit transmission : BEGIN " + System.currentTimeMillis());

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				submit.postSubmission(RequestType.FEEDBACK, submissionParams, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

	public void testFeedbackSubmit2() throws Throwable {
		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.e(tag, "End of Feedback Submission submit transmission : END " + System.currentTimeMillis());
				Log.i(tag, "FeedbackResponse = \n" + response);
				boolean errors = response.getBoolean("HasErrors");
				assertEquals(errors, false);
			}
		};

		final SubmissionParams submissionParams = new SubmissionParams();
		submissionParams.setContentType(FeedbackContentType.REVIEW);
		submissionParams.setContentId("83964");
		submissionParams.setUserId("123abc");
		submissionParams.setFeedbackType(FeedbackType.HELPFULNESS);
		submissionParams.setVote(FeedbackVoteType.NEGATIVE);

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				submit.postSubmission(RequestType.FEEDBACK, submissionParams, bazaarResponse);
			}
		});
		Log.e(tag, "Begin of Feedback Submission submit transmission : BEGIN " + System.currentTimeMillis());
		bazaarResponse.waitForTestToFinish();
	}

}
