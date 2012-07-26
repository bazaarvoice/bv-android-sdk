package com.bazaarvoice.test.SubmissionTests;

import android.util.Log;

import com.bazaarvoice.ApiVersion;
import com.bazaarvoice.BazaarException;
import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.RequestType;
import com.bazaarvoice.SubmissionMediaParams;
import com.bazaarvoice.test.*;

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

/**
 * Author: Gary Pezza Created: 5/13/12 8:55 PM
 */
public class VideoSubmissionTest extends BaseTest {

	private final String tag = getClass().getSimpleName();
	private BazaarRequest submitMedia = new BazaarRequest(
			"reviews.apitestcustomer.bazaarvoice.com/bvstaging",
			"2cpdrhohmgmwfz8vqyo48f52g", apiVersion);

	public void testVideoSubmit() {

		// Your PC can't communicate with your device and access your sd card at
		// the same time. So for this test, lets
		// download a well know video that we don't think is going anywhere so
		// the tests will successfully complete. If
		// this fails just change the url to something that works
		byte[] imageBytes = null;
		try {
			HttpRequestBase httpRequest = new HttpGet(
					"http://glass.googlecode.com/svn-history/r151/trunk/intro.avi");
			HttpClient httpClient = new DefaultHttpClient();

			HttpResponse response = httpClient.execute(httpRequest);
			StatusLine statusLine = response.getStatusLine();
			int status = statusLine.getStatusCode();

			if (status < 200 || status > 299) {
				throw new BazaarException("Error communicating with server. "
						+ statusLine.getReasonPhrase() + " " + status);
			} else {
				HttpEntity entity = response.getEntity();
				imageBytes = EntityUtils.toByteArray(entity);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error getting a video for a test!\n");
		}

		OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
			@Override
			public void onResponseHelper(JSONObject response)
					throws JSONException {

				Log.i(tag, "Response = \n" + response);

				assertFalse("The test returned errors! ",
						response.getBoolean("HasErrors"));
				assertNotNull(response.getJSONObject("Video").getString(
						"VideoUrl"));
			}
		};

		SubmissionMediaParams mediaParams = new SubmissionMediaParams("review");
		mediaParams
				.setUserId("735688f97b74996e214f5df79bff9e8b7573657269643d393274796630666f793026646174653d3230313130353234");
		try {
			mediaParams.setVideo(imageBytes, "intro.avi");
			submitMedia.queueSubmission(RequestType.VIDEOS, mediaParams,
					bazaarResponse);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		bazaarResponse.waitForTestToFinish();
	}
}
