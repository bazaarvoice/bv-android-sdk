package com.requiem.bazaarvoice.test.SubmissionTests;

import com.requiem.bazaarvoice.test.*;

import android.util.Log;
import com.requiem.bazaarvoice.BazaarRequest;

import com.requiem.bazaarvoice.Action;
import com.requiem.bazaarvoice.RequestType;
import com.requiem.bazaarvoice.SubmissionParams;
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
                        "5.1");


        SubmissionParams submissionParams = new SubmissionParams();
//        //Log.e(TAG,submissionParams.getEncryptedUser());
        submissionParams.setCategoryId("1000001");
        submissionParams.setQuestionId("6104");
        submissionParams.setAction(Action.submit);
        submissionParams.setAnswerText(answerText);
        submissionParams.setUserId("gpezz");


        submit.postSubmission(RequestType.ANSWERS, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

}
