package com.bazaarvoice.test;

import android.os.SystemClock;
import android.util.Log;
import com.bazaarvoice.OnBazaarResponse;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.assertNull;
/**
 * Created with IntelliJ IDEA.
 * User: gary
 * Date: 4/26/12
 * Time: 10:26 PM
 */
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
