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

@SuppressLint("NewApi")
public class CommentsSubmissionTest extends BaseTest {
	private final String tag = getClass().getSimpleName();

	public void testReviewCommentSubmit() throws Throwable {

		final String titleText = "This is my title text for review comment";
		final String commentText = "This is my comment text for review comment ggggggggggggggggggggggggggggggggggggggg";
		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONObject comment = response.getJSONObject("Comment");
				// assert there are results

				assertEquals(commentText, comment.getString("CommentText"));
				assertEquals(titleText, comment.getString("Title"));
			}
		};

		final SubmissionParams submissionParams = new SubmissionParams();
		// //Log.e(TAG,submissionParams.getEncryptedUser());
		submissionParams.setProductId("1001");
		submissionParams.setReviewId("83964");
		submissionParams.setAction(Action.PREVIEW);
		submissionParams.setCommentText(commentText);
		submissionParams.setTitle(titleText);
		submissionParams.setUserId("cwod");

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				submit.postSubmission(RequestType.REVIEW_COMMENTS, submissionParams, bazaarResponse);
			}
		});
		bazaarResponse.waitForTestToFinish();
	}

	public void testStoryCommentSubmit() throws Throwable {

		final String titleText = "This is my title text for story comment";
		final String commentText = "This is my comment text for story comment hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";
		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {

				JSONObject comment = response.getJSONObject("Comment");
				Log.i(tag, "Response = \n" + comment);
				// assert there are results

				assertEquals(commentText, comment.getString("CommentText"));
				assertEquals(titleText, comment.getString("Title"));
			}
		};

		submit = new BazaarRequest(
                "apitestcustomer",
				"1wtp4lx7aww42x4154oly21ae",
                BazaarEnvironment.staging,
                ApiVersion.FIVE_FOUR);

		final SubmissionParams submissionParams = new SubmissionParams();
		submissionParams.setProductId("1001");
		submissionParams.setReviewId("967");
		submissionParams.setAction(Action.PREVIEW);
		submissionParams.setCommentText(commentText);
		submissionParams.setTitle(titleText);
		submissionParams.setUserId("cwod");

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				submit.postSubmission(RequestType.STORY_COMMENTS, submissionParams, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}
}
