/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class DemoSdkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        boolean isProdHost = originalRequest.url().host().equals("network.bazaarvoice.com");
        boolean isAnalyticsEvent = originalRequest.url().encodedPath().contains("event");
        if (DemoConstants.PREVENT_ANALYTICS_IN_PROD && isProdHost && isAnalyticsEvent) {
            Response noResponse = new Response.Builder()
                    .code(999)
                    .message("Not sending analytics to production while testing")
                    .body(ResponseBody.create(MediaType.parse("json"), "{\"appResponse\":\"Not sending analytics to production while testing\"}"))
                    .request(originalRequest)
                    .protocol(Protocol.HTTP_2)
                    .build();
            return noResponse;
        } else {
            return chain.proceed(originalRequest);
        }
    }
}
