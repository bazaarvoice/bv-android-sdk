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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Base wrapper for a {@link Call} for a Conversations request to
 * both display and submission endpoints
 */
abstract class LoadCall<RequestType extends ConversationsRequest, ResponseType extends ConversationsResponse> {
    final Call call;
    final Class responseTypeClass;
    final OkHttpClient okHttpClient = BVSDK.getInstance().getOkHttpClient();
    final Gson gson = BVSDK.getInstance().getGson();
    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    LoadCall(Class<ResponseType> responseTypeClass, Call call) {
        this.call = call;
        this.responseTypeClass = responseTypeClass;
    }

    /**
     * Will load the request synchronously on the Thread from which this method is call
     *
     * @return ResponseType the Response of type that corresponds to the prepared Request
     * @throws BazaarException containing detailed message of failure
     */
    public abstract ResponseType loadSync() throws BazaarException;

    /**
     * Will load the request asynchronously and perform ConversationsCallback on Main Thread
     *
     * @param conversationsCallback Callback to be performed
     */
    public abstract void loadAsync(final ConversationsCallback<ResponseType> conversationsCallback);

    ResponseType deserializeAndCloseResponse(Response response) throws BazaarException {
        Gson gson = BVSDK.getInstance().getGson();
        ResponseType conversationResponse = null;
        BazaarException error = null;
        try {
            Reader jsonReader = response.body().charStream();
            conversationResponse = (ResponseType) gson.fromJson(jsonReader, responseTypeClass);

        } catch (JsonSyntaxException | JsonIOException e) {
            error = new BazaarException("Unable to parse JSON " + response);
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }

        if (conversationResponse != null && conversationResponse.getHasErrors()) {

            if (conversationResponse.getErrors().size() > 0) {
                error = new BazaarException(gson.toJson(conversationResponse.getErrors()));
            }else {
                error = new BazaarException(gson.toJson(conversationResponse.getFormErrors().getFieldErrorMap()));

            }
        }

        if (error != null) {
            throw error;
        }

        return conversationResponse;
    }

    void callbackMainThread(BVHandlerCallbackPayload payload) {
        BVCallback callback = payload.getExternalCB();
        if (payload.getError() != null) {
            callback.onFailure(payload.getError());
        } else {
            callback.onSuccess(payload.getPayload());
        }
    }

    void successOnMainThread(ConversationsCallback<ResponseType> callback, ConversationsResponse responseBase) {
        final BVHandlerCallback internalCb = new BVHandlerCallback() {
            @Override
            public void performOnMainThread(BVHandlerCallbackPayload payload) {
                callbackMainThread(payload);
            }
        };
        BVHandlerCallbackPayload handlerCallbackPayload = new BVHandlerCallbackPayload(internalCb, callback, responseBase, null);
        BVSDK.getInstance().postPayloadToMainThread(handlerCallbackPayload);
    }

    void errorOnMainThread(ConversationsCallback<ResponseType> callback, BazaarException e) {
        final BVHandlerCallback internalCb = new BVHandlerCallback() {
            @Override
            public void performOnMainThread(BVHandlerCallbackPayload payload) {
                callbackMainThread(payload);
            }
        };
        BVHandlerCallbackPayload handlerCallbackPayload = new BVHandlerCallbackPayload(internalCb, callback, null, e);
        BVSDK.getInstance().postPayloadToMainThread(handlerCallbackPayload);
    }
}