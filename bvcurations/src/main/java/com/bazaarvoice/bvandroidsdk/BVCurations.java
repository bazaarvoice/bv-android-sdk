/*
 * Copyright 2017
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
 *
 */

package com.bazaarvoice.bvandroidsdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.toBase64;

/**
 * Bazaarvoice Curations API wrapper. This class is best instantiated as a
 * singleton.
 */
public class BVCurations {
    // region Properties

    private static final int MSG_DISPATCH_GET_FEED = 1;
    private static final int MSG_DISPATCH_POST_CONTENT = 2;
    private static final int MSG_DELIVER_GET_FEED = 3;
    private static final int MSG_DELIVER_POST_CONTENT = 4;

    private final CurationsBgHandler bgHandler;
    private final CurationsUiHandler uiHandler;
    private final CurationsAnalyticsManager curationsAnalyticsManager;

    // endregion

    // region Constructor

    public BVCurations() {
        this.bgHandler = new CurationsBgHandler(BVSDK.getInstance().getBackgroundLooper(), this);
        this.uiHandler = new CurationsUiHandler(this);
        this.curationsAnalyticsManager = new CurationsAnalyticsManager(BVSDK.getInstance());
    }

    // endregion

    // region Public API

    /**
     * Get curations feed using specified parameters
     * @param request The request is used to specify parameters which will be used in the Curations GET request
     * @param callback Callback is used to handle successful and unsuccessful requests
     * <em>Note:</em> This method keeps a weak reference to the {@link CurationsFeedCallback} instance and will be
     * garbage collected if you do not keep a strong reference to it. }.
     */
    @MainThread
    public void getCurationsFeedItems(final CurationsFeedRequest request, final CurationsFeedCallback callback){
        RequestData<List<CurationsFeedItem>> requestData = new RequestData<>(
            request.toUrlQueryString(), new DelegateFeedCb(callback), request);
        bgHandler.sendMessage(bgHandler.obtainMessage(MSG_DISPATCH_GET_FEED, requestData));
    }

    /**
     * Post Content to curations
     *
     * @param request The request with the content you would like to post.
     * @param callback Callback is used to handle successful and unsuccessful posts
     * <em>Note:</em> This method keeps a weak reference to the {@link CurationsPostCallback} instance and will be
     * garbage collected if you do not keep a strong reference to it. }.
     */
    @MainThread
    public void postContentToCurations(CurationsPostRequest request, final CurationsPostCallback callback){
        RequestData<CurationsPostResponse> requestData = new RequestData<CurationsPostResponse>(
            request.toUrlQueryString(), new DelegatePostCb(callback), request);
        bgHandler.sendMessage(bgHandler.obtainMessage(MSG_DISPATCH_POST_CONTENT, requestData));
    }

    // endregion

    // region Internal API

    @WorkerThread
    List<CurationsFeedItem> getCurationsFeedItems(CurationsFeedRequest request) throws Throwable {
        OkHttpClient okHttpClient = BVSDK.getInstance().getOkHttpClient();
        Gson gson = BVSDK.getInstance().getGson();
        BVLogger bvLogger = BVSDK.getInstance().getBvLogger();

        Request httpRequest = new Request.Builder()
            .url(request.toUrlQueryString())
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .addHeader("User-Agent", BVSDK.getInstance().getBvsdkUserAgent())
            .build();
        bvLogger.v(getClass().getSimpleName(), httpRequest.url().toString());

        Response response = null;
        try {
            response = okHttpClient.newCall(httpRequest).execute();
            if (!response.isSuccessful()) {
                throw new Exception("Unsuccessful response for curations with error code: " + response.code());
            } else {
                CurationsFeedResponse curationsResponse = gson.fromJson(response.body().charStream(), CurationsFeedResponse.class);
                //curations responses can have an HTTP response code of 200 but they return a parameter, 'code', which should also be checked for a 200
                if (curationsResponse.code != 200){
                    throw new Exception("Curations 'code' not 200");
                }
                return curationsResponse.getUpdates();
            }

        } catch (IOException e) {
            throw new IOException("Request for curations failed", e);
        } catch (JsonIOException | JsonSyntaxException e) {
            throw new Exception("Failed to parse curations", e);
        } catch (Exception e) {
            throw new Exception("Exception while getting curations", e);
        }finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
    }

