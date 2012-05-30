package com.requiem.bazaarvoice.test.SubmissionTests;

import com.requiem.bazaarvoice.*;
import com.requiem.bazaarvoice.test.*;

import android.util.Log;
import com.requiem.bazaarvoice.BazaarRequest;

import com.requiem.bazaarvoice.Action;
import com.requiem.bazaarvoice.RequestType;
import com.requiem.bazaarvoice.SubmissionParams;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionSubmissionTest extends BaseTest {
    private final String tag = getClass().getSimpleName();

    public void testQuestionSubmit() {

        final String questionText = "This is my question text.uaskhakdhakshdakhsdkahsdkajhsdkahskdhakdjhakjshdakjhdakjhdakjsdhkajsdajkhdkajhsdkajhsdkajhsdkjahskdjhakdjhaksjh";
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                assertFalse(""+response, response.getBoolean("HasErrors"));

                JSONObject review = response.getJSONObject("Question");
                Log.i(tag, "Response = \n" + review);
                //assert there are results

                assertEquals(questionText, review.getString("QuestionSummary"));
            }
        };

        submit = new BazaarRequest(
                        "answers.apitestcustomer.bazaarvoice.com/bvstaging",
                        "1wtp4lx7aww42x4154oly21ae",
                        "5.1");

        SubmissionParams submissionParams = new SubmissionParams();
//        //Log.e(TAG,submissionParams.getEncryptedUser());
        submissionParams.setProductId("1000001");
        submissionParams.setAction(Action.submit);
        submissionParams.setQuestionSummary(questionText);
        submissionParams.setUserId("cwod");


        submit.postSubmission(RequestType.QUESTIONS, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

}