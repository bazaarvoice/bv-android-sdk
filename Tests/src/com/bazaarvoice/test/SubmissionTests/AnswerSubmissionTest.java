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
//                assertFalse(""+response, response.getBoolean("HasErrors"));

                JSONObject review = response.getJSONObject("Answer");
                Log.i(tag, "Response = \n" + review);
                //assert there are results

                assertEquals(answerText, review.getString("AnswerText"));
            }
        };

        submit = new BazaarRequest(
                        "answers.apitestcustomer.bazaarvoice.com/bvstaging",
                        "1wtp4lx7aww42x4154oly21ae",
                        ApiVersion.FIVE_THREE);


        SubmissionParams submissionParams = new SubmissionParams();
//        //Log.e(TAG,submissionParams.getEncryptedUser());
        submissionParams.setCategoryId("1000001");
        submissionParams.setQuestionId("6104");
        submissionParams.setAction(Action.PREVIEW);
        submissionParams.setAnswerText(answerText);
        submissionParams.setUserId("gpezz");


        submit.postSubmission(RequestType.ANSWERS, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

}
