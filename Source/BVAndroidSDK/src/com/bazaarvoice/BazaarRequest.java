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
package com.bazaarvoice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
 */
public class BazaarRequest {
	
	//private static final String TAG = "BazaarRequest";
	
	private final String SDK_HEADER_NAME = "X-UA-BV-SDK";
	private final String SDK_HEADER_VALUE = "ANDROID_SDK_V203";

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
    	protected ArrayList<String> multiPartParams;
    	protected ArrayList<String> mediaParam;
    	protected int contentLength = 0;
    	protected Object receivedData;
    	protected String boundary;
    	protected boolean multipart = false;
    	protected boolean media = false;


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
			requestString = requestString + params.toURL(apiVersion, passKey);
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
				params.addPostParameters(apiVersion, passKey, this);
				if (this.mediaEntity.getFile() != null) {

					params.addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getFile(), this);
	
				} else {

					params.addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getBytes(), this);

				}
			} else { 
				paramString = params.toURL(apiVersion, passKey);
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
				connection = (HttpURLConnection) url.openConnection();
	            
				// Allow Inputs
				connection.setRequestMethod(httpMethod);
				connection.setDoInput(true);
				connection.setUseCaches(false);		
				
				if (httpMethod.equals("POST")) {
				    //allows outputs
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
									
				// stream the top boundary
				out.write(mediaParam.get(0).getBytes());
				// stream content disposition, key name, filename
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
				
				//stream \r\n
				out.write(mediaParam.get(2).getBytes());
				//stream boundary
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

}
