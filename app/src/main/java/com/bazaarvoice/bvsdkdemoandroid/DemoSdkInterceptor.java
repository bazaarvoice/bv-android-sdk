/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid;

import com.bazaarvoice.bvandroidsdk.AuthorsResponse;
import com.bazaarvoice.bvandroidsdk.BulkRatingsResponse;
import com.bazaarvoice.bvandroidsdk.CommentsResponse;
import com.bazaarvoice.bvandroidsdk.CurationsFeedResponse;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageResponse;
import com.bazaarvoice.bvandroidsdk.QuestionAndAnswerResponse;
import com.bazaarvoice.bvandroidsdk.ReviewResponse;
import com.bazaarvoice.bvandroidsdk.ShopperProfile;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class DemoSdkInterceptor implements Interceptor {

    private DemoClient demoClient;
    private DemoMockDataUtil demoMockDataUtil;

    public DemoSdkInterceptor(DemoClient demoClient, DemoMockDataUtil demoMockDataUtil) {
        this.demoClient = demoClient;
        this.demoMockDataUtil = demoMockDataUtil;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (demoClient.isMockClient()) {
            return interceptMockRequests(chain);
        } else {
            if (isInvalidRequest(chain)) {
                return getInvalidResponse(chain);
            }
        }

        Request originalRequest = chain.request();
        return chain.proceed(originalRequest);
    }

    private Response interceptMockRequests(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String host = originalRequest.url().host();
        String path = originalRequest.url().encodedPath();

        if (host.contains("api.bazaarvoice.com")) {
            if (path.contains("curations/c3/content/get")) {
                CurationsFeedResponse curationsFeedResponse = demoMockDataUtil.getCurationsFeedReponse();
                return demoMockDataUtil.getHttpResponse(originalRequest, curationsFeedResponse);
            } else if (path.contains("data/reviews.json")) {
                ReviewResponse reviewsResponse = demoMockDataUtil.getConversationsReviews();
                return demoMockDataUtil.getHttpResponse(originalRequest, reviewsResponse);
            } else if (path.contains("data/questions.json")) {
                QuestionAndAnswerResponse qAndAResponse = demoMockDataUtil.getConversationsQuestions();
                return demoMockDataUtil.getHttpResponse(originalRequest, qAndAResponse);
            } else if (path.contains("data/products.json")) {
                ProductDisplayPageResponse pdpResponse = demoMockDataUtil.getConversationsPdp();
                return demoMockDataUtil.getHttpResponse(originalRequest, pdpResponse);
            } else if (path.contains("data/statistics.json")) {
                BulkRatingsResponse bulkRatingsResponse = demoMockDataUtil.getConversationsBulkRatings();
                return demoMockDataUtil.getHttpResponse(originalRequest, bulkRatingsResponse);
            } else if (path.contains("data/authors.json")) {
                AuthorsResponse authorsResponse = demoMockDataUtil.getConversationsAuthors();
                return demoMockDataUtil.getHttpResponse(originalRequest, authorsResponse);
            } else if (path.contains("data/reviewcomments.json")) {
                CommentsResponse commentsResponse = demoMockDataUtil.getConversationsComments();
                return demoMockDataUtil.getHttpResponse(originalRequest, commentsResponse);
            }
        } else if (host.contains("my.network-stg.bazaarvoice") || host.contains("my.network.bazaarvoice.com")) {
            if (path.contains("recommendations")) {
                ShopperProfile shopperProfile = demoMockDataUtil.getRecommendationsProfile();
                return demoMockDataUtil.getHttpResponse(originalRequest, shopperProfile);
            }
        }

        return chain.proceed(originalRequest);
    }

    private boolean isInvalidRequest(Chain chain) {
        return chain.request().url().toString().contains("REPLACE_ME");
    }

    private Response getInvalidResponse(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String doNotSendInvalidRequestsMessage = "{ \"demoInterceptMessage\" : \"Query params contain REPLACE_ME, so this is not being sent to the endpoint\" }";
        Response response = new Response.Builder()
            .code(403)
            .body(ResponseBody.create(MediaType.parse("json"), doNotSendInvalidRequestsMessage))
            .request(originalRequest)
            .protocol(Protocol.HTTP_2)
            .message("REPLACE_ME request cancelled")
            .build();
        return response;
    }
}
