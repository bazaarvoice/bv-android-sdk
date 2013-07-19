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

public class AnswerSubmissionTest extends BaseTest {
    private final String tag = getClass().getSimpleName();

    public void testAnswerSubmit() {

        final String answerText = "This is my answer text.";
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                assertFalse(""+response, response.getBoolean("HasErrors"));

                JSONObject review = response.getJSONObject("Answer");
                Log.i(tag, "Response = \n" + review);
                //assert there are results

                assertEquals(answerText, review.getString("AnswerText"));
            }
        };

        submit = new BazaarRequest(
                        "answers.apitestcustomer.bazaarvoice.com/bvstaging",
                        "1wtp4lx7aww42x4154oly21ae",
                        ApiVersion.FIVE_FOUR);


        SubmissionParams submissionParams = new SubmissionParams();
        submissionParams.setCategoryId("1000001");
        submissionParams.setQuestionId("6104");
        submissionParams.setAction(Action.PREVIEW);
        submissionParams.setAnswerText(answerText);
        submissionParams.setUserId("gpezz");
        submissionParams.setUserNickname(Long.toString(System.currentTimeMillis()));

        submit.postSubmission(RequestType.ANSWERS, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

}
