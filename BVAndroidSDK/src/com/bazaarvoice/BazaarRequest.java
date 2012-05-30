package com.bazaarvoice;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.apache.http.entity.mime.MultipartEntity;

import java.util.LinkedList;

/**
 * User: gary
 * Date: 4/9/12
 * Time: 8:55 PM
 *
 * This class sends and handles requests to the BazaarVoice API.  The 3 ways to send a request are via sendDisplayRequest,
 * queueDisplayRequest and sendBlockingDisplayRequest.  sendDisplayRequest will create a thread to handle the result from the server.
 * queueDisplayRequest has a single reused thread that keeps the responses in the order they were requested.
 * sendBlockingDisplayRequest blocks on the current thread waiting for a reply
 */
public class BazaarRequest
{
    private String passKey;
    private String apiVersion;
    private RequestQueue requestQueue;


    private String requestHeader;

    private final String tag = getClass().getSimpleName();
    private final Object lock = new Object();
    private final Object lockThread = new Object();
    private enum RequestMethod{DISPLAY, SUBMIT}

    //attempt to reuse old http client to avoid having to reopen the socket every time
    private HttpClient reusableClient;

    /**
     * Initialize the request with the necessary parameters.
     *
     * @param domainName reviews.apitestcustomer.bazaarvoice.com/bvstaging
     * @param passKey your api test key
     * @param apiVersion the version of the api you want to use
     */
    public BazaarRequest(String domainName, String passKey, String apiVersion) {
        this.passKey = passKey;
        this.apiVersion = apiVersion;

        requestHeader = "http://" + domainName + "/data/";
        reusableClient = getThreadSafeClient();
    }


    /**
     * Send a request, spawn a thread, and return the result via the listener. (non-blocking)
     * @param type the type of request
     * @param params the parameters for the request
     * @param listener the listener to handle the results on
     */
    public void sendDisplayRequest(final RequestType type, DisplayParams params, final OnBazaarResponse listener)
    {
        sendThreaded(type.getDisplayName(), params, listener, RequestMethod.DISPLAY);
    }

    /**
     * Post a submission, spawn a thread, and return the response via the listener. (non-blocking)
     * @param type the type of request
     * @param params the parameters for the request
     * @param listener the listener to handle the results on
     */
    public void postSubmission(final RequestType type, BazaarParams params, final OnBazaarResponse listener)
    {
        sendThreaded(type.getSubmissionName(), params, listener, RequestMethod.SUBMIT);
    }

    /**
     * Add a new request to the request queue, and return the result via the listener.  This will keep the order
     * that the requests are made in. (non-blocking)
     * @param type the type of request
     * @param params the parameters for the request
     * @param listener the listener to handle the results on
     */
    public void queueDisplayRequest(final RequestType type, DisplayParams params, final OnBazaarResponse listener)
    {
        addToQueue(type.getDisplayName(),params,listener,RequestMethod.DISPLAY);
    }

    /**
     * Add a new request to the request queue, and return the response via the listener.  This will keep the order
     * that the requests are made in. (non-blocking)
     * @param type the type of request
     * @param params the parameters for the request
     * @param listener the listener to handle the results on
     */
    public void queueSubmission(final RequestType type, BazaarParams params, final OnBazaarResponse listener)
    {
        addToQueue(type.getSubmissionName(),params,listener,RequestMethod.SUBMIT);
    }

    /**
     * Send a blocking request to the server
     * @param type the type of request
     * @param params the parameters for the request
     * @return the JSON result
     * @throws BazaarException on any JSON or communication errors
     */
    public JSONObject sendBlockingDisplayRequest(RequestType type, DisplayParams params) throws BazaarException
    {
        return send(getRequestString(type.getDisplayName(), params),
                RequestMethod.DISPLAY,
                params==null?null:params.getMedia());
    }

    /**
     * Send a blocking submit to the server
     * @param type the type of submit
     * @param params the parameters for the request
     * @return the JSON result
     * @throws BazaarException on any JSON or communication errors
     */
    public JSONObject postBlockingSubmission(RequestType type, BazaarParams params) throws BazaarException
    {
        return send(getRequestString(type.getSubmissionName(), params),
                RequestMethod.DISPLAY,
                params==null?null:params.getMedia());
    }

    /**
     * Send a blocking request to the server with a simple string url and optional byte array
     * @param URL the url to send the request/submit to
     * @param method display or submit
     * @param mediaEntity a file to send to the server
     * @return the JSON result
     * @throws BazaarException on any JSON or communication errors
     */
    public JSONObject send(String URL, RequestMethod method, Media mediaEntity) throws BazaarException {
        try {
            // create an HTTP request to a protected resource
            HttpRequestBase httpRequest = method==RequestMethod.SUBMIT ?new HttpPost(URL):new HttpGet(URL);
            if (mediaEntity != null && method == RequestMethod.SUBMIT)
            {
                reusableClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                //httpRequest.setHeader("Content-Type", "multipart/form-data");
                MultipartEntity mpEntity = new MultipartEntity();
                ContentBody body = new ByteArrayBody(mediaEntity.getBytes(),
                        mediaEntity.getFilename());

                mpEntity.addPart(mediaEntity.getName(), body);
                ((HttpPost) httpRequest).setEntity(mpEntity);
            }

            HttpClient httpClient = reusableClient;

            HttpResponse response = httpClient.execute(httpRequest);
            StatusLine statusLine= response.getStatusLine();
            int status = statusLine.getStatusCode();

            if (status < 200 || status > 299)
            {
                throw new BazaarException("Error communicating with server. "  + statusLine.getReasonPhrase() + " " + status);
            }
            else
            {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                return new JSONObject(result);
            }
        }
        catch (Exception e) {
            throw new BazaarException("Error handling results from server!", e);
        }
    }


