package com.bazaarvoice.test.SubmissionTests;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bazaarvoice.RequestType;
import com.bazaarvoice.SubmissionFeedbackParams;
import com.bazaarvoice.SubmissionFeedbackParams.FeedbackType;
import com.bazaarvoice.SubmissionFeedbackParams.Vote;
import com.bazaarvoice.test.BaseTest;
import com.bazaarvoice.test.OnBazaarResponseHelper;

public class FeedbackSubmissionTest extends BaseTest{
	private final String tag = getClass().getSimpleName();
	
	public void testHelpfulnessSubmit(){
		OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper(){

			@Override
			public void onResponseHelper(JSONObject response)
					throws JSONException {
				Log.i(tag, "Response = \n" + response);
				assertFalse(response.getBoolean("HasErrors"));
				
				JSONObject helpfulness = response.getJSONObject("Feedback").optJSONObject("Helpfulness");
				assertNotNull(helpfulness);
				assertEquals("POSITIVE", helpfulness.getString("Vote"));
				assertEquals("testUser", helpfulness.getString("AuthorId"));
			}
			
		};
		
		SubmissionFeedbackParams params = new SubmissionFeedbackParams("review");
		params.setContentId("6384491");
		params.setFeedbackType(FeedbackType.HELPFULNESS);
		params.setUserId("testUser");
		params.setVote(Vote.POSITIVE);
		
		submit.postSubmission(RequestType.FEEDBACK, params, bazaarResponse);
		bazaarResponse.waitForTestToFinish();
	}
	
	public void testInappropriateSubmit(){
		final String reasonText = "This is the reason why I find this content inappropriate.";
		OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper(){

			@Override
			public void onResponseHelper(JSONObject response)
					throws JSONException {
				Log.i(tag, "Response = \n" + response);
				assertFalse(response.getBoolean("HasErrors"));
				
				JSONObject inappropriate = response.getJSONObject("Feedback").optJSONObject("Inappropriate");
				assertNotNull(inappropriate);
				assertEquals(reasonText, inappropriate.getString("ReasonText"));
				assertEquals("testUser", inappropriate.getString("AuthorId"));
			}
			
		};
		
		SubmissionFeedbackParams params = new SubmissionFeedbackParams("review");
		params.setContentId("6384491");
		params.setFeedbackType(FeedbackType.INAPPROPRIATE);
		params.setUserId("testUser");
		params.setReasonText(reasonText);
		
		submit.postSubmission(RequestType.FEEDBACK, params, bazaarResponse);
		bazaarResponse.waitForTestToFinish();
	}

}
