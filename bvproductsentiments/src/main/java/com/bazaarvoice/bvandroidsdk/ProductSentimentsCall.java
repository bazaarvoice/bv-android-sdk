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

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.Reader;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Base wrapper for a {@link Call} for a ProductSentimentss request to
 * both display and submission endpoints
 */
abstract class ProductSentimentsCall<RequestType extends ProductSentimentsRequest, ResponseType extends ProductSentimentsResponse> {
    Call call;
    final Class<ResponseType> responseTypeClass;
    final RequestType requestTypeClass;
    final OkHttpClient okHttpClient;
    final Gson gson;

    ProductSentimentsCall(RequestType requestTypeClass, Class<ResponseType> responseTypeClass, OkHttpClient okHttpClient, Gson gson) {
        this.responseTypeClass = responseTypeClass;
        this.requestTypeClass = requestTypeClass;
        this.okHttpClient = okHttpClient;
        this.gson = gson;
    }

    /**
     * Will load the request synchronously on the Thread from which this method is call
     *
     * @return ResponseType the Response of type that corresponds to the prepared Request
     * @throws BazaarException containing detailed message of failure
     */
    public abstract ResponseType loadSync() throws BazaarException;

    /**
     * Will load the request asynchronously and perform ProductSentimentsCallback on Main Thread
     *
     * @param productSentimentsCallback Callback to be performed
     */
    public abstract void loadAsync(final ProductSentimentsCallback<ResponseType> productSentimentsCallback);

    public void cancel() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    ResponseType deserializeAndCloseResponse(Response response) throws ProductSentimentsException {
        ResponseType productSentimentsResponse ;
        try {
            Reader jsonReader = response.body().charStream();
            productSentimentsResponse = (ResponseType) gson.fromJson(jsonReader, responseTypeClass);
        }  catch (JsonSyntaxException | JsonIOException e) {
        throw ProductSentimentsException.withNoRequestErrors("Unable to parse JSON", e);
    } catch (Throwable t) {
        throw ProductSentimentsException.withNoRequestErrors("Unknown error", t);
    } finally {
        if (response != null && response.body() != null) {
            response.body().close();
        }
    }
        return productSentimentsResponse;
    }


    BVErrorReport createErrorReportFromLoadCall(Exception e) {
        String requestTypeClassName = this.requestTypeClass.getClass().getSimpleName();
        BVEventValues.BVProductType bvProductType =  BVEventValues.BVProductType.CONVERSATIONS_REVIEWS;
        if(e instanceof  ProductSentimentsException) {
            String detailedMessage = ((ProductSentimentsException) e).getErrorListMessages();
            return new BVErrorReport(bvProductType, requestTypeClassName, detailedMessage);
        }
        return new BVErrorReport(bvProductType, requestTypeClassName, e);
    }

}
