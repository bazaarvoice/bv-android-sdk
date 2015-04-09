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
import com.bazaarvoice.OnBazaarResponseHelper;

import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.SubmissionParams;
import com.bazaarvoice.types.Action;
import com.bazaarvoice.types.ApiVersion;
import com.bazaarvoice.types.RequestType;

@SuppressLint("NewApi") public class StorySubmissionTest extends BaseTest {
	private final String tag = getClass().getSimpleName();

	public void testStorySubmit() throws Throwable {

		final String titleText = "This is my title text.";
		final String storyText = "This is my story text.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				JSONObject review = response.getJSONObject("Story");
				Log.i(tag, "Response = \n" + review);
				// assert there are results

				assertEquals(storyText, review.getString("StoryText"));
			}
		};

		submit = new BazaarRequest(
                "reviews.apitestcustomer.bazaarvoice.com/bvstaging", "stories.apitestcustomer.bazaarvoice.com/bvstaging",
				"1wtp4lx7aww42x4154oly21ae",
				ApiVersion.FIVE_THREE);

		final SubmissionParams submissionParams = new SubmissionParams();
		// //Log.e(TAG,submissionParams.getEncryptedUser());
		submissionParams.setAction(Action.PREVIEW);
		submissionParams.setCategoryId("6108");
		submissionParams.setTitle(titleText);
		submissionParams.setUserId("cwod");
		submissionParams.setStoryText(storyText);

		runTestOnUiThread(new Runnable() {

			@Override
			public void run() {
				submit.postSubmission(RequestType.STORIES, submissionParams, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

}