    @WorkerThread
    private void dispatchGetFeed(RequestData<List<CurationsFeedItem>> requestData) {
        CurationsFeedRequest request = (CurationsFeedRequest) requestData.getObj1();
        ResponseData responseData;
        try {
            List<CurationsFeedItem> feedItems = getCurationsFeedItems(request);
            responseData = new ResponseData<>(true, null, feedItems, requestData);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            responseData = new ResponseData<>(false, throwable, null, requestData);
        }
        uiHandler.sendMessage(uiHandler.obtainMessage(MSG_DELIVER_GET_FEED, responseData));
    }

    @WorkerThread
    private void dispatchPostContent(RequestData<CurationsPostResponse> requestData) {
        CurationsPostRequest request = (CurationsPostRequest) requestData.getObj1();
        OkHttpClient okHttpClient = BVSDK.getInstance().getOkHttpClient();
        BVLogger bvLogger = BVSDK.getInstance().getBvLogger();

        Gson gson = BVSDK.getInstance().getGson();

        String jsonPayload = request.getJsonPayload();
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("jsonpayload", jsonPayload);

        if (request.builder.bitmap != null && (request.builder.photos == null || request.builder.photos.size() == 0)){
            String contentBase64 = "data:image/jpeg;base64," + toBase64(request.builder.bitmap);
            bodyBuilder.addFormDataPart("image", contentBase64);
        }

        Request httpRequest = new Request.Builder()
            .addHeader("User-Agent", BVSDK.getInstance().getBvsdkUserAgent())
            .url(request.toUrlQueryString())
            .post(bodyBuilder.build())
            .build();
        bvLogger.v(getClass().getSimpleName(), "POST: " + request.toUrlQueryString());
        bvLogger.v(getClass().getSimpleName(), "MultiPart: " + jsonPayload);

        Throwable errorThrowable = null;
        CurationsPostResponse curationsResponse = null;
        boolean didSucceed = false;

        Response response = null;
        try {
            response = okHttpClient.newCall(httpRequest).execute();
            if (!response.isSuccessful()) {
                errorThrowable = new Exception("Unsuccessful response for curations with error code: " + response.code());
            } else {
                curationsResponse = gson.fromJson(response.body().charStream(), CurationsPostResponse.class);
                //curations responses can have an HTTP response code of 200 but they return a parameter, 'code', which should also be checked for a 200
                if (curationsResponse.status.equals(201.0)){
                    didSucceed = true;
                }else {
                    didSucceed = false;
                    errorThrowable = new Exception("Curations error: " + curationsResponse.status);
                    curationsResponse = null;
                }
            }

        } catch (IOException e) {
            errorThrowable = new IOException("Request for curations failed", e);
        } catch (JsonIOException | JsonSyntaxException e) {
            errorThrowable = new Exception("Failed to parse curations", e);
        } catch (Exception e) {
            errorThrowable = new Exception("Exception while getting curations", e);
        }finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }

        if (errorThrowable != null) {
            errorThrowable.printStackTrace();
        }

