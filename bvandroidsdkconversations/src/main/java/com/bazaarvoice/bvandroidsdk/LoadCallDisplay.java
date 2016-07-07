/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public final class LoadCallDisplay<T> extends LoadCall<T> {

    private final ConversationsRequest request;

    LoadCallDisplay(ConversationsRequest request, Class<T> c, Call call) {
        super(c, call);
        this.request = request;
    }

    @Override
    public T loadSync() throws BazaarException {
        ConversationsResponseBase conversationResponse;
        try {
            Response response = call.execute();
            conversationResponse = deserializeAndCloseResponse(response);
        } catch (Throwable t) {
            throw new BazaarException(t.getMessage());
        }

        return (T)conversationResponse;
    }

    @Override
    public void loadAsync(final ConversationsCallback<T> conversationsCallback) {

        BazaarException error = request.getError();

        if (error != null) {
            errorOnMainThread(conversationsCallback, error);
            return;
        }

        this.call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) {
                ConversationsResponseBase conversationResponse = null;
                BazaarException error = null;
                if (!response.isSuccessful()) {
                    error = new BazaarException("Unsuccessful response for Conversations with error code: " + response.code());
                } else {
                    try {
                        conversationResponse = deserializeAndCloseResponse(response);
                    } catch (BazaarException t) {
                        error = t;
                    }
                }

                if (error != null) {
                    errorOnMainThread(conversationsCallback, error);
                }else {
                    successOnMainThread(conversationsCallback, conversationResponse);
                }
            }
        });
    }
}