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

import androidx.annotation.AnyThread;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.checkMain;
import static com.bazaarvoice.bvandroidsdk.internal.Utils.checkNotMain;

/**
 * Base wrapper for a {@link Call} for a  ProductSentimentss request to
 * endpoints
 */
public class ProductSentimentsLoadCall<RequestType extends ProductSentimentsRequest, ResponseType extends ProductSentimentsResponse> extends ProductSentimentsCall<RequestType, ResponseType> {    private final RequestType request;
    private final ProductSentimentsAnalyticsManager productSentimentsAnalyticsManager;
    private final ProductSentimentsUiHandler<RequestType, ResponseType> productSentimentsUiHandler;
    private final ProductSentimentsWorkerHandler<RequestType, ResponseType> productSentimentsWorkerHandler;
    private ProductSentimentsCallback<ResponseType> productSentimentsCallback;
    public static final String TAG = "BVProductSentiment";
    // Constructor

    ProductSentimentsLoadCall(
            RequestType request,
            Class<ResponseType> responseTypeClass,
            ProductSentimentsAnalyticsManager productSentimentsAnalyticsManager,
            Looper uiLooper,
            Looper bgLooper,
            OkHttpClient okHttpClient,
            Gson gson,
            Call call) {
        super(request, responseTypeClass, okHttpClient, gson);
        this.call = call;
        this.productSentimentsAnalyticsManager = productSentimentsAnalyticsManager;
        this.request = request;
        this.productSentimentsUiHandler = new ProductSentimentsUiHandler<>(uiLooper, this);
        this.productSentimentsWorkerHandler = new ProductSentimentsWorkerHandler<>(bgLooper, this);
    }
    RequestType getRequest() {
        return request;
    }

    ProductSentimentsAnalyticsManager getproductSentimentsAnalyticsManager() {
        return productSentimentsAnalyticsManager;
    }
    private ResponseType fetch() throws ProductSentimentsException {
        ResponseType productSentimentsResponse;
        Response response = null;
        try {
            response = call.execute();
            productSentimentsResponse = deserializeAndCloseResponse(response);
            // Route callbacks to Analytics Manager to handle any analytics that are associated
            // with a successful display response
            RequestType request = this.getRequest();
            //  productSentimentsAnalyticsManager.sendSuccessfulConversationsDisplayResponse(conversationResponse,request);
        } catch (IOException e) {
            throw  ProductSentimentsException.withNoRequestErrors("Execution of call failed", e);
        } catch (ProductSentimentsException e) {
            throw e;
        } catch (Throwable t) {
            throw  ProductSentimentsException.withNoRequestErrors("Unknown exception", t);
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }


        return productSentimentsResponse;
    }

    @Override
    public ResponseType loadSync() throws ProductSentimentsException {
        BVSDK.getInstance().bvLogger.v(TAG, "Beginning of sync request");
        checkNotMain();
        return fetch();
    }



    public void loadAsync(@NonNull ProductSentimentsCallback<ResponseType> productSentimentsCallback) {
        BVSDK.getInstance().bvLogger.v(TAG, "Beginning of async request");
        checkMain();
        this.productSentimentsCallback = productSentimentsCallback;
        dispatchFetch();
    }


    @Override
    public void cancel() {
        super.cancel();
        productSentimentsCallback = null;
    }

    private List<Error> getErrorsFromResponse(ResponseType productSentimentsResponse) {
        List<Error> errors = new ArrayList<>();
        if (productSentimentsResponse != null && productSentimentsResponse.getErrors() != null) {
            errors = productSentimentsResponse.getErrors();
        }
        return errors;
    }

    private void completeWithException(ProductSentimentsException exception) {
        BVSDK.getInstance().bvLogger.v(TAG, "Complete with failure");
        if (productSentimentsCallback != null) {
            productSentimentsCallback.onFailure(exception);
            productSentimentsCallback = null;
        }
    }

