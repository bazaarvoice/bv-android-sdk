/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
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
 * TODO: Describe file here.
 */
abstract class LoadCall<T> {
    final Call call;
    final Class c;
    final OkHttpClient okHttpClient = BVSDK.getInstance().getOkHttpClient();
    final Gson gson = BVSDK.getInstance().getGson();
    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    LoadCall(Class<T> c, Call call) {
        this.call = call;
        this.c = c;
    }

    /**
     * Will load the request synchronously on the Thread from which this method is call
     *
     * @return T the Response of type that corresponds to the prepared Request
     * @throws BazaarException containing detailed message of failure
     */
    public abstract T loadSync() throws BazaarException;

    /**
     * Will load the request asynchronously and perform ConversationsCallback on Main Thread
     *
     * @param conversationsCallback Callback to be performed
     */
    public abstract void loadAsync(final ConversationsCallback<T> conversationsCallback);

    ConversationsResponseBase deserializeAndCloseResponse(Response response) throws BazaarException {
        Gson gson = BVSDK.getInstance().getGson();
        ConversationsResponseBase conversationResponse = null;
        BazaarException error = null;
        try {
            Reader jsonReader = response.body().charStream();
            conversationResponse = (ConversationsResponseBase) gson.fromJson(jsonReader, c);

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

    void successOnMainThread(ConversationsCallback<T> callback, ConversationsResponseBase responseBase) {
        final BVHandlerCallback internalCb = new BVHandlerCallback() {
            @Override
            public void performOnMainThread(BVHandlerCallbackPayload payload) {
                callbackMainThread(payload);
            }
        };
        BVHandlerCallbackPayload handlerCallbackPayload = new BVHandlerCallbackPayload(internalCb, callback, responseBase, null);
        BVSDK.getInstance().postPayloadToMainThread(handlerCallbackPayload);
    }

    void errorOnMainThread(ConversationsCallback<T> callback, BazaarException e) {
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