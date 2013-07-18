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

public class CommentsSubmissionTest extends BaseTest {
    private final String tag = getClass().getSimpleName();

    public void testReviewCommentSubmit() {

        final String titleText = "This is my title text for review comment";
        final String commentText = "This is my comment text for review comment ggggggggggggggggggggggggggggggggggggggg";
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONObject comment = response.getJSONObject("Comment");
                //assert there are results

                assertEquals(commentText, comment.getString("CommentText"));
                assertEquals(titleText, comment.getString("Title"));
            }
        };

        SubmissionParams submissionParams = new SubmissionParams();
//        //Log.e(TAG,submissionParams.getEncryptedUser());
        submissionParams.setProductId("1001");
        submissionParams.setReviewId("83964");
        submissionParams.setAction(Action.PREVIEW);
        submissionParams.setCommentText(commentText);
        submissionParams.setTitle(titleText);
        submissionParams.setUserId("cwod");
        submit.postSubmission(RequestType.REVIEW_COMMENTS, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

    public void testStoryCommentSubmit() {

            final String titleText = "This is my title text for story comment";
            final String commentText = "This is my comment text for story comment hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";
            OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
                @Override
                public void onResponseHelper(JSONObject response) throws JSONException {

                    JSONObject comment = response.getJSONObject("Comment");
                    Log.i(tag, "Response = \n" + comment);
                    //assert there are results

                    assertEquals(commentText, comment.getString("CommentText"));
                    assertEquals(titleText, comment.getString("Title"));
                }
            };

            submit = new BazaarRequest(
                            "stories.apitestcustomer.bazaarvoice.com/bvstaging",
                            "1wtp4lx7aww42x4154oly21ae",
                            ApiVersion.FIVE_FOUR);

            SubmissionParams submissionParams = new SubmissionParams();
                submissionParams.setProductId("1001");
                submissionParams.setReviewId("967");
                submissionParams.setAction(Action.PREVIEW);
                submissionParams.setCommentText(commentText);
                submissionParams.setTitle(titleText);
                submissionParams.setUserId("cwod");



            submit.postSubmission(RequestType.STORY_COMMENTS, submissionParams, bazaarResponse);
            bazaarResponse.waitForTestToFinish();
        }
}
