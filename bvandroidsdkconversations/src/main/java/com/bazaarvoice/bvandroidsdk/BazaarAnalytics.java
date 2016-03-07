/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.net.Uri;
import android.os.AsyncTask;

import com.bazaarvoice.bvandroidsdk.jackson.rison.RisonFactory;
import com.bazaarvoice.bvandroidsdk.types.RequestType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Internal SDK API for sending Conversations Analytics.
 */
class BazaarAnalytics {

	private final static String SOURCE = "bv-android-sdk";
    private final static String PRODUCT = "bv-android-sdk";
    private static final String TAG = BazaarAnalytics.class.getSimpleName();

    private final Uri baseUri;

	public BazaarAnalytics(String clientId, BazaarEnvironment environment) {

		this.baseUri = BazaarAnalytics.getBaseUri(environment)
				.buildUpon()
				.appendQueryParameter("source", SOURCE)
				.appendQueryParameter("client", clientId)
				.appendQueryParameter("environment", environment.toString())
                .appendQueryParameter("bvProduct", PRODUCT)
				.build();
	}

	/**
	 * Interface to receive confirmation of an event being sent correctly.
	 */
	public interface SendEventCallback {
		/**
		 * Called when the event has been successfully sent.
         * @param url
         */
		public abstract void success(URL url);
		
		/**
		 * Called when there was an error sending the event.
		 */
		public abstract void failure(BazaarException cause);
	}

    public void sendAnalyticsEvent(String responseUrl, JSONObject response, SendEventCallback callback) {

        try {
            JSONArray results = response.getJSONArray("Results");

            // Chunk the results into 10 events, so that the results fit in a given URI.
            // handle only 10 chunks per network request, as some URI limits are as low as 2000 characters.
            //
            for(int chunkIndex=0; chunkIndex<results.length(); chunkIndex+=10) {
                List<Map<String, Object>> eventMaps = new ArrayList<>();

                // Chunk the next 10 results together
                //
                for(int i=chunkIndex; i<chunkIndex+10 && i<results.length(); i++){

                    JSONObject result = results.getJSONObject(i);

                    for(RequestType requestType : RequestType.values()){

                        if(responseUrl.contains(requestType.getDisplayName() + ".json")){

                            Map eventMap = new HashMap();
                            eventMap.put("cl", "Impression");
                            eventMap.put("visible", false);
                            eventMaps.add(addParamsBasedOnType(eventMap, requestType, result));
                        }
                    }
                }

                ObjectMapper RISON = new ObjectMapper(new RisonFactory());
                String risonEncodedEventString = RISON.writeValueAsString(eventMaps);

                Uri.Builder eventUri = this.baseUri.buildUpon()
                    .appendQueryParameter("cl", "null")
                    .appendQueryParameter("type", "UGC")
                    .appendQueryParameter("r_batch", risonEncodedEventString);

                final String url = eventUri.build().toString();
                this.getInBackground(new URL(url), callback);
            }
        } catch (Exception e) {
            if(callback != null) {
                callback.failure(new BazaarException("Error sending impression event", e));
            }
        }
    }

    private Map addParamsBasedOnType(Map eventMap, RequestType requestType, JSONObject response) throws JSONException {
        switch (requestType) {
            case REVIEWS:
                eventMap.put("contentType", "Review");
                eventMap.put("visible", false);
                eventMap.put("contentId", response.getString("Id"));
                eventMap.put("productId", response.getString("ProductId"));
                break;
            case QUESTIONS:
                eventMap.put("contentType", "Question");
                eventMap.put("contentId", response.getString("Id"));
                eventMap.put("productId", response.getString("ProductId"));
                eventMap.put("categoryId", response.getString("CategoryId"));
                break;
            case PRODUCTS:
                eventMap.put("contentType", "Product");
                eventMap.put("contentId", response.getString("Id"));
                eventMap.put("productId", response.getString("Id"));
                eventMap.put("categoryId", response.getString("CategoryId"));
                break;
            case STATISTICS:
                eventMap.put("contentType", "Statistic");
                eventMap.put("contentId", "");
                eventMap.put("productId", response.getJSONObject("ProductStatistics").getString("ProductId"));
                break;
            case STORY_COMMENTS:
                eventMap.put("contentType", "Comment");
                eventMap.put("contentId", response.getString("Id"));
                break;
            case CATEGORIES:
                eventMap.put("contentType", "Category");
                eventMap.put("contentId", response.getString("Id"));
                break;
            case STORIES:
                eventMap.put("contentType", "Story");
                eventMap.put("contentId", response.getString("Id"));
                break;
            case ANSWERS:
                eventMap.put("contentType", "Answer");
                eventMap.put("contentId", response.getString("Id"));
                break;
            default:
                break;
        }
        return eventMap;
    }

    void getInBackground(URL url, SendEventCallback callback) {
		new AsyncGet(url, callback).execute();
	}

	public static class AsyncGet extends AsyncTask<Void, Void, Void> {
		//the size of a.gif is 43 bytes, give some extra bytes just in case
		public final static byte[] IGNORE_BUFFER = new byte[50];
		private final URL url;
		private final SendEventCallback callback;
		private BazaarException exception;

		public AsyncGet(URL url, SendEventCallback callback) {
			this.url = url;
			this.callback = callback;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				final URLConnection connection = url.openConnection();
				final InputStream stream = connection.getInputStream();

				// read it to download the pixel, but ignore the contents
				while (stream.read(IGNORE_BUFFER) > -1) { }

			} catch (IOException e) {
				exception = new BazaarException("Error getting a.gif", e);
			}

            return null;
        }

		@Override
		protected void onPostExecute(Void result) {

			if (callback == null) {
				return;
			}
			
			if (exception != null) {
				callback.failure(exception);
			} else {
				callback.success(url);
			}
		}
	}


    /**
     * Get the Bazaarvoice base URL for specific environment.
     *
     * @param environment The desired environment for the Analytics event (PRODUCTION or STAGING)
     */
    public static Uri getBaseUri(BazaarEnvironment environment) {
        if(environment == BazaarEnvironment.PRODUCTION) {
            return new Uri.Builder().scheme("https").path("//network.bazaarvoice.com/st.gif").build();
        }
        else {
            return new Uri.Builder().scheme("https").path("//network-stg.bazaarvoice.com/st.gif").build();
        }
    }
}
