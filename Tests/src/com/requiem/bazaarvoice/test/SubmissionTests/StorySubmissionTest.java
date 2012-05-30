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

import java.util.Iterator;

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
                        "5.1");

        SubmissionParams submissionParams = new SubmissionParams();
//        //Log.e(TAG,submissionParams.getEncryptedUser());
        submissionParams.setAction(Action.preview);
        submissionParams.setCategoryId("6106");
        submissionParams.setTitle(titleText);
        submissionParams.setUserId("cwod");
        submissionParams.setStoryText(storyText);


        submit.postSubmission(RequestType.STORIES, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

}
