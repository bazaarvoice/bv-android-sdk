package com.bazaarvoice.test.SubmissionTests;

import com.bazaarvoice.*;
import com.bazaarvoice.test.*;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Author: Gary Pezza
 * Created: 5/13/12 8:55 PM
 */
public class PhotoSubmissionTest extends BaseTest {

    private final String tag = getClass().getSimpleName();
    private BazaarRequest submitMedia = new BazaarRequest("reviews.apitestcustomer.bazaarvoice.com/bvstaging",
            "2cpdrhohmgmwfz8vqyo48f52g",
            "5.1");
    public void testPhotoSubmit() {

        //Your PC can't communicate with your device and access your sd card at the same time.  So for this test, lets
        //download a well know image that we don't think is going anywhere so the tests will successfully complete.  If
        //this fails just change the url to something that works
        byte[] imageBytes = null;
        try {
            HttpRequestBase httpRequest = new HttpGet("http://fc04.deviantart.net/images/i/2002/26/9/1/Misconstrue_-_Image_1.jpg");
            HttpClient httpClient = new DefaultHttpClient();

            HttpResponse response = httpClient.execute(httpRequest);
            StatusLine statusLine= response.getStatusLine();
            int status = statusLine.getStatusCode();

            if (status < 200 || status > 299)
            {
                throw new BazaarException("Error communicating with server. "  + statusLine.getReasonPhrase() + " " + status);
            }
            else
            {
                HttpEntity entity = response.getEntity();
                imageBytes = EntityUtils.toByteArray(entity);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Error getting an image for a test!\n");
        }


        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
                Log.i(tag, "Response = \n" + response);
                assertFalse("The test returned errors! ", response.getBoolean("HasErrors"));
                assertNotNull(response.getJSONObject("Photo"));
            }
        };

        SubmissionMediaParams mediaParams = new SubmissionMediaParams("review_comment");
        mediaParams.setUserId("735688f97b74996e214f5df79bff9e8b7573657269643d393274796630666f793026646174653d3230313130353234");
        try {
            mediaParams.setPhoto(imageBytes, "Misconstrue_-_Image_1.jpg");
            submitMedia.queueSubmission(RequestType.PHOTOS, mediaParams, bazaarResponse);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        bazaarResponse.waitForTestToFinish();
    }
}
