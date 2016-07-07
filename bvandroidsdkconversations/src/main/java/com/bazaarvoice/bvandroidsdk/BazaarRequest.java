/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.bazaarvoice.bvandroidsdk.types.ApiVersion;
import com.bazaarvoice.bvandroidsdk.types.RequestType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
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
    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";

    private String clientName;
    private BazaarEnvironment environment;
    private String apiKeyConversations;
    private String apiVersion;
    protected String requestUrl;

    private Media mediaEntity;
    private OnBazaarResponse listener;

    private HttpURLConnection connection;
    protected URL url;
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
     * <p> DEPRECATED: In favor of the {@link BVConversationsClient}</p>
     * Initialize the request
     */
    @Deprecated
    public BazaarRequest() {
        this(BVSDK.getInstance().getApiKeyConversations(), ApiVersion.FIVE_FOUR);
    }

    /**
     * <p> DEPRECATED: In favor of the {@link BVConversationsClient}</p>
     * <p> Initialize the request with the necessary parameters. </p>
     *
     * @param apiKeyConversations Bazaarvoice Conversations API Key
     * @param apiVersion Bazaarvoice Conversations API Version
     */
    @Deprecated
    public BazaarRequest(String apiKeyConversations, ApiVersion apiVersion) {
        if (apiKeyConversations == null || apiKeyConversations.isEmpty()) {
            throw new IllegalStateException("BV Conversations SDK requires a conversations api key");
        }

        BVSDK bvsdk = BVSDK.getInstance();
        this.environment = bvsdk.getEnvironment();
        this.clientName = bvsdk.getClientId();

        if(this.environment == BazaarEnvironment.PRODUCTION)
            this.requestUrl = "https://api.bazaarvoice.com/data/";
        else
            this.requestUrl = "https://stg.api.bazaarvoice.com/data/";

        this.apiKeyConversations = apiKeyConversations;
        this.apiVersion = apiVersion.getVersionName();

        multiPartParams = new ArrayList<String>();
        mediaParam = new ArrayList<String>();

        receivedData = null;
        mediaEntity = null;
    }

    private String getBoundary()
    {
        if (boundary == null)
        {
            //make a random boundary for the HTTP requests
            Random random = new Random();
            int min = 1;
            int max = 10000;
            boundary = (Long.valueOf(System.currentTimeMillis()).toString() + (random.nextInt(max - min + 1) + min));
        }

        return boundary;
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
     */
    public void sendDisplayRequest(final RequestType type, DisplayParams params, final OnBazaarResponse listener) {

        String requestString = getSendDisplayUrlString(type, params);

        Logger.d("Started Load", requestString);
        this.url = null;
        try {
            url = new URL(requestString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onException("Invalid display request url", e);
            return;
        }

        this.listener = listener;

        RequestData requestData = new RequestData(HTTP_METHOD_GET, url, type, params);
        new AsyncTransaction().execute(requestData);
    }

    protected String getSendDisplayUrlString(final RequestType type, DisplayParams params)
    {
        //build url [stg.]api.bazaarvoice.com/data/xxx.json
        //
        String requestString = requestUrl + type.getDisplayName() + ".json";

        //add API key and version
        //
        requestString = requestString + "?" + "apiversion=" + apiVersion + "&" + "passkey=" + apiKeyConversations;

        //if any, add params to request string
        //
        if (params != null) {
            requestString = requestString + params.toURL(apiVersion, apiKeyConversations);
        }

        return requestString;
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
     */
    public void postSubmission(final RequestType type, BazaarParams params, final OnBazaarResponse listener) {

        String requestString = requestUrl + type.getSubmissionName() + ".json";

        try {
            this.url = new URL(requestString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onException("Invalid submission request url", e);
            return;
        }

        boolean isFingerprintUsed = false;
        if (params instanceof SubmissionParams) {
            SubmissionParams submissionParams = (SubmissionParams) params;
            String fingerprint = submissionParams.getFingerprint();
            isFingerprintUsed = fingerprint != null && !fingerprint.isEmpty();
        }

        if (params != null) {
            this.mediaEntity = params.getMedia();

            if (this.mediaEntity != null) {
                params.addPostParameters(apiVersion, apiKeyConversations, this);
                if (this.mediaEntity.getFile() != null) {

                    params.addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getFile(), this);

                } else {

                    params.addMultipartParameter(mediaEntity.getName(), mediaEntity.getFilename(), mediaEntity.getBytes(), this);

                }
            } else {
                paramString = params.toURL(apiVersion, apiKeyConversations);
            }
        }

        this.listener = listener;

        RequestData requestData = new RequestData(HTTP_METHOD_POST, url, type, params);
        new AsyncTransaction().execute(requestData);
    }

    private static final class RequestData {
        private String httpMethod;
        private URL url;
        private RequestType requestType;
        private BazaarParams bazaarParams;

        public RequestData(String httpMethod, URL url, RequestType requestType, BazaarParams bazaarParams) {
            this.httpMethod = httpMethod;
            this.url = url;
            this.requestType = requestType;
            this.bazaarParams = bazaarParams;
        }

        public String getHttpMethod() {
            return httpMethod;
        }

        public URL getUrl() {
            return url;
        }

        public RequestType getRequestType() {
            return requestType;
        }

        public BazaarParams getBazaarParams() {
            return bazaarParams;
        }
    }

    private static final class ResponseData {
        private boolean didSucceed;
        private String errorMessage;
        private Throwable errorThrowable;
        private String successUrl;
        private JSONObject successJsonObject;
        private RequestType requestType;
        private String httpMethod;
        private BazaarParams bazaarParams;

        public ResponseData(boolean didSucceed, String errorMessage, Throwable errorThrowable, String successUrl, JSONObject successJsonObject, RequestType requestType, String httpMethod, BazaarParams bazaarParams) {
            this.didSucceed = didSucceed;
            this.errorMessage = errorMessage;
            this.errorThrowable = errorThrowable;
            this.successUrl = successUrl;
            this.successJsonObject = successJsonObject;
            this.requestType = requestType;
            this.httpMethod = httpMethod;
            this.bazaarParams = bazaarParams;
        }

        public boolean isDidSucceed() {
            return didSucceed;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public Throwable getErrorThrowable() {
            return errorThrowable;
        }

        public String getSuccessUrl() {
            return successUrl;
        }

        public JSONObject getSuccessJsonObject() {
            return successJsonObject;
        }

        public RequestType getRequestType() {
            return requestType;
        }

        public String getHttpMethod() {
            return httpMethod;
        }

        public BazaarParams getBazaarParams() {
            return bazaarParams;
        }
    }

    @SuppressLint("NewApi")
    private class AsyncTransaction extends AsyncTask<RequestData, Integer, ResponseData> {

        @Override
        protected ResponseData doInBackground(RequestData... args) {

            RequestData requestData = args[0];
            String httpMethod = requestData.getHttpMethod();
            String errorMessage = null;
            JSONObject successJsonObject = null;
            String successUrl = url.toString();
            boolean didSucceed = false;
            Throwable errorThrowable = null;
            RequestType requestType = requestData.getRequestType();

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

                if ( httpMethod.equals("POST")) {
                    writeToServer(connection);
                }

                serverResponseMessage = readResponse(connection);
                serverResponseCode = connection.getResponseCode();

                if (serverResponseCode < 200 || serverResponseCode > 299) {
                    didSucceed = false;
                    errorMessage = "Error reading JSON response.";
                    errorThrowable = new BazaarException("Message : "+ serverResponseMessage + " Error :  " + serverResponseCode);
                } else {
                    try {
                        didSucceed = true;
                        successJsonObject = new JSONObject(serverResponseMessage);
                    } catch (JSONException e) {
                        didSucceed = false;
                        errorMessage = "Error reading JSON response.";
                        errorThrowable = e;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                didSucceed = false;
                errorMessage = "There was an error in the network connection";
                errorThrowable = e;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return new ResponseData(didSucceed, errorMessage, errorThrowable, successUrl, successJsonObject, requestType, httpMethod, requestData.getBazaarParams());
        }

        //Process after transaction is complete
        @Override
        protected void onPostExecute(ResponseData responseData) {
            if (responseData.isDidSucceed()) {
                listener.onResponse(responseData.getSuccessUrl(), responseData.getSuccessJsonObject());

                if (responseData.getHttpMethod().equals(HTTP_METHOD_GET)) {
                    ConversationsAnalyticsManager.sendDisplayAnalyticsEvent(responseData.getRequestType(), responseData.getSuccessJsonObject());
                } else if (responseData.getHttpMethod().equals(HTTP_METHOD_POST)) {
                    ConversationsAnalyticsManager.sendSubmissionAnalyticsEvent(responseData.getRequestType(), responseData.getBazaarParams());
                }
            } else {
                listener.onException(responseData.getErrorMessage(), responseData.getErrorThrowable());
            }
        }

        private String readResponse(HttpURLConnection connection) throws IOException {
            BufferedReader reader =  new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line=reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
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
