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
package com.bazaarvoice.test;

import android.os.SystemClock;
import android.util.Log;
import com.bazaarvoice.OnBazaarResponse;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.assertNull;

public abstract class OnBazaarResponseHelper implements OnBazaarResponse {
    private static final String TAG = "OnBazaarResponseHelper";
    private static final String PROFILER_TAG = "ProfilerInfo";
    
    private String callingFunction;
    long startTime;

    public boolean requestComplete = false;
    private String failStackTrace  = null;

    @Override
    public void onResponse(final JSONObject response) {
    	long executionTime = System.currentTimeMillis() - startTime;
    	Log.i(PROFILER_TAG, callingFunction + ":" + executionTime);


    	try {
            onResponseHelper(response);
            failStackTrace = null;
        } catch (JSONException e) {
            failStackTrace = Log.getStackTraceString(e);
        }

        requestComplete = true;
    }

    public abstract void onResponseHelper(JSONObject response) throws JSONException;

    @Override
    public void onException(final String message, final Throwable exception) {
        Log.i(TAG, "Error = " + message + "\n" + Log.getStackTraceString(exception));
        failStackTrace = Log.getStackTraceString(exception);
        requestComplete = true;
    }

    public void waitForTestToFinish() {
    	callingFunction = new Throwable().getStackTrace()[1].getMethodName();
    	startTime = System.currentTimeMillis();

        while (!requestComplete) {
            SystemClock.sleep(200);
        }
        assertNull("Stack Trace? " + failStackTrace, failStackTrace);
    }
}