    @MainThread
    private void completeWithSuccess(ResponseType response) {
        BVSDK.getInstance().bvLogger.v(TAG, "Complete With Success");
        if (productSentimentsCallback != null) {
            productSentimentsCallback.onSuccess(response);
            productSentimentsCallback = null;
        }
    }

    @AnyThread
    private void dispatchFetch() {
        BVSDK.getInstance().bvLogger.v("BVProductSentiments", "Dispatch submit message");
        productSentimentsWorkerHandler.sendMessage(productSentimentsWorkerHandler.obtainMessage(ProductSentimentsWorkerHandler.FETCH));
    }

    @WorkerThread
    private void dispatchCompleteWithSuccess(ResponseType response) {
        BVSDK.getInstance().bvLogger.v("BVProductSentiments", "Dispatch completed with success");
        productSentimentsUiHandler.sendMessage(productSentimentsUiHandler.obtainMessage(ProductSentimentsUiHandler.CB_SUCCESS, response));
    }

    @WorkerThread
    private void dispatchCompleteWithFailure(BazaarException e) {
        BVSDK.getInstance().bvLogger.v("BVProductSentiments", "Dispatch completed with error");
        productSentimentsUiHandler.sendMessage(productSentimentsUiHandler.obtainMessage(ProductSentimentsUiHandler.CB_FAILURE, e));
    }

    @MainThread
    private void dispatchError(BazaarException e) {
        Message message = productSentimentsUiHandler.obtainMessage(ProductSentimentsUiHandler.CB_FAILURE, e);
        productSentimentsUiHandler.sendMessage(message);
    }

    /**
     * Handler that is responsible for communicating to the BVWorker thread to complete background work
     *
     * @param <RequestType>
     * @param <ResponseType>
     */
    private static class ProductSentimentsWorkerHandler<RequestType extends ProductSentimentsRequest, ResponseType extends ProductSentimentsResponse> extends Handler {
        private static final int FETCH = 1;

        ProductSentimentsLoadCall<RequestType, ResponseType> loadCallProductSentiment;

        ProductSentimentsWorkerHandler(Looper looper, ProductSentimentsLoadCall<RequestType, ResponseType> loadCallProductSentiment) {
            super(looper);
            this.loadCallProductSentiment = loadCallProductSentiment;
        }

        @Override
        public void handleMessage(Message msg) {
            BVSDK.getInstance().bvLogger.v(TAG, "Handle display worker message");
            try {
                ResponseType response = loadCallProductSentiment.fetch();
                loadCallProductSentiment.dispatchCompleteWithSuccess(response);
            } catch (BazaarException e) {
                BVErrorReport bvErrorReport = loadCallProductSentiment.createErrorReportFromLoadCall(e);
                BVSDK.getInstance().getBvPixel().track(bvErrorReport);
                loadCallProductSentiment.dispatchCompleteWithFailure(e);
            }

        }
    }

    /**
     * Handler responsible for communicating with the MainThread when work on the WorkerThread is complete.
     */
    private static class ProductSentimentsUiHandler<RequestType extends ProductSentimentsRequest, ResponseType extends ProductSentimentsResponse> extends Handler {
        ProductSentimentsLoadCall<RequestType, ResponseType> loadCallProductSentiment;
        private static final int CB_SUCCESS = 1;
        private static final int CB_FAILURE = 2;

        ProductSentimentsUiHandler(Looper uiLooper, ProductSentimentsLoadCall<RequestType, ResponseType> loadCallProductSentiment) {
            super(uiLooper);
            this.loadCallProductSentiment = loadCallProductSentiment;
        }

        @Override
        public void handleMessage(Message msg) {
            BVSDK.getInstance().bvLogger.v(TAG, "Handle UI message");
            switch (msg.what) {
                case CB_SUCCESS:
                    ResponseType response = (ResponseType) msg.obj;
                    loadCallProductSentiment.completeWithSuccess(response);
                    break;
                case CB_FAILURE:
                    ProductSentimentsException exception = (ProductSentimentsException) msg.obj;
                    loadCallProductSentiment.completeWithException(exception);
                    break;
            }
        }
    }
}


