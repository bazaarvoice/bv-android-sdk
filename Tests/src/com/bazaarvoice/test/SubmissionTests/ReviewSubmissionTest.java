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
package com.bazaarvoice.test.SubmissionTests;

import com.bazaarvoice.*;
import com.bazaarvoice.types.*;
import com.bazaarvoice.test.*;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class ReviewSubmissionTest extends BaseTest {
    private final String tag = getClass().getSimpleName();

    public void testReviewSubmit() {

        final String reviewText = "This is my review text.uaskhakdhakshdakhsdkahsdkajhsdkahskdhakdjhakjshdakjhdakjhdakjsdhkajsdajkhdkajhsdkajhsdkajhsdkjahskdjhakdjhaksjh";
        final String title = "This is my title";
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONObject review = response.getJSONObject("Review");

                //assert there are results

                assertEquals(reviewText, review.getString("ReviewText"));
                assertEquals(title, review.getString("Title"));
            }
        };

        SubmissionParams submissionParams = new SubmissionParams();
//        //Log.e(TAG,submissionParams.getEncryptedUser());
        submissionParams.setProductId("80087355");
        submissionParams.setAction(Action.PREVIEW);
        submissionParams.setRating(5);
        submissionParams.setReviewText(reviewText);
        submissionParams.setTitle(title);
        submissionParams.setUserNickname("gpezz");
        submissionParams.setUserId("gpezz");
        submissionParams.addRatingForDimensionExternalId("SomeDimension", "4");

        submit.postSubmission(RequestType.REVIEWS, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

}
