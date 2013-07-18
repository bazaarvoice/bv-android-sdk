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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

import android.content.res.AssetManager;
import android.util.Log;

import com.bazaarvoice.BazaarException;
import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.SubmissionMediaParams;
import com.bazaarvoice.test.BaseTest;
import com.bazaarvoice.test.OnBazaarResponseHelper;
import com.bazaarvoice.types.ApiVersion;
import com.bazaarvoice.types.MediaParamsContentType;
import com.bazaarvoice.types.RequestType;

public class VideoSubmissionTest extends BaseTest {

    private final String tag = getClass().getSimpleName();
    private BazaarRequest submitMedia;
    
    @Override
	protected void setUp() throws Exception {
    	submitMedia = new BazaarRequest("reviews.apitestcustomer.bazaarvoice.com/bvstaging",
                "2cpdrhohmgmwfz8vqyo48f52g",
                ApiVersion.FIVE_FOUR);
	}
    
    public void testVideoSubmit() {

        //Your PC can't communicate with your device and access your sd card at the same time.  So for this test, lets
        //download a well know video that we don't think is going anywhere so the tests will successfully complete.  If
        //this fails just change the url to something that works
        byte[] imageBytes = null;
        try {
            HttpRequestBase httpRequest = new HttpGet("http://glass.googlecode.com/svn-history/r151/trunk/intro.avi");
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
            throw new RuntimeException("Error getting a video for a test!\n");
        }


        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
            	Log.e(tag, "End of video submit transmission : END " + System.currentTimeMillis());
            	
                Log.i(tag, "Response = \n" + response);

                assertFalse("The test returned errors! ", response.getBoolean("HasErrors"));
                assertNotNull(response.getJSONObject("Video").getString("VideoUrl"));
            }
        };

        SubmissionMediaParams mediaParams = new SubmissionMediaParams(MediaParamsContentType.REVIEW);
        mediaParams.setUserId("735688f97b74996e214f5df79bff9e8b7573657269643d393274796630666f793026646174653d3230313130353234");
        try {
        	
            mediaParams.setVideo(imageBytes, "Android_Video.mp4");
            
            Log.e(tag, "Begin of video submit transmission : BEGIN " + System.currentTimeMillis());
            submitMedia.postSubmission(RequestType.VIDEOS, mediaParams, bazaarResponse);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        bazaarResponse.waitForTestToFinish();
    }
    
    public void testVideoSubmit2() {

        //Your PC can't communicate with your device and access your sd card at the same time.  So for this test, lets
        //download a well know video that we don't think is going anywhere so the tests will successfully complete.  If
        //this fails just change the url to something that works
        OnBazaarResponseHelper bazaarResponse = new OnBazaarResponseHelper() {
            @Override
            public void onResponseHelper(JSONObject response) throws JSONException {
            	Log.e(tag, "End of video submit transmission : END " + System.currentTimeMillis());
            	
                Log.i(tag, "Response = \n" + response);

                assertFalse("The test returned errors! ", response.getBoolean("HasErrors"));
                assertNotNull(response.getJSONObject("Video").getString("VideoUrl"));
            }
        };

        SubmissionMediaParams mediaParams = new SubmissionMediaParams(MediaParamsContentType.REVIEW);
        mediaParams.setUserId("735688f97b74996e214f5df79bff9e8b7573657269643d393274796630666f793026646174653d3230313130353234");
        
        AssetManager assets = this.mContext.getAssets();
        File dir = this.mContext.getDir("TEMP", 0);
        File file = new File(dir, "video.mp4");

        InputStream in = null;
        FileOutputStream out = null;
        try {
			in = assets.open("Android_Video.mp4");
			out = new FileOutputStream(file);
			  
			byte[] buffer = new byte[1024];
			int read;
			while((read = in.read(buffer)) != -1){
			    out.write(buffer, 0, read);
			}
			  
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
        } catch(IOException e) {
            e.printStackTrace();
        }       

        try {
			mediaParams.setVideo(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        Log.e(tag, "Begin of video submit transmission : BEGIN " + System.currentTimeMillis());
        submitMedia.postSubmission(RequestType.VIDEOS, mediaParams, bazaarResponse);
        bazaarResponse.waitForTestToFinish();
    }
}
