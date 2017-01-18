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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.checkMain;
import static com.bazaarvoice.bvandroidsdk.internal.Utils.checkNotMain;

/**
 *
 * @param <RequestType>
 * @param <ResponseType>
 */
public final class LoadCallDisplay<RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> extends LoadCall<RequestType, ResponseType> {

    private final RequestType request;
    private DisplayDelegateCallback displayDelegateCallback;

    private static class DisplayDelegateCallback<RequestType extends ConversationsDisplayRequest, ResponseType extends ConversationsDisplayResponse> implements Callback {
        private final ConversationsCallback<ResponseType> conversationsCallback;
        private final LoadCallDisplay<RequestType, ResponseType> loadCallDisplay;

        public DisplayDelegateCallback(final LoadCallDisplay<RequestType, ResponseType> loadCallDisplay, final ConversationsCallback<ResponseType> conversationsCallback) {
            this.conversationsCallback = conversationsCallback;
            this.loadCallDisplay = loadCallDisplay;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            BazaarException bazaarException = new BazaarException("Display request failed", e);
            loadCallDisplay.errorOnMainThread(conversationsCallback, bazaarException);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                ConversationsResponse conversationResponse = null;
                BazaarException error = null;
                if (!response.isSuccessful()) {
                    error = new BazaarException("Unsuccessful response for Conversations with error code: " + response.code());
                } else {
                    try {
                        conversationResponse = loadCallDisplay.deserializeAndCloseResponse(response);
                    } catch (BazaarException t) {
                        error = t;
                    }
                }

                if (error != null) {
                    loadCallDisplay.errorOnMainThread(conversationsCallback, error);
                } else {
                    loadCallDisplay.successOnMainThread(conversationsCallback, conversationResponse);
                    // Route callbacks to Analytics Manager to handle any analytics that are associated
                    // with a successful display response
                    ConversationsAnalyticsManager.sendSuccessfulConversationsDisplayResponse(conversationResponse);
                }
            } finally {
                if (response != null) {
                    response.body().close();
                }
            }
        }
    }

    LoadCallDisplay(RequestType request, Class<ResponseType> responseTypeClass, Call call) {
        super(responseTypeClass, call);
        this.request = request;
    }

    RequestType getRequest() {
        return request;
    }

    @Override
    public ResponseType loadSync() throws BazaarException {
        checkNotMain();
        ResponseType conversationResponse;
        Response response = null;
        try {
            response = call.execute();
            conversationResponse = deserializeAndCloseResponse(response);
            // Route callbacks to Analytics Manager to handle any analytics that are associated
            // with a successful display response
            ConversationsAnalyticsManager.sendSuccessfulConversationsDisplayResponse(conversationResponse);
        } catch (Throwable t) {
            throw new BazaarException(t.getMessage());
        } finally {
            if (response != null) {
                response.body().close();
            }
        }

        return conversationResponse;
    }

    @Override
    public void loadAsync(final ConversationsCallback<ResponseType> conversationsCallback) {
        checkMain();
        BazaarException error = request.getError();

        if (error != null) {
            errorOnMainThread(conversationsCallback, error);
            return;
        }

        this.displayDelegateCallback = new DisplayDelegateCallback<RequestType, ResponseType>(this, conversationsCallback);
        this.call.enqueue(displayDelegateCallback);
    }
}