package com.bazaarvoice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

	private String passKey;
	private String apiVersion;
	private String requestUrl;
	
	private Media mediaEntity;
	private OnBazaarResponse listener;
	
	private HttpURLConnection connection;
    protected URL url;
    protected String httpMethod;
    protected int serverResponseCode;
    private String serverResponseMessage = null;
    private String paramString;
    private ArrayList<String> multiPartParams;
    private ArrayList<String> mediaParam;
    protected int contentLength = 0;
    protected Object receivedData;
    private String boundary;
    private boolean multipart = false;
    private boolean media = false;


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
		
		multiPartParams = new ArrayList<String>();
		mediaParam = new ArrayList<String>();
		
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

					addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getMimeType(), mediaEntity.getFile());
	
				} else {

					addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getMimeType(), mediaEntity.getBytes());

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
						contentLength = getContentLength();						
					} else {						
						contentLength = paramString.getBytes().length;					
					}
					
					if (contentLength != 0) {
						connection.setRequestProperty("Content-length", (Integer.valueOf(contentLength).toString()));
						connection.setFixedLengthStreamingMode(contentLength);
					}					
				}
	            			
				//Headers
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Accept", "application/xml");	
				connection.setRequestProperty(SDK_HEADER_NAME, SDK_HEADER_VALUE);
				
				if ( httpMethod.equals("POST")) {
					writeToServer(connection);
				}
					
				serverResponseMessage = readResponse(new BufferedInputStream(connection.getInputStream()));									
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
			
			connection.disconnect();
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
			
			OutputStream out;
			
			if (media) {
				
				//change the content type to multipart
				connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
				
				out = new BufferedOutputStream(connection.getOutputStream());
				
				
				for (String param : multiPartParams) {
					out.write(param.getBytes());
				}
				multiPartParams.clear();
				
				
				//buffer for file transfers
				int bytesRead, bytesAvailable, bufferSize;
				byte[] buffer;
				int maxBufferSize = 1*1024*1024;
									
				out.write(mediaParam.get(0).getBytes());
				out.write(mediaParam.get(1).getBytes());
				
				if (mediaEntity.getFile() != null) {
					
					FileInputStream fileInputStream = new FileInputStream(mediaEntity.getFile());
					
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];
					
					// Read file
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					
					while (bytesRead > 0)
					{
						out.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}
					
					fileInputStream.close();
					
				} else {				
					InputStream fileInputStream = new ByteArrayInputStream(mediaEntity.getBytes());
							
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];
					
					// Read file
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					
					while (bytesRead > 0)
					{
						out.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}

					fileInputStream.close();	
				}
				
				out.write(mediaParam.get(2).getBytes());
				out.write(mediaParam.get(3).getBytes());
				
				
			} else {	
				out = new BufferedOutputStream(connection.getOutputStream());
				out.write(paramString.getBytes());
			}
			
			out.flush();
			out.close();
					
		}		
	}

	private int getContentLength() throws IOException {
		writeLastBoundary();
		return contentLength;
	}

	private void writeLastBoundary() throws IOException {
		//out.write(("--" + boundary + "--\r\n").getBytes());
		if (media) {
			mediaParam.add("--" + boundary + "--\r\n");
		} else {
			multiPartParams.add("--" + boundary + "--\r\n");
		}
		contentLength = contentLength + ("--" + boundary + "--\r\n").getBytes().length;
		
	}
	
	private void addMultipartParameter(String name, Object val) {
		if (name != null && val != null) {
			String value = val.toString();
			
			String topBoundary = "--" + boundary + "\r\n";
			String contentDisp = String.format("Content-Disposition: form-data; name=\"%s\"\r\n\r\n", name);
			String valueParam = String.format("%s\r\n", value);
			
			multiPartParams.add(topBoundary);
			multiPartParams.add(contentDisp);
			multiPartParams.add(valueParam);
			
			contentLength = contentLength + topBoundary.getBytes().length + contentDisp.getBytes().length + valueParam.getBytes().length;
        	
        	multipart = true;    
		}
    }
	
	//adding a file object to request
	private void addMultipartParameter(String name, String fileName, String contentType, File mediaFile) {
    	
        if ((name != null) && (fileName != null) && (contentType != null)) {
        	
        	String topBoundary = "--" + boundary + "\r\n";
			String contentDisp = String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n", 
    				name, fileName, contentType);
			String valueParam = "\r\n";
			
			mediaParam.add(topBoundary);
			mediaParam.add(contentDisp);
			mediaParam.add(valueParam);			
			
			contentLength = contentLength + topBoundary.getBytes().length + contentDisp.getBytes().length + valueParam.getBytes().length + (int) mediaFile.length();
        	
            multipart = true;
        	media = true;
        }
    }
	
	//adding byte array to the request
	private void addMultipartParameter(String name, String fileName, String contentType, byte[] mediaFileBytes) {
    	
        if ((name != null) && (fileName != null) && (contentType != null)) {
        	
        	String topBoundary = "--" + boundary + "\r\n";
			String contentDisp = String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n", 
    				name, fileName, contentType);
			String valueParam = "\r\n";
			
			mediaParam.add(topBoundary);
			mediaParam.add(contentDisp);
			mediaParam.add(valueParam);			
			
			contentLength = contentLength + topBoundary.getBytes().length + contentDisp.getBytes().length + valueParam.getBytes().length + mediaFileBytes.length;
        	
        	multipart = true;
        	media = true;
        }
    }
	
	
	private String addURLParameter(String name, Object val) {
		if (val != null) {
			String value = val.toString();
			
			if (value.length() > 0) {
				value = BazaarParams.encode(value);
				return "&" + name + '=' + value;
			}
		}
		return "";
	}	
	
	
	private String addURLParameter(String name, Map<String, String> map) {
		StringBuilder url = new StringBuilder();
		if (map != null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				url.append(addURLParameter(name + "_" + key, value));
			}
		}
		return url.toString();
	}
	
	
	private String addURLParameter(String name, List<String> values) {
		if (values != null) {
			StringBuilder paramList = new StringBuilder();
			boolean first = true;
			for (String value : values) {
				if (first) {
					first = false;
				} else {
					paramList.append(",");
				}

				paramList.append(value);
			}
			return addURLParameter(name, paramList);
		}
		return "";
	}

	
	private String addNthURLParamsFromList(String name, List<String> values) {
		StringBuilder url = new StringBuilder();
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				// Start with param_1
				int key = i + 1;
				String value = values.get(i);

				url.append(addURLParameter(name + "_" + key, value));
			}
		}
		return url.toString();
	}
	
	
	private String displayParamsToURL(DisplayParams params) {
		
		char separator = '&';
		StringBuilder url = new StringBuilder();

		if (params.getFilters() != null) {
			for (String filter : params.getFilters()) {
				url.append(separator);
				url.append(filter);
			}
		}

		url.append(addURLParameter("search", params.getSearch()));
		if (params.getLocale() != null) {
			url.append(addURLParameter("locale", params.getLocale()));
		}
		url.append(addURLParameter("offset", params.getOffset()));
		url.append(addURLParameter("limit", params.getLimit()));
		url.append(addURLParameter("excludeFamily", params.getExcludeFamily()));

		
		url.append(addURLParameter("include", params.getIncludes()));
		url.append(addURLParameter("attributes", params.getAttributes()));
		url.append(addURLParameter("stats", params.getStats()));
		url.append(addURLParameter("sort", params.getSort()));

		//char separator = url.contains("?") ? '&' : '?';
		if (params.getSortType() != null) {
			for (String s : params.getSortType()) {
				url.append(separator + s);
			}
		}
		
		if (params.getSearchType() != null) {
			for (String s : params.getSearchType()) {
				url.append(separator + s);
			}
		}

		if (params.getLimitType() != null) {
			for (String limit : params.getLimitType()) {
				url.append(separator + limit);
			}
		}
		return url.toString();
	}

	
	private String submissionParamsToURL(SubmissionParams params) {
		
		StringBuilder url = new StringBuilder();
		
		url.append(addURLParameter("apiversion", apiVersion));
		url.append(addURLParameter("passkey", passKey));
		
		if (params.getAction() != null) {
			url.append(addURLParameter("action", params.getAction().getActionName()));
		}
		url.append(addURLParameter("agreedToTermsAndConditions",
				params.getAgreedToTermsAndConditions()));
		url.append(addURLParameter("campaignId", params.getCampaignId()));
		url.append(addURLParameter("locale", params.getLocale()));
		url.append(addURLParameter("sendEmailAlertWhenPublished",
				params.getSendEmailAlertWhenPublished()));
		url.append(addURLParameter("userEmail", params.getUserEmail()));
		url.append(addURLParameter("userId", params.getUserId()));
		url.append(addURLParameter("userLocation", params.getUserLocation()));
		url.append(addURLParameter("userNickname", params.getUserNickname()));
		
		url.append(addURLParameter("contextDataValue", params.getContextDataValue()));
		url.append(addNthURLParamsFromList("photoCaption", params.getPhotoCaptions()));
		url.append(addNthURLParamsFromList("photoUrl", params.getPhotoUrls()));
		url.append(addNthURLParamsFromList("productRecommendationId",
				params.getProductRecommendationIds()));
		url.append(addNthURLParamsFromList("videoCaption", params.getVideoCaptions()));
		url.append(addNthURLParamsFromList("videoUrl", params.getVideoUrls()));
		
		url.append(addURLParameter("additionalField", params.getAdditionalField()));
		
		url.append(addURLParameter("productId", params.getProductId()));
		
		url.append(addURLParameter("tag", params.getTagForDimensionExternalId()));
		url.append(addURLParameter("tagid", params.getTagIdForDimensionExternalId()));
		
		url.append(addURLParameter("title", params.getTitle()));
		
		url.append(addURLParameter("categoryId", params.getCategoryId()));
		
		url.append(addURLParameter("isRecommended", params.getRecommended()));
		url.append(addURLParameter("netPromoterComment", params.getNetPromoterComment()));
		url.append(addURLParameter("netPromoterScore", params.getNetPromoterScore()));
		url.append(addURLParameter("rating", params.getRating()));
		url.append(addURLParameter("reviewText", params.getReviewText()));
		
		url.append(addURLParameter("rating", params.getRatingForDimensionExternalId()));
		
		url.append(addURLParameter("isUserAnonymous", params.getIsUserAnonymous()));
		url.append(addURLParameter("questionSummary", params.getQuestionSummary()));
		url.append(addURLParameter("questionDetails", params.getQuestionDetails()));
		
		url.append(addURLParameter("answerText", params.getAnswerText()));
		url.append(addURLParameter("questionId", params.getQuestionId()));
		
		url.append(addURLParameter("sendEmailAlertWhenCommented",
				params.getSendEmailAlertWhenCommented()));
		url.append(addURLParameter("storyText", params.getStoryText()));
		
		url.append(addURLParameter("reviewId", params.getReviewId()));
		url.append(addURLParameter("storyId", params.getStoryId()));
		url.append(addURLParameter("CommentText", params.getCommentText()));
		
		url.append(addURLParameter("contentId", params.getContentId()));
		if(params.getContentType() != null){
			url.append(addURLParameter("contentType", params.getContentType().getTypeString()));			
		}
		if(params.getFeedbackType() != null){
			url.append(addURLParameter("feedbackType", params.getFeedbackType().getTypeString()));
		}
		url.append(addURLParameter("reasonText", params.getReasonText()));
		if(params.getVote() != null){
			url.append(addURLParameter("vote", params.getVote().getTypeString()));
		}
		return url.toString();
	}
	
	private String submissionMediaParamsToURL(SubmissionMediaParams params) {
		StringBuilder url = new StringBuilder();
		
		if(params.getContentType() != null){
			url.append(addURLParameter("contentType", params.getContentType().getTypeString()));			
		}
		url.append(addURLParameter("apiversion", apiVersion));
		url.append(addURLParameter("passkey", passKey));
		url.append(addURLParameter("locale", params.getLocale()));
		url.append(addURLParameter("userId", params.getUserId()));
		url.append(addURLParameter("photoUrl", params.getPhotoUrl()));
		return url.toString();
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