    /**
     * Get the request as a string
     * @param type the request type
     * @param params the parameters of the request
     * @return the request url
     */
    private String getRequestString(final String type, BazaarParams params)
    {
        String requestString = requestHeader +  type + ".json";
        requestString = DisplayParams.addURLParameter(requestString, "apiversion", apiVersion);
        requestString = DisplayParams.addURLParameter(requestString, "passkey", passKey);
        if (params != null) {
            requestString = params.toURL(requestString);
        }

        return requestString;
    }

    /**
     * Create a clone of the media entity
     * @param entity the entity to clone from
     * @return the new entity
     */
    private Media cloneMedia(Media entity) {
        //clone the media entity for the same reason
        if (entity != null) {
            try {
                entity = (Media)entity.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("ByteArrayBody Clone is not supported!", e);
            }
        }
        return entity;
    }


    /**
     * Send a request, spawn a thread, and return the result via the listener. (non-blocking)
     * @param type the type of request
     * @param params the parameters for the request
     * @param listener the listener to handle the results on
     */
    private void sendThreaded(final String type,
                              BazaarParams params,
                              final OnBazaarResponse listener,
                              final RequestMethod method)
    {
        //get the request string before we thread in case other calls modify it
        final String requestString = getRequestString(type, params);
        //clone the media for the same reason
        final Media media = cloneMedia(params==null?null:params.getMedia());

        Log.d(tag, requestString);

        new Thread() {
            public void run() {
                try {
                    JSONObject response = send(requestString, method, media);

                    //lets synchronize the use of the listener in case the user does not
                    synchronized (lock) {
                        listener.onResponse(response);
                    }
                } catch (Exception e) {
                    synchronized (lock) {
                        listener.onException(e.getMessage(), e);
                    }
                }
            }
        }.start();
    }

    /**
     * Add a new request to the request queue, and return the result via the listener.  This will keep the order
     * that the requests are made in. (non-blocking)
     * @param type the type of request
     * @param params the parameters for the request
     * @param listener the listener to handle the results on
     */
    private void addToQueue(final String type,
                       BazaarParams params,
                       final OnBazaarResponse listener,
                       RequestMethod method)
    {
        final String requestString = getRequestString(type, params);
        Log.d(tag, requestString);

        if (requestQueue == null) {
            requestQueue = new RequestQueue();
        }
        requestQueue.addRequest(requestString, listener, method, cloneMedia(params==null?null:params.getMedia()));
    }


    /**
     * @return a default http client in a thread safe manner.
     */
    public static DefaultHttpClient getThreadSafeClient()
    {
        DefaultHttpClient client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();

        client = new DefaultHttpClient(
                new ThreadSafeClientConnManager(params,
                        mgr.getSchemeRegistry()), params);

        return client;
    }

    /**
     * inner class for handling queued requests
     */
    class RequestQueue {
        private Thread requestThread;

        private class RequestData {
            public String request;
            public OnBazaarResponse listener;
            public RequestMethod method;
            public Media media;

            private RequestData(String request, OnBazaarResponse listener, RequestMethod method, Media media) {
                this.request = request;
                this.listener = listener;
                this.method = method;
                this.media = media;
            }
        }
        private LinkedList<RequestData> requestQueue = new LinkedList<RequestData>();
        private final Object lock = new Object();

        public RequestQueue()
        {
        }

        public void addRequest(String request, OnBazaarResponse listener, RequestMethod method, Media media) {
            synchronized (lock) {
                requestQueue.addFirst(new RequestData(request,listener,method,media));
            }

            checkThread();
        }

        public void checkThread() {
            if (requestThread == null) {
                boolean startThread = false;
                //synchronize here so we don't create 2 threads
                synchronized (lockThread) {
                    if (requestThread == null) {
                        startThread = true;
                        requestThread = new Thread() {
                            public void run()
                            {
                                dequeRequests();
                            }
                        };
                    }
                }

                if (startThread) {
                    requestThread.start();
                }
            }
        }

        private void dequeRequests()
        {
            while(true) {
                synchronized (lock) {
                    if (requestQueue.size() > 0)
                    {
                        RequestData requestData = requestQueue.removeLast();
                        try {
                            JSONObject response = send(requestData.request, requestData.method, requestData.media);
                            requestData.listener.onResponse(response);

                        } catch (Exception e) {
                            synchronized (lock) {
                                requestData.listener.onException(e.getMessage(), e);
                            }
                        }
                    }
                    else {
                        //this thread is dead
                        requestThread = null;
                        break;
                    }
                }
            }
        }
    }
}
