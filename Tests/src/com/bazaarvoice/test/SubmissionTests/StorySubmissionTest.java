package com.bazaarvoice.test.SubmissionTests;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.SubmissionParams;
import com.bazaarvoice.types.*;
import com.bazaarvoice.test.BaseTest;
import com.bazaarvoice.test.OnBazaarResponseHelper;

public class StorySubmissionTest extends BaseTest {
    private final String tag = getClass().getSimpleName();

    public void testStorySubmit() {

        final String titleText = "This is my title text.";
        final String storyText = "This is my story text.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                JSONObject review = response.getJSONObject("Story");
                Log.i(tag, "Response = \n" + review);
                //assert there are results

                assertEquals(storyText, review.getString("StoryText"));
            }
        };

        submit = new BazaarRequest(
                        "stories.apitestcustomer.bazaarvoice.com/bvstaging",
                        "1wtp4lx7aww42x4154oly21ae",
                        ApiVersion.FIVE_THREE);

        SubmissionParams submissionParams = new SubmissionParams();
//        //Log.e(TAG,submissionParams.getEncryptedUser());
        submissionParams.setAction(Action.PREVIEW);
        submissionParams.setCategoryId("6108");
        submissionParams.setTitle(titleText);
        submissionParams.setUserId("cwod");
        submissionParams.setStoryText(storyText);


        submit.postSubmission(RequestType.STORIES, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

}
