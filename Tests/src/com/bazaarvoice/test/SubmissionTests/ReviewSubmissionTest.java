package com.bazaarvoice.test.SubmissionTests;

import com.bazaarvoice.*;
import com.bazaarvoice.test.*;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Gary Pezza
 * Created: 5/10/12 9:34 PM
 */
public class ReviewSubmissionTest extends BaseTest {
    private final String tag = getClass().getSimpleName();

    public void testReviewSubmit() {

        final String reviewText = "This is my review text.uaskhakdhakshdakhsdkahsdkajhsdkahskdhakdjhakjshdakjhdakjhdakjsdhkajsdajkhdkajhsdkajhsdkajhsdkjahskdjhakdjhaksjh";
        final String title = "This is my title";
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);
                JSONObject review = response.getJSONObject("Review");

                //assert there are results

                assertEquals(reviewText, review.getString("ReviewText"));
                assertEquals(title, review.getString("Title"));
            }
        };

        SubmissionParams submissionParams = new SubmissionParams();
//        //Log.e(TAG,submissionParams.getEncryptedUser());
        submissionParams.setProductId("80087355");
        submissionParams.setAction(Action.preview);
        submissionParams.setRating(5);
        submissionParams.setReviewText(reviewText);
        submissionParams.setTitle(title);
        submissionParams.setUserNickname("gpezz");
        submissionParams.setUserId("gpezz");

        submit.postSubmission(RequestType.REVIEWS, submissionParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }

}
