package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.Utils.mapPutSafe;

/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
class MagpieMobileAppPartialSchema extends BvPartialSchema {
    private static final String KEY_MOBILE_SOURCE = "mobileSource";
    private static final String KEY_CLIENT = "client";

    private static final String mobileSource = "bv-android-sdk";
    private String client;

    private MagpieMobileAppPartialSchema(Builder builder) {
        client = builder.client;
    }

    @Override
    public void addPartialData(Map<String, Object> dataMap) {
        mapPutSafe(dataMap, KEY_MOBILE_SOURCE, mobileSource);
        mapPutSafe(dataMap, KEY_CLIENT, client);
    }

    public static final class Builder {
        private String client;

        public Builder() {
        }

        public Builder client(String val) {
            client = val;
            return this;
        }

        public MagpieMobileAppPartialSchema build() {
            return new MagpieMobileAppPartialSchema(this);
        }
    }
}
