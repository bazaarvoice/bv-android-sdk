package com.bazaarvoice.test.SubmissionTests;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bazaarvoice.SubmissionParams;
import com.bazaarvoice.test.BaseTest;
import com.bazaarvoice.test.OnBazaarResponseHelper;
import com.bazaarvoice.types.FeedbackContentType;
import com.bazaarvoice.types.FeedbackType;
import com.bazaarvoice.types.FeedbackVoteType;
import com.bazaarvoice.types.RequestType;

/**
 * Author: Gary Pezza
 * Created: 5/10/12 9:34 PM
 */
public class FeedbackSubmissionTest extends BaseTest {
    private final String tag = getClass().getSimpleName();
    private final String reasonText = "This post was not nice.";

    public void testFeedbackSubmit() {

        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
            	Log.e(tag, "End of Feedback Submission submit transmission : END " + System.currentTimeMillis());
                Log.i(tag, "FeedbackResponse = \n" + response);
                JSONObject feedback = response.getJSONObject("Feedback");

                //assert there are results
               assertEquals(reasonText, feedback.getJSONObject("Inappropriate").getString("ReasonText"));
            }
        };

        SubmissionParams submissionParams = new SubmissionParams();
        submissionParams.setContentType(FeedbackContentType.REVIEW);
        submissionParams.setContentId("83964");
        submissionParams.setUserId("123abc");
        submissionParams.setFeedbackType(FeedbackType.INAPPROPRIATE);
        submissionParams.setReasonText("This post was not nice.");

        Log.e(tag, "Begin of Feedback Submission submit transmission : BEGIN " + System.currentTimeMillis());
        submit.postSubmission(RequestType.FEEDBACK, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }
    
    public void testFeedbackSubmit2() {
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
            	Log.e(tag, "End of Feedback Submission submit transmission : END " + System.currentTimeMillis());
                Log.i(tag, "FeedbackResponse = \n" + response);
                boolean errors = response.getBoolean("HasErrors");
                assertEquals(errors, false);
            }
        };

        SubmissionParams submissionParams = new SubmissionParams();
        submissionParams.setContentType(FeedbackContentType.REVIEW);
        submissionParams.setContentId("83964");
        submissionParams.setUserId("123abc");
        submissionParams.setFeedbackType(FeedbackType.HELPFULNESS);
        submissionParams.setVote(FeedbackVoteType.NEGATIVE);

        Log.e(tag, "Begin of Feedback Submission submit transmission : BEGIN " + System.currentTimeMillis());
        submit.postSubmission(RequestType.FEEDBACK, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

}
