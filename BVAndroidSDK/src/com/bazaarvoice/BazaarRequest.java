package com.bazaarvoice;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
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
    protected String httpMethod;
    private String param;
    private List<ArrayList<String>> fileParams;
    private String serverResponseMessage = null;
    protected int serverResponseCode;
    protected int contentLength = 0;
    protected Object receivedData;
    private String boundary;
    private boolean multipart = false;
    private String twoHyphens = "--";

	private enum RequestMethod {
		DISPLAY, SUBMIT
	}

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
		
    	fileParams = new ArrayList<ArrayList<String>>();
    	receivedData = null;
    	mediaEntity = null;
    	
    	//make a random boundary for the HTTP requests
    	Random random = new Random();
	    int min = 1;
	    int max = 10000;
    	boundary = Long.valueOf(System.currentTimeMillis()).toString() + (random.nextInt(max - min + 1) + min); 
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
		send(type.getDisplayName(), RequestMethod.DISPLAY, params, listener);
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
		send(type.getSubmissionName(), RequestMethod.SUBMIT, params, listener);
	}

	

	/**
	 * Send a blocking request to the server with a simple string url and
	 * optional byte array.
	 * 
	 * <p><b>Usage:</b><br> This method is wrapped by each of the send/post/queue methods and
	 *        should not be called itself unless needed.
	 * 
	 * @param URL
	 *            the url to send the request/submit to
	 * @param method
	 *            display or submit
	 * @param mediaEntity
	 *            a file to send to the server
	 * @return the JSON result
	 * @throws BazaarException
	 *             on any JSON or communication errors
	 * @throws URISyntaxException 
	 * @throws MalformedURLException 
	 * @throws IOException 
	 */
	public void send(String url, RequestMethod method, BazaarParams params, OnBazaarResponse listener) {
		
		//gets the url to make the post or get request
		if (method == RequestMethod.SUBMIT) {
			this.url = getRequestString(url);
			Log.e(TAG, "request url = " + this.url);
				
			//adds all the parameters after that used to be thrown in with the url
			if (params != null) {
				addPostParameters(params.toURL());
				Log.e(TAG, "param = " + param);
			}
		} else {
			this.url = getTestRequestString(url, params.toURL()); 
		}
		
		//get the media
		this.mediaEntity = (params == null ? null : params.getMedia());
		
		if (this.mediaEntity != null) {
			if (this.mediaEntity.getFile() != null) {
				addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getMimeType(), mediaEntity.getFile(), null);
			} else {
				addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getMimeType(), null, mediaEntity.getBytes());
			}
			
		}
		
		this.listener = listener;
		
		new AsyncTransaction().execute(method == RequestMethod.SUBMIT ? "POST" : "GET"); 
		
		/*
		try {
			// create an HTTP request to a protected resource
			HttpRequestBase httpRequest = method == RequestMethod.SUBMIT ? new HttpPost(
					URL) : new HttpGet(URL);
			// httpRequest.setHeader("Content-Type", "multipart/form-data");
			httpRequest.setHeader(SDK_HEADER_NAME, SDK_HEADER_VALUE);
			if (mediaEntity != null && method == RequestMethod.SUBMIT) {
				reusableClient.getParams().setParameter(
						CoreProtocolPNames.PROTOCOL_VERSION,
						HttpVersion.HTTP_1_1);
				MultipartEntity mpEntity = new MultipartEntity();
				ContentBody body = null;
				if (mediaEntity.getFile() != null) {
					body = new FileBody(mediaEntity.getFile(), mediaEntity.getFilename(), mediaEntity.getMimeType(), "UTF-8");
				} else {
					body = new ByteArrayBody(mediaEntity.getBytes(), mediaEntity.getFilename());
				}
				mpEntity.addPart(mediaEntity.getName(), body);
				((HttpPost) httpRequest).setEntity(mpEntity);
			}

			HttpClient httpClient = reusableClient;

			HttpResponse response = httpClient.execute(httpRequest);
			StatusLine statusLine = response.getStatusLine();
			int status = statusLine.getStatusCode();

			if (status < 200 || status > 299) {
				throw new BazaarException("Error communicating with server. "
						+ statusLine.getReasonPhrase() + " " + status);
			} else {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				return new JSONObject(result);
			}
		} catch (Exception e) {
			throw new BazaarException("Error handling results from server!", e);
		}
		*/
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
	private URL getRequestString(final String type) {
		//build url xxxx.ugc.bazaarvoice.com/data/xxx.json
		String requestString = requestUrl + type + ".json";
		//add api version
		requestString = requestString + "?" + "apiversion=" + apiVersion;
		//add API Key
		requestString = requestString + "&" + "passkey=" + passKey;
		URI uri = null;
		try {
			uri = new URI(requestString.replace(" ", "%20"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URL url = null;
		try {
			url = new URL(uri.toASCIIString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return url;
	}
	
	//TODO delete
	private URL getTestRequestString(final String type, String params) {
		//build url xxxx.ugc.bazaarvoice.com/data/xxx.json
		String requestString = requestUrl + type + ".json";
		//add api version
		requestString = requestString + "?" + "apiversion=" + apiVersion;
		//add API Key
		requestString = requestString + "&" + "passkey=" + passKey;
		requestString = requestString + params;
		URI uri = null;
		try {
			uri = new URI(requestString.replace(" ", "%20"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URL url = null;
		try {
			url = new URL(uri.toASCIIString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return url;
	}
	
	
	public void addPostParameters(String params) {
   	 if (params != null) {
   		 param = params;
   		 //remove the first &
		 param = param.substring(1);
		 //TODO figure out this
   		 contentLength = contentLength + params.getBytes().length - 1;
    	}
    }

    
    public void addMultipartParameter(String name, String fileName, String contentType, File mediaFile, byte[] mediaFileBytes) {
    	
        if ((name != null) && (fileName != null) && (contentType != null)) {
        	
        	ArrayList<String> item = new ArrayList<String>();
        	
        	String header = String.format("--%s\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n", 
        				boundary, name, fileName, contentType);
        	
        	item.add(header);
        	item.add(fileName);
        	fileParams.add(item);
        	
        	if (mediaFile != null) {
        		contentLength = contentLength + header.getBytes().length + (int) mediaFile.length()  + ("\r\n").getBytes().length;
        	} else {
        		contentLength = contentLength + header.getBytes().length + mediaFileBytes.length  + ("\r\n").getBytes().length;
        	}
        	
        	multipart = true;
        }
    }
    
    @SuppressLint("NewApi")
	private class AsyncTransaction extends AsyncTask<String, Integer, String> {
		
		@Override
		protected String doInBackground(String... args) {
			
			String httpMethod = args[0];
			//String httpMethod = "POST";
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
			
		    if (multipart) {
		    	connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		    }
			
		    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		    
			//buffer for file transfers
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1*1024*1024;
			
			if (multipart) {
				Log.e(TAG, "multipart = " + true);
				
				if (fileParams.size() > 0) {
					
					for (int i = 0; i < fileParams.size(); i++) {
						ArrayList<String> part = fileParams.get(i);
					
						outputStream.writeBytes(part.get(0));		
						
						//File I/O
						if (mediaEntity.getFile() != null) {
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
							
							fileInputStream.close();
						} else {
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
							
							fileInputStream.close();
						}
						
						outputStream.writeBytes("\r\n");
					
					}
				}
					
				outputStream.writeBytes(twoHyphens + boundary + twoHyphens + "\r\n");				
			
			} else {
				Log.e(TAG, "sending out param : " + param);
				outputStream.writeBytes(param);
			}
			
			outputStream.flush();
			outputStream.close();	
		}		
	}

}
