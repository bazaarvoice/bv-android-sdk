package com.bazaarvoice.test.SubmissionTests;

import com.bazaarvoice.*;
import com.bazaarvoice.types.*;
import com.bazaarvoice.test.*;

import android.util.Log;

import org.apache.http.ReasonPhraseCatalog;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Gary Pezza
 * Created: 5/10/12 9:34 PM
 */
public class FeedbackSubmissionTest extends BaseTest {
    private final String tag = getClass().getSimpleName();

    public void testFeedbackSubmit() {
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "FeedbackResponse = \n" + response);
                //JSONObject review = response.getJSONObject("Review");

                //assert there are results
                //assertEquals(title, review.getString("Title"));
            }
        };

        String reasonText = "This post was not nice.";
        SubmissionParams submissionParams = new SubmissionParams();
        submissionParams.setContentType(FeedbackContentType.REVIEW);
        submissionParams.setContentId("83964");
        submissionParams.setUserId("123abc");
        submissionParams.setFeedbackType(FeedbackType.INAPPROPRIATE);
        submissionParams.setReasonText("This post was not nice.");

        submit.postSubmission(RequestType.FEEDBACK, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }
    
    public void testFeedbackSubmit2() {
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
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

        submit.postSubmission(RequestType.FEEDBACK, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

}