        ResponseData<CurationsPostResponse> responseData = new ResponseData<>(didSucceed, errorThrowable, curationsResponse, requestData);
        uiHandler.sendMessage(uiHandler.obtainMessage(MSG_DELIVER_POST_CONTENT, responseData));
    }

    @MainThread
    private void deliverGetFeed(ResponseData<List<CurationsFeedItem>> responseData) {
        List<CurationsFeedItem> feedItems = responseData.getResponse();
        Throwable throwable = responseData.getErrorThrowable();
        BVCallback<List<CurationsFeedItem>> cb = responseData.getRequestData().getCallback();
        if (responseData.isSuccess()) {
            cb.onSuccess(feedItems);
        } else {
            cb.onFailure(new BazaarException("Failed to get curations feed", throwable));
        }
    }

    @MainThread
    private void deliverPostContent(ResponseData<CurationsPostResponse> responseData) {
        CurationsPostResponse response = responseData.getResponse();
        Throwable throwable = responseData.getErrorThrowable();
        BVCallback<CurationsPostResponse> cb = responseData.getRequestData().getCallback();
        if (responseData.isSuccess()) {
            cb.onSuccess(response);
            curationsAnalyticsManager.sendUploadPhotoFeatureEvent("none");
        } else {
            cb.onFailure(new BazaarException("Failed to get curations feed", throwable));
        }
    }

    private static class CurationsBgHandler extends Handler {
        private BVCurations curations;

        CurationsBgHandler(Looper looper, BVCurations curations) {
            super(looper);
            this.curations = curations;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DISPATCH_GET_FEED: {
                    RequestData<List<CurationsFeedItem>> requestData = (RequestData<List<CurationsFeedItem>>) msg.obj;
                    curations.dispatchGetFeed(requestData);
                    break;
                }
                case MSG_DISPATCH_POST_CONTENT: {
                    RequestData<CurationsPostResponse> requestData = (RequestData<CurationsPostResponse>) msg.obj;
                    curations.dispatchPostContent(requestData);
                }
            }
        }
    }

    private static class CurationsUiHandler extends Handler {
        private final BVCurations curations;

        CurationsUiHandler(BVCurations curations) {
            super(Looper.getMainLooper());
            this.curations = curations;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DELIVER_GET_FEED: {
                    ResponseData<List<CurationsFeedItem>> responseData = (ResponseData<List<CurationsFeedItem>>) msg.obj;
                    curations.deliverGetFeed(responseData);
                    break;
                }
                case MSG_DELIVER_POST_CONTENT: {
                    ResponseData<CurationsPostResponse> responseData = (ResponseData<CurationsPostResponse>) msg.obj;
                    curations.deliverPostContent(responseData);
                }
            }
        }
    }

    private static class RequestData<ResponseType> {
        private final String url;
        private final BVCallback<ResponseType> callback;
        private final Object obj1;

        RequestData(String url, BVCallback<ResponseType> callback, final Object obj1) {
            this.url = url;
            this.callback = callback;
            this.obj1 = obj1;
        }

        public String getUrl() {
            return url;
        }

        BVCallback<ResponseType> getCallback() {
            return callback;
        }

        @Nullable
        Object getObj1() {
            return obj1;
        }
    }

    private static class ResponseData<ResponseType> {
        private final boolean didSucceed;
        private final Throwable errorThrowable;
        private final ResponseType response;
        private final RequestData<ResponseType> requestData;

        ResponseData(final boolean didSucceed, final Throwable errorThrowable, final ResponseType response, final RequestData<ResponseType> requestData) {
            this.didSucceed = didSucceed;
            this.errorThrowable = errorThrowable;
            this.response = response;
            this.requestData = requestData;
        }

        boolean isSuccess() {
            return didSucceed;
        }

        @Nullable
        Throwable getErrorThrowable() {
            return errorThrowable;
        }

        @Nullable
        ResponseType getResponse() {
            return response;
        }

        RequestData<ResponseType> getRequestData() {
            return requestData;
        }
    }

    private static class DelegateFeedCb implements BVCallback<List<CurationsFeedItem>> {
        private final WeakReference<CurationsFeedCallback> cbWeakRef;

        DelegateFeedCb(CurationsFeedCallback callback) {
            this.cbWeakRef = new WeakReference<CurationsFeedCallback>(callback);
        }

        @Override
        public void onSuccess(List<CurationsFeedItem> response) {
            CurationsFeedCallback callback = cbWeakRef.get();
            if (callback == null) {
                return;
            }
            callback.onSuccess(response);
        }

        @Override
        public void onFailure(BazaarException exception) {
            CurationsFeedCallback callback = cbWeakRef.get();
            if (callback == null) {
                return;
            }
            callback.onFailure(exception);
        }
    }

    private static class DelegatePostCb implements BVCallback<CurationsPostResponse> {
        private final WeakReference<CurationsPostCallback> cbWeakRef;

        DelegatePostCb(CurationsPostCallback callback) {
            this.cbWeakRef = new WeakReference<>(callback);
        }

        @Override
        public void onSuccess(CurationsPostResponse response) {
            CurationsPostCallback callback = cbWeakRef.get();
            if (callback == null) {
                return;
            }
            callback.onSuccess(response);
        }

        @Override
        public void onFailure(BazaarException exception) {
            CurationsPostCallback callback = cbWeakRef.get();
            if (callback == null) {
                return;
            }
            callback.onFailure(exception);
        }
    }

    // endregion
}
