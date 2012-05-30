package bazaarsdk.test;

import static junit.framework.Assert.assertEquals;
import junit.framework.AssertionFailedError;

import org.json.JSONException;
import org.json.JSONObject;

import com.requiem.bazaarvoice.*;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

public class BazaarSDKTestActivity extends Activity implements OnBazaarResponse{
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	try{
			throwNull();
		}
		catch (NullPointerException e){
			Log.i("Exception", "Null Pointer");
		}
		catch (AssertionFailedError e){
			Log.i("Exception", "Assertion Error");
		}
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String key = "kuy3zj9pr3n7i0wxajrzj04xo";
        String version = "5.1";
        
        BazaarRequest request = new BazaarRequest("reviews.apitestcustomer.bazaarvoice.com/bvstaging", key, version);
        DisplayParams params = new DisplayParams();
        params.addFilter("Id", Equality.EQUAL, "16369");
        
        request.sendDisplayRequest(RequestType.ANSWERS, params, this);
        
        
    }
    
    private static void throwNull() throws NullPointerException {
		assertEquals(1,0);
	}

	public void onException(String s, Throwable t) {
		//((TextView)findViewById(R.id.hello)).setText(s);
		t.printStackTrace();
		
	}

	public void onResponse(final JSONObject json) {
		try {
			Log.i("Response", json.getJSONArray("Results").getJSONObject(0).get("AnswerText").toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}