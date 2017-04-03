/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid;

import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoDataUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class DemoSdkInterceptor implements Interceptor {

    private DemoConfigUtils demoConfigUtils;
    private DemoDataUtil demoDataUtil;

    public DemoSdkInterceptor(DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil) {
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (demoConfigUtils.isDemoClient()) {
            return interceptDemoRequests(chain);
        }

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
        }

        return chain.proceed(originalRequest);
    }

    private Response interceptDemoRequests(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String host = originalRequest.url().host();
        String path = originalRequest.url().encodedPath();

        if (originalRequest.url().toString().contains("REPLACE_ME")) {
            String doNotSendInvalidRequestsMessage = "{ \"demoInterceptMessage\" : \"Query params contain REPLACE_ME, so this is not being sent to the endpoint\" }";
            Response response = new Response.Builder()
                .code(403)
                .body(ResponseBody.create(MediaType.parse("json"), doNotSendInvalidRequestsMessage))
                .request(originalRequest)
                .protocol(Protocol.HTTP_2)
                .build();
            return response;
        }

        if (host.contains("api.bazaarvoice.com")) {
            if (path.contains("curations/content/get")) {
                String curationsFeedItemsJsonStr = demoDataUtil.getCurationsFeedResponseJsonString();
                Response response = new Response.Builder()
                    .code(200)
                    .body(ResponseBody.create(MediaType.parse("json"), curationsFeedItemsJsonStr))
                    .request(originalRequest)
                    .protocol(Protocol.HTTP_2)
                    .build();
                return response;
            } else if (path.contains("curations/content/add")) {
                String curationsPostResponseJsonString = demoDataUtil.getCurationsPostResponseJsonString();
                Response response = new Response.Builder()
                    .code(200)
                    .body(ResponseBody.create(MediaType.parse("json"), curationsPostResponseJsonString))
                    .request(originalRequest)
                    .protocol(Protocol.HTTP_2)
                    .build();
                return response;
            }
        }

        return chain.proceed(originalRequest);
    }
}
