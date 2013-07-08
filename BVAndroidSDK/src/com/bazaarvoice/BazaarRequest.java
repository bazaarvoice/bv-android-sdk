package com.bazaarvoice;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

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
	
	//private static final String TAG = "BazaarRequest";
	
	private final String SDK_HEADER_NAME = "X-UA-BV-SDK";
	private final String SDK_HEADER_VALUE = "ANDROID_SDK_V202";
	public static final int DEFAULT_READ_TIMEOUT = 10000;
	public static final int DEFAULT_CONNECT_TIMEOUT = 15000;

	private String passKey;
	private String apiVersion;
	private String requestUrl;
	
	private Media mediaEntity;
	private OnBazaarResponse listener;
	
	private HttpURLConnection connection;
    protected URL url;
    protected String httpMethod;
    private String paramString;
    private String serverResponseMessage = null;
    protected int serverResponseCode;
    protected int contentLength = 0;
    protected Object receivedData;
    private String boundary;
    private boolean multipart = false;
    private boolean media = false;
    private ByteArrayOutputStream out;


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
		
		out = new ByteArrayOutputStream();
		
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
		
		//build url xxxx.ugc.bazaarvoice.com/data/xxx.json
		String requestString = requestUrl + type.getDisplayName() + ".json";
		
		//add API key and version
		requestString = requestString + "?" + "apiversion=" + apiVersion + "&" + "passkey=" + passKey;
		
		//if any, add params to request string
		if (params != null) {
			requestString = requestString + displayParamsToURL((DisplayParams) params);
		}
		
		this.url = null;
		try {
			url = new URL(requestString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
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

		String requestString = requestUrl + type.getSubmissionName() + ".json";
		
		try {
			this.url = new URL(requestString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	

		if (params != null) {
			this.mediaEntity = params.getMedia();
			
			if (this.mediaEntity != null) {
				submissionMediaParamsAddPostParameters((SubmissionMediaParams) params);
				if (this.mediaEntity.getFile() != null) {
					try {
						addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getMimeType(), mediaEntity.getFile());
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getMimeType(), mediaEntity.getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else { 
				if (params.getClass().equals(SubmissionMediaParams.class)) {
					paramString = submissionMediaParamsToURL((SubmissionMediaParams) params);
				} else { 
					paramString = submissionParamsToURL((SubmissionParams) params);
				}
			}
		} 

		this.listener = listener;
		
		new AsyncTransaction().execute("POST"); 
	}


	@SuppressLint("NewApi")
	private class AsyncTransaction extends AsyncTask<String, Integer, String> {
		
		@Override
		protected String doInBackground(String... args) {
			
			String httpMethod = args[0];
			
			try {
				System.setProperty("http.keepAlive", "false");
				connection = (HttpURLConnection) url.openConnection();
	            
				// Allow Inputs & Outputs
				connection.setRequestMethod(httpMethod);
				connection.setDoInput(true);
				connection.setUseCaches(false);		
				
				if (httpMethod.equals("POST")) {
					connection.setDoOutput(true);
					
					if (multipart) {						
						contentLength = (int) getContentLength();						
					} else {						
						contentLength = paramString.getBytes().length;					
					}
					
					if (contentLength != 0) {
						connection.setRequestProperty("Content-length", (Integer.valueOf(contentLength).toString()));
					}
					
					connection.setFixedLengthStreamingMode(contentLength);
				}
	            			
				//Headers
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Accept", "application/xml");	
				connection.setRequestProperty(SDK_HEADER_NAME, SDK_HEADER_VALUE);
				connection.setReadTimeout(DEFAULT_READ_TIMEOUT);
				connection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
				
				if ( httpMethod.equals("POST")) {
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
						+ result + " Error :  " + serverResponseCode));
			} else {
				try {
					listener.onResponse(new JSONObject(result));
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
			
			InputStream is;
			
			//if we are sending a picture of video
			if (media) {
				//change the content type to multipart
				connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);		
				
				is = new ByteArrayInputStream(out.toByteArray());				 
				
			} else {
				
				is = new ByteArrayInputStream(paramString.getBytes());
				
			}
			
			final OutputStream os = connection.getOutputStream();
			
			final BufferedInputStream bis = new BufferedInputStream(is);
			int read = 0;
			final byte[] buffer = new byte[8192];
			while (true) {
				
				read = bis.read(buffer);
				if (read == -1) {
					break;
				}
				os.write(buffer, 0, read);
			}

			try {
				os.close();
			} catch (final IOException e) {}

			try {
				bis.close();
			} catch (final IOException e) {}

			try {
				is.close();
			} catch (final IOException e) {}
		}		
	}

	private long getContentLength() throws IOException {
		writeLastBoundary();
		return out.toByteArray().length;
	}

	private void writeLastBoundary() throws IOException {
		out.write(("--" + boundary + "--\r\n").getBytes());
	}
	
	private void addMultipartParameter(String name, Object val) {
		if (name != null && val != null) {
			String value = val.toString();

			try {
				out.write(("--" + boundary + "\r\n").getBytes());
	        	out.write((String.format("Content-Disposition: form-data; name=\"%s\"\r\n\r\n", name)).getBytes()); 
	        	out.write((String.format("%s\r\n", value)).getBytes());	
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        	multipart = true;    
		}
    }
	
	//adding a file object to request
	private void addMultipartParameter(String name, String fileName, String contentType, File mediaFile) throws IOException {
    	
        if ((name != null) && (fileName != null) && (contentType != null)) {

        	InputStream fin = new FileInputStream(mediaFile);
        	
        	try {
        		out.write(("--" + boundary + "\r\n").getBytes());
        		out.write((String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n", 
        				name, fileName, contentType)).getBytes());
        		
        		final byte[] tmp = new byte[4096];
    			int l = 0;
    			while ((l = fin.read(tmp)) != -1) {
    				out.write(tmp, 0, l);
    			}
    			
    			out.write(("\r\n").getBytes());
    			out.flush();
    		} catch (final IOException e) {
    			throw e;
    		} finally {
    			fin.close();
    		}
        	
            multipart = true;
        	media = true;
        }
    }
	
	//adding byte array to the request
	private void addMultipartParameter(String name, String fileName, String contentType, byte[] mediaFileBytes) throws IOException {
    	
        if ((name != null) && (fileName != null) && (contentType != null)) {
        	
        	try {
        		out.write(("--" + boundary + "\r\n").getBytes());
        		out.write((String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n", 
        				name, fileName, contentType)).getBytes());
        		
        		out.write(mediaFileBytes);
    			
    			out.write(("\r\n").getBytes());
    			out.flush();
    		} catch (final IOException e) {
    			throw e;
    		}
        	
        	multipart = true;
        	media = true;
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
		
		url = addURLParameter(url, "apiversion", apiVersion);
		url = addURLParameter(url, "passkey", passKey);
		
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
	
	private String submissionMediaParamsToURL(SubmissionMediaParams params) {
		String url = new String();
		
		if(params.getContentType() != null){
		url = addURLParameter(url, "contentType", params.getContentType().getTypeString());			
		}
		url = addURLParameter(url, "apiversion", apiVersion);
		url = addURLParameter(url, "passkey", passKey);
		url = addURLParameter(url, "locale", params.getLocale());
		url = addURLParameter(url, "userId", params.getUserId());
		url = addURLParameter(url, "photoUrl", params.getPhotoUrl());
		return url;
	}
	

	private void submissionMediaParamsAddPostParameters(SubmissionMediaParams params) {
		
		if(params.getContentType() != null){
			addMultipartParameter("contentType", params.getContentType().getTypeString());			
		}
		addMultipartParameter("apiversion", apiVersion);
		addMultipartParameter("passkey", passKey);
		addMultipartParameter("locale", params.getLocale());
		addMultipartParameter("userId", params.getUserId());
		addMultipartParameter("photoUrl", params.getPhotoUrl());
	}

}
