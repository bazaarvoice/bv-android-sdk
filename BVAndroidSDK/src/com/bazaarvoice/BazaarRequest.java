package com.bazaarvoice;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    	setBoundary(Long.valueOf(System.currentTimeMillis()).toString() + (random.nextInt(max - min + 1) + min)); 
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
			params.addPostParameters(this);
			//get the media
			this.mediaEntity = params.getMedia();
		} else {
			this.mediaEntity = null;
		}
		
		if (this.mediaEntity != null) {
			if (this.mediaEntity.getFile() != null) {
				addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getMimeType(), mediaEntity.getFile(), null);
			} else {
				Log.e(TAG, "mediaEntity.getName() = " + mediaEntity.getName());
				Log.e(TAG, "mediaEntity.getFilename() = " + mediaEntity.getFilename());
				Log.e(TAG, "mediaEntity.getMimeType() = " + mediaEntity.getMimeType());
				addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getMimeType(), null, mediaEntity.getBytes());
			}
			
		}
		
		this.listener = listener;
		
		new AsyncTransaction().execute("POST"); 
	}


	/**
	 * Get the request url as a string.
	 * 
	 * @param type
	 *            the request type
	 * @param params
	 *            the parameters of the request
	 * @return the request url
	 * @throws URISyntaxException 
	 * @throws MalformedURLException 
	 */
	private URL getRequestString(final String type, BazaarParams params) {
		//build url xxxx.ugc.bazaarvoice.com/data/xxx.json
		String requestString = requestUrl + type + ".json?" + "apiversion=" + apiVersion + "&" + "passkey=" + passKey;

		if (params != null) {
			requestString = requestString + params.toURL();
		}
		
		URL url = null;
		try {
			url = new URL(requestString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return url;
	}	
	
	
	public void addMultipartParameter(String name, String value) {
    	
        if ((name != null) && (value != null)) {
        	ArrayList<String> item = new ArrayList<String>();
        	String header = String.format("--%s\r\nContent-Disposition: form-data; name=\"%s\"\r\n\r\n", getBoundary(), name); 
        	String body = String.format("%s\r\n", value);		

        	item.add(header);
        	item.add(body);
        	requestParams.add(item);
        	
        	contentLength = contentLength + header.getBytes().length + body.getBytes().length;
       
        	multipart = true;
        }
    }

    
    public void addMultipartParameter(String name, String fileName, String contentType, File mediaFile, byte[] mediaFileBytes) {
    	
        if ((name != null) && (fileName != null) && (contentType != null)) {
        	
        	header = String.format("--%s\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n", 
        				getBoundary(), name, fileName, contentType);
        	
        	if (mediaFile != null) {
        			contentLength = contentLength + header.getBytes().length + (int) mediaFile.length()  + ("\r\n").getBytes().length;
        	} else {
        			contentLength = contentLength + header.getBytes().length + mediaFileBytes.length  + ("\r\n").getBytes().length;
        	} 
        	
        	multipart = true;
        }
    }
    
    public String getBoundary() {
		return boundary;
	}

	public void setBoundary(String boundary) {
		this.boundary = boundary;
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
						contentLength = contentLength + (twoHyphens + getBoundary() + twoHyphens + "\r\n").getBytes().length;
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
				//connection.setChunkedStreamingMode(0);
				
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
			
		    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + getBoundary());
			
		    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		    
			//buffer for file transfers
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1*1024*1024;
			
			Log.e(TAG, "multipart = " + true);
			Log.e(TAG, "requestParams = " + requestParams);
			
			for (ArrayList<String> part : requestParams) {
				outputStream.writeBytes(part.get(0));
				outputStream.writeBytes(part.get(1));
			}
					
			Log.e(TAG, "header = " + header);
			
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
				
			outputStream.writeBytes(twoHyphens + getBoundary() + twoHyphens + "\r\n");				
			
			outputStream.flush();
			outputStream.close();	
		}		
	}

}
