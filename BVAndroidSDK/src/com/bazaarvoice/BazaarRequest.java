package com.bazaarvoice;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.bazaarvoice.types.ApiVersion;
import com.bazaarvoice.types.RequestType;

/**
 *
 * Sends and handles requests to the Bazaarvoice API. Both
 * submissions and display requests are handled by this class. They are
 * differentiated by calling the corresponding method to send the request and by
 * passing an object of the corresponding subclass of BazaarParams.
 * <p>
 * There are options for both display and submission using asynchronous
 * requests, queued requests, and blocking requests.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class BazaarRequest {
	
	private static final String TAG = "BazaarRequest";
	
	private final String SDK_HEADER_NAME = "X-UA-BV-SDK";
	private final String SDK_HEADER_VALUE = "ANDROID_SDK_V202";

	private String passKey;
	private String apiVersion;
	private String requestUrl;
	
	private Media mediaEntity;
	private OnBazaarResponse listener;
	
	private HttpURLConnection connection;
    protected URL url;
    private String header;
    protected String httpMethod;
    private List<ArrayList<String>> requestParams;
    private String serverResponseMessage = null;
    protected int serverResponseCode;
    protected int contentLength = 0;
    protected Object receivedData;
    private String boundary;
    private boolean multipart = false;
    private boolean media = false;
    private String twoHyphens = "--";


	/**
	 * Initialize the request with the necessary parameters.
	 * 
	 * @param domainName
	 *            your client domain name
	 * @param passKey
	 *            your api test key
	 * @param apiVersion
	 *            the version of the api you want to use
	 */
	public BazaarRequest(String domainName, String passKey, ApiVersion apiVersion) {
		this.passKey = passKey;
		this.apiVersion = apiVersion.getVersionName();

		requestUrl = "http://" + domainName + "/data/";
		
		requestParams = new ArrayList<ArrayList<String>>();
    	receivedData = null;
    	mediaEntity = null;
    	
    	//make a random boundary for the HTTP requests
    	Random random = new Random();
	    int min = 1;
	    int max = 10000;
    	boundary = (Long.valueOf(System.currentTimeMillis()).toString() + (random.nextInt(max - min + 1) + min)); 
	}

	/**
	 * Send a request, spawn a thread, and return the result via the listener.
	 * (non-blocking)
	 * 
	 * @param type
	 *            the type of request
	 * @param params
	 *            the parameters for the request
	 * @param listener
	 *            the listener to handle the results on
	 * @throws BazaarException 
	 */
	public void sendDisplayRequest(final RequestType type,
			DisplayParams params, final OnBazaarResponse listener) {
		
		url = getRequestString(type.getDisplayName(), params);	
		
		this.listener = listener;
		
		new AsyncTransaction().execute("GET"); 
	}

	/**
	 * Post a submission, spawn a thread, and return the response via the
	 * listener. (non-blocking)
	 * 
	 * @param type
	 *            the type of request
	 * @param params
	 *            the parameters for the request
	 * @param listener
	 *            the listener to handle the results on
	 * @throws BazaarException 
	 */
	public void postSubmission(final RequestType type, BazaarParams params,
			final OnBazaarResponse listener) {
		
		url = getRequestString(type.getSubmissionName(), null);
		
		Log.e(TAG, "params = " + params);	
		if (params != null) {
			if (params.getClass().equals(SubmissionMediaParams.class)) {
				submissionMediaParamsAddPostParameters((SubmissionMediaParams) params);
			}
			else { 
				submissionParamsAddPostParameters((SubmissionParams) params);
			}
			this.mediaEntity = params.getMedia();
		} else {
			this.mediaEntity = null;
		}
		
		if (this.mediaEntity != null) {
			if (this.mediaEntity.getFile() != null) {
				addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getMimeType(), mediaEntity.getFile());
			} else {
				Log.e(TAG, "mediaEntity.getName() = " + mediaEntity.getName());
				Log.e(TAG, "mediaEntity.getFilename() = " + mediaEntity.getFilename());
				Log.e(TAG, "mediaEntity.getMimeType() = " + mediaEntity.getMimeType());
				addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getMimeType(), mediaEntity.getBytes());
			}
			
		}
		
		this.listener = listener;
		
		new AsyncTransaction().execute("POST"); 
	}

	
	private URL getRequestString(final String type, BazaarParams params) {
		//build url xxxx.ugc.bazaarvoice.com/data/xxx.json
		String requestString = requestUrl + type + ".json?" + "apiversion=" + apiVersion + "&" + "passkey=" + passKey;

		if (params != null) {
			requestString = requestString + displayParamsToURL((DisplayParams) params);
		}
		
		URL url = null;
		try {
			url = new URL(requestString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return url;
	}


	@SuppressLint("NewApi")
	private class AsyncTransaction extends AsyncTask<String, Integer, String> {
		
		@Override
		protected String doInBackground(String... args) {
			
			String httpMethod = args[0];
			Log.e(TAG, "httpMethod = " + httpMethod);
			
			try {
				//accept no cookies
				//TODO requires Gingerbread and up, we'll need to decide on this.
				/*
				CookieManager cookieManager = new CookieManager();
				cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_NONE);
				CookieHandler.setDefault(cookieManager);
				*/
				
				Log.e(TAG, "url = " + url);
				System.setProperty("http.keepAlive", "false");
				connection = (HttpURLConnection) url.openConnection();
	            
				// Allow Inputs & Outputs
				connection.setRequestMethod(httpMethod);
				connection.setDoInput(true);
				connection.setUseCaches(false);			
				if (httpMethod.equals("POST")) {
					connection.setDoOutput(true);
					if (multipart) {
						contentLength = contentLength + (twoHyphens + boundary + twoHyphens + "\r\n").getBytes().length;
					}
					if (contentLength != 0) {
						connection.setRequestProperty("Content-length", (Integer.valueOf(contentLength).toString()));
					}
				}
	            			
				//Headers
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Accept", "application/xml");	
				// httpRequest.setHeader("Content-Type", "multipart/form-data");
				connection.setRequestProperty(SDK_HEADER_NAME, SDK_HEADER_VALUE);
				
				if ( httpMethod.equals("POST")) {
					//open stream and start writting
					Log.e(TAG, "write to server");
					writeToServer(connection);
				}
					
				serverResponseMessage = readResponse(connection.getInputStream());									
				serverResponseCode = connection.getResponseCode();

			} catch (IOException e) {	
				e.printStackTrace();
				listener.onException("There was an error in the network connection", e);
			} 
			
			return serverResponseMessage;
		}
	    
		//Process after transaction is complete
		@Override
		protected void onPostExecute(String result) {
			
			if (serverResponseCode < 200 || serverResponseCode > 299) {
				listener.onException("Error communicating with server.", new BazaarException("Message : "
						+ serverResponseMessage + " Error :  " + serverResponseCode));
			} else {
				try {
					listener.onResponse(new JSONObject(serverResponseMessage));
				} catch (JSONException e) {
					listener.onException("Error reading JSON response.", e);
				}
			}
		}
				
		private String readResponse(InputStream stream) throws IOException {
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			
			int bytesRead = -1;
			byte[] buffer = new byte[1024];
			while ((bytesRead = stream.read(buffer)) >= 0) {
				// process the buffer, "bytesRead" have been read, no more, no less
				baf.append(buffer, 0, bytesRead);
			}
			stream.close();
			return new String(baf.toByteArray());
		}
		
		private void writeToServer(HttpURLConnection connection) throws IOException {
			
		    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			
		    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		    
			//buffer for file transfers
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1*1024*1024;
			
			Log.e(TAG, "requestParams = " + requestParams);
			
			for (ArrayList<String> part : requestParams) {
				outputStream.writeBytes(part.get(0));
				outputStream.writeBytes(part.get(1));
			}
					
			Log.e(TAG, "media = " + media);
			
			if (media) {
				outputStream.writeBytes(header);		
			
				//File I/O
				if (mediaEntity.getFile() != null) {
					Log.e(TAG, "sending out file");
					FileInputStream fileInputStream = new FileInputStream(mediaEntity.getFile());
				
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];
					
					// Read file
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					
					while (bytesRead > 0)
					{
		                outputStream.write(buffer, 0, bufferSize);
		                bytesAvailable = fileInputStream.available();
		                bufferSize = Math.min(bytesAvailable, maxBufferSize);
		                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}
					
					outputStream.writeBytes("\r\n");
					fileInputStream.close();
					
				} else {
					Log.e(TAG, "mediaEntity.getBytes().length = " + mediaEntity.getBytes().length);
					
					ByteArrayInputStream fileInputStream = new ByteArrayInputStream(mediaEntity.getBytes());
					
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];
					
					// Read file
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					
					while (bytesRead > 0)
					{
		                outputStream.write(buffer, 0, bufferSize);
		                bytesAvailable = fileInputStream.available();
		                bufferSize = Math.min(bytesAvailable, maxBufferSize);
		                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}
					
					outputStream.writeBytes("\r\n");
					fileInputStream.close();							
				}	
			}
			
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + "\r\n");				
			
			outputStream.flush();
			outputStream.close();
		}		
	}

	
	private String addURLParameter(String url, String name, Object val) {
		if (val != null) {
			String value = val.toString();
			
			if (value.length() > 0) {
				value = BazaarParams.encode(value);
				url += "&" + name + '=' + value;
			}
		}
		return url;
	}
	
	
	
	private void addMultipartParameter(String name, Object val) {
		if (val != null) {
			String value = val.toString();
    	
	        if (name != null) {
	        	ArrayList<String> item = new ArrayList<String>();
	        	String header = String.format("--%s\r\nContent-Disposition: form-data; name=\"%s\"\r\n\r\n", boundary, name); 
	        	String body = String.format("%s\r\n", value);		
	
	        	item.add(header);
	        	item.add(body);
	        	requestParams.add(item);
	        	
	        	contentLength = contentLength + header.getBytes().length + body.getBytes().length;
	        	
	        	multipart = true;
	        }	     
		}
    }
	
	
	
	private String addURLParameter(String url, String name, Map<String, String> map) {
		if (map != null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				url = addURLParameter(url, name + "_" + key, value);
			}
		}
		return url;
	}
	
	
	
	private void addMultipartParameter(String name, Map<String, String> map) {
		if (map != null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				addMultipartParameter(name + "_" + key, value);
			}
		}
	}

	
	private String addURLParameter(String url, String name,
			List<String> values) {
		if (values != null) {
			String paramList = "";
			boolean first = true;
			for (String value : values) {
				if (first) {
					first = false;
				} else {
					paramList += ",";
				}

				paramList += value;
			}
			return addURLParameter(url, name, paramList);
		}
		return url;
	}
	
	
	private void addMultipartParameter(String name, List<String> values) {
		if (values != null) {
			String paramList = "";
			boolean first = true;
			for (String value : values) {
				if (first) {
					first = false;
				} else {
					paramList += ",";
				}

				paramList += value;
			}
			addMultipartParameter(name, paramList);
		}
	}

	
	private String addNthURLParamsFromList(String url, String name,
			List<String> values) {
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				// Start with param_1
				int key = i + 1;
				String value = values.get(i);

				url = addURLParameter(url, name + "_" + key, value);
			}
		}
		return url;
	}
	
	
	private void addNthMultipartParameter(String name, List<String> values) {
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				// Start with param_1
				int key = i + 1;
				String value = values.get(i);

				addMultipartParameter(name + "_" + key, value);
			}
		}
	}
	
	//adding a file object to request
	private void addMultipartParameter(String name, String fileName, String contentType, File mediaFile) {
    	
        if ((name != null) && (fileName != null) && (contentType != null)) {
        	
        	header = String.format("--%s\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n", 
        				boundary, name, fileName, contentType);
        	

            contentLength = contentLength + header.getBytes().length + (int) mediaFile.length()  + ("\r\n").getBytes().length;
        	
            multipart = true;
        	media = true;
        }
    }
	
	//adding byte array to the request
	private void addMultipartParameter(String name, String fileName, String contentType, byte[] mediaFileBytes) {
    	
        if ((name != null) && (fileName != null) && (contentType != null)) {
        	
        	header = String.format("--%s\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n", 
        				boundary, name, fileName, contentType);
        	
        	
        	contentLength = contentLength + header.getBytes().length + mediaFileBytes.length  + ("\r\n").getBytes().length; 
        	
        	multipart = true;
        	media = true;
        }
    }
	
	
	private String displayParamsToURL(DisplayParams params) {
		
		char separator = '&';
		String url = new String();

		if (params.getFilters() != null) {
			//char separator = url.contains("?") ? '&' : '?';
			for (String filter : params.getFilters()) {
				url += separator + filter;
				//separator = '&';
			}
		}

		url = addURLParameter(url, "search", params.getSearch());
		if (params.getLocale() != null) {
			url = addURLParameter(url, "locale", params.getLocale() + "");
		}
		url = addURLParameter(url, "offset", params.getOffset());
		url = addURLParameter(url, "limit", params.getLimit());
		url = addURLParameter(url, "excludeFamily", params.getExcludeFamily());

		
		url = addURLParameter(url, "include", params.getIncludes());
		url = addURLParameter(url, "attributes", params.getAttributes());
		url = addURLParameter(url, "stats", params.getStats());
		url = addURLParameter(url, "sort", params.getSort());

		//char separator = url.contains("?") ? '&' : '?';
		if (params.getSortType() != null) {
			for (String s : params.getSortType()) {
				url += separator + s;
				separator = '&';
			}
		}
		
		if (params.getSearchType() != null) {
			for (String s : params.getSearchType()) {
				url += separator + s;
				separator = '&';
			}
		}

		if (params.getLimitType() != null) {
			for (String limit : params.getLimitType()) {
				url += separator + limit;
				separator = '&';
			}
		}
		return url;
	}

	
	private String submissionParamsToURL(SubmissionParams params) {
		
		String url = new String();
		
		if (params.getAction() != null) {
			url = addURLParameter(url, "action", params.getAction().getActionName());
		}
		url = addURLParameter(url, "agreedToTermsAndConditions",
				params.getAgreedToTermsAndConditions());
		url = addURLParameter(url, "campaignId", params.getCampaignId());
		url = addURLParameter(url, "locale", params.getLocale());
		url = addURLParameter(url, "sendEmailAlertWhenPublished",
				params.getSendEmailAlertWhenPublished());
		url = addURLParameter(url, "userEmail", params.getUserEmail());
		url = addURLParameter(url, "userId", params.getUserId());
		url = addURLParameter(url, "userLocation", params.getUserLocation());
		url = addURLParameter(url, "userNickname", params.getUserNickname());
		
		url = addURLParameter(url, "contextDataValue", params.getContextDataValue());
		url = addNthURLParamsFromList(url, "photoCaption", params.getPhotoCaptions());
		url = addNthURLParamsFromList(url, "photoUrl", params.getPhotoUrls());
		url = addNthURLParamsFromList(url, "productRecommendationId",
				params.getProductRecommendationIds());
		url = addNthURLParamsFromList(url, "videoCaption", params.getVideoCaptions());
		url = addNthURLParamsFromList(url, "videoUrl", params.getVideoUrls());
		
		url = addURLParameter(url, "additionalField", params.getAdditionalField());
		
		url = addURLParameter(url, "productId", params.getProductId());
		
		// TODO: test tag_ and tagid_
		url = addURLParameter(url, "tag_", params.getTagForDimensionExternalId());
		url = addURLParameter(url, "tagid_", params.getTagIdForDimensionExternalId());
		
		url = addURLParameter(url, "title", params.getTitle());
		
		url = addURLParameter(url, "categoryId", params.getCategoryId());
		
		url = addURLParameter(url, "isRecommended", params.getRecommended());
		url = addURLParameter(url, "netPromoterComment", params.getNetPromoterComment());
		url = addURLParameter(url, "netPromoterScore", params.getNetPromoterScore());
		url = addURLParameter(url, "rating", params.getRating());
		url = addURLParameter(url, "reviewText", params.getReviewText());
		
		url = addURLParameter(url, "rating", params.getRatingForDimensionExternalId());
		
		url = addURLParameter(url, "isUserAnonymous", params.getIsUserAnonymous());
		url = addURLParameter(url, "questionSummary", params.getQuestionSummary());
		url = addURLParameter(url, "questionDetails", params.getQuestionDetails());
		
		url = addURLParameter(url, "answerText", params.getAnswerText());
		url = addURLParameter(url, "questionId", params.getQuestionId());
		
		url = addURLParameter(url, "sendEmailAlertWhenCommented",
				params.getSendEmailAlertWhenCommented());
		url = addURLParameter(url, "storyText", params.getStoryText());
		
		url = addURLParameter(url, "reviewId", params.getReviewId());
		url = addURLParameter(url, "storyId", params.getStoryId());
		url = addURLParameter(url, "CommentText", params.getCommentText());
		
		url = addURLParameter(url, "contentId", params.getContentId());
		if(params.getContentType() != null){
			url = addURLParameter(url, "contentType", params.getContentType().getTypeString());			
		}
		if(params.getFeedbackType() != null){
			url = addURLParameter(url, "feedbackType", params.getFeedbackType().getTypeString());
		}
		url = addURLParameter(url, "reasonText", params.getReasonText());
		if(params.getVote() != null){
				url = addURLParameter(url, "vote", params.getVote().getTypeString());
		}
		return url;
	}


	private void submissionParamsAddPostParameters(SubmissionParams params) {
		
		if (params.getAction() != null) {
			addMultipartParameter("action", params.getAction().getActionName());
		}
		addMultipartParameter("agreedToTermsAndConditions",
				params.getAgreedToTermsAndConditions());
		addMultipartParameter("campaignId", params.getCampaignId());
		addMultipartParameter("locale", params.getLocale());
		addMultipartParameter("sendEmailAlertWhenPublished",
				params.getSendEmailAlertWhenPublished());
		addMultipartParameter("userEmail", params.getUserEmail());
		addMultipartParameter("userId", params.getUserId());
		addMultipartParameter("userLocation", params.getUserLocation());
		addMultipartParameter("userNickname", params.getUserNickname());
		
		addMultipartParameter("contextDataValue", params.getContextDataValue());
		addNthMultipartParameter("photoCaption", params.getPhotoCaptions());
		addNthMultipartParameter("photoUrl", params.getPhotoUrls());
		addNthMultipartParameter("productRecommendationId",
				params.getProductRecommendationIds());
		addNthMultipartParameter("videoCaption", params.getVideoCaptions());
		addNthMultipartParameter("videoUrl", params.getVideoUrls());
		
		addMultipartParameter("additionalField", params.getAdditionalField());
		
		addMultipartParameter("productId", params.getProductId());
		
		// TODO: test tag_ and tagid_
		addMultipartParameter("tag_", params.getTagForDimensionExternalId());
		addMultipartParameter("tagid_", params.getTagIdForDimensionExternalId());
		
		addMultipartParameter("title", params.getTitle());
		
		addMultipartParameter("categoryId", params.getCategoryId());
		
		addMultipartParameter("isRecommended", params.getRecommended());
		addMultipartParameter("netPromoterComment", params.getNetPromoterComment());
		addMultipartParameter("netPromoterScore", params.getNetPromoterScore());
		addMultipartParameter("rating", params.getRating());
		addMultipartParameter("reviewText", params.getReviewText());
		
		addMultipartParameter("rating", params.getRatingForDimensionExternalId());
		
		addMultipartParameter("isUserAnonymous", params.getIsUserAnonymous());
		addMultipartParameter("questionSummary", params.getQuestionSummary());
		addMultipartParameter("questionDetails", params.getQuestionDetails());
		
		addMultipartParameter("answerText", params.getAnswerText());
		addMultipartParameter("questionId", params.getQuestionId());
		
		addMultipartParameter("sendEmailAlertWhenCommented",
				params.getSendEmailAlertWhenCommented());
		addMultipartParameter("storyText", params.getStoryText());
		
		addMultipartParameter("reviewId", params.getReviewId());
		addMultipartParameter("storyId", params.getStoryId());
		addMultipartParameter("CommentText", params.getCommentText());
		
		addMultipartParameter("contentId", params.getContentId());
		if(params.getContentType() != null){
			addMultipartParameter("contentType", params.getContentType().getTypeString());			
		}
		if(params.getFeedbackType() != null){
			addMultipartParameter("feedbackType", params.getFeedbackType().getTypeString());
		}
		addMultipartParameter("reasonText", params.getReasonText());
		if(params.getVote() != null){
				addMultipartParameter("vote", params.getVote().getTypeString());
		}
		
	}
	
	private String submissionMediaParamsToURL(SubmissionMediaParams params) {
		String url = new String();
		
		if(params.getContentType() != null){
			url = addURLParameter(url, "contentType", params.getContentType().getTypeString());			
		}
		url = addURLParameter(url, "locale", params.getLocale());
		url = addURLParameter(url, "userId", params.getUserId());
		url = addURLParameter(url, "photoUrl", params.getPhotoUrl());
		return url;
	}


	private void submissionMediaParamsAddPostParameters(SubmissionMediaParams params) {
		
		if(params.getContentType() != null){
			addMultipartParameter("contentType", params.getContentType().getTypeString());			
		}
		addMultipartParameter("locale", params.getLocale());
		addMultipartParameter("userId", params.getUserId());
		addMultipartParameter("photoUrl", params.getPhotoUrl());
	}

}
