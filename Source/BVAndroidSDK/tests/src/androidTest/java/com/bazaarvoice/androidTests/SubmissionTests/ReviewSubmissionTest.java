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
import com.bazaarvoice.types.Action;
import com.bazaarvoice.types.RequestType;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi") public class ReviewSubmissionTest extends BaseTest {
	private final String tag = getClass().getSimpleName();

	public void testReviewSubmit() throws Throwable {

		final String reviewText = "This is my review text.uaskhakdhakshdakhsdkahsdkajhsdkahskdhakdjhakjshdakjhdakjhdakjsdhkajsdajkhdkajhsdkajhsdkajhsdkjahskdjhakdjhaksjh";
		final String title = "This is my title";
		final OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response) throws JSONException {
				Log.i(tag, "Response = \n" + response);
				JSONObject review = response.getJSONObject("Review");

				// assert there are results

				assertEquals(reviewText, review.getString("ReviewText"));
				assertEquals(title, review.getString("Title"));
			}
		};

		final SubmissionParams submissionParams = new SubmissionParams();
		// //Log.e(TAG,submissionParams.getEncryptedUser());
		submissionParams.setProductId("80087355");
		submissionParams.setAction(Action.PREVIEW);
		submissionParams.setRating(5);
		submissionParams.setReviewText(reviewText);
		submissionParams.setTitle(title);
		submissionParams.setUserNickname("gpezz");
		submissionParams.setUserId("gpezz");
		submissionParams.addRatingForDimensionExternalId("SomeDimension", "4");

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				submit.postSubmission(RequestType.REVIEWS, submissionParams, bazaarResponse);
			}
		});

		bazaarResponse.waitForTestToFinish();
	}

}
