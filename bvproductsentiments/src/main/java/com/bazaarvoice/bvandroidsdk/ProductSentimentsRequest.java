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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Base options for a conversations request, to display
 * or submission endpoints.
 */
abstract class ProductSentimentsRequest {
    static final String kAPI_VERSION = "apiversion";
    static final String kPASS_KEY = "passkey";

    static final String API_VERSION = "5.4";
    private final List<QueryPair> extraParams;

    ProductSentimentsRequest(Builder builder) {
        extraParams = builder.extraParams;

    }

    static class QueryPair {
        private final String key, value;

        public QueryPair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    static abstract class Builder<BuilderType> {
        private final List<QueryPair> extraParams;

        public Builder() {
            extraParams = new ArrayList<>();
        }



        public BuilderType addAdditionalField(@NonNull String key, @Nullable String value) {
            extraParams.add(new QueryPair(key, value));
            return (BuilderType) this;
        }

        /*
            Internal helper method. To be implemented in all classes inheriting from this.
            This method will be used mainly by BVConversations client to shortcut the network
            request if the request is in a known fail state. For example limit being to high or low.
            Return Throwable - If Request is misconfigured return Throwable with detailed message as to why the request will fail
            else null to indicate no error.
         */


    }
    abstract BazaarException getError();

}