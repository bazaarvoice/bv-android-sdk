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
package com.bazaarvoice.bvsdkdemoandroid.configs;

import com.bazaarvoice.bvandroidsdk.Answer;
import com.bazaarvoice.bvandroidsdk.AuthorsResponse;
import com.bazaarvoice.bvandroidsdk.BulkRatingsResponse;
import com.bazaarvoice.bvandroidsdk.CommentsResponse;
import com.bazaarvoice.bvandroidsdk.CurationsFeedResponse;
import com.bazaarvoice.bvandroidsdk.CurationsPostResponse;
import com.bazaarvoice.bvandroidsdk.FeaturesSentimentRequest;
import com.bazaarvoice.bvandroidsdk.FeaturesSentimentResponse;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageResponse;
import com.bazaarvoice.bvandroidsdk.Question;
import com.bazaarvoice.bvandroidsdk.QuestionAndAnswerResponse;
import com.bazaarvoice.bvandroidsdk.ReviewResponse;
import com.bazaarvoice.bvandroidsdk.ShopperProfile;
import com.bazaarvoice.bvandroidsdk.StoreReview;
import com.bazaarvoice.bvandroidsdk.StoreReviewResponse;
import com.bazaarvoice.bvandroidsdk.SummarisedFeaturesResponse;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoAssetsUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DemoMockDataUtil {
    private static final String AD_UNIT_ID = "/5705/bv-incubator/IncubatorEnduranceCycles";
    private ShopperProfile shopperProfile;
    private CurationsFeedResponse savedCurationsFeedResponse;
    private ReviewResponse savedConversationsReviews;
    private SummarisedFeaturesResponse savedSummarisedFeaturesResponse;
    private FeaturesSentimentResponse savedFeaturesSentimentResponse;
    private List<StoreReview> savedConversationsStoreReviews;
    private QuestionAndAnswerResponse savedConversationsQuestions;
    private ProductDisplayPageResponse savedConversationsPdp;
    private BulkRatingsResponse savedBulkRatings;
    private AuthorsResponse savedAuthors;
    private CommentsResponse savedComments;
    private CurationsPostResponse savedCurationsPostResponse;
    private final DemoAssetsUtil demoAssetsUtil;
    private final Gson gson;

    @Inject
    DemoMockDataUtil(Gson gson, DemoAssetsUtil demoAssetsUtil) {
        this.gson = gson;
        this.demoAssetsUtil = demoAssetsUtil;
    }

    public ShopperProfile getRecommendationsProfile() {
        if (shopperProfile != null) {
            return shopperProfile;
        }
        shopperProfile = demoAssetsUtil.parseJsonFileFromAssets(
            "recommendationsResult.json",
            ShopperProfile.class);
        return shopperProfile;
    }

    public CurationsFeedResponse getCurationsFeedReponse() {
        if (savedCurationsFeedResponse != null) {
            return savedCurationsFeedResponse;
        }
        savedCurationsFeedResponse = demoAssetsUtil.parseJsonFileFromAssets(
            "curationsEnduranceCycles.json",
            CurationsFeedResponse.class);
        return savedCurationsFeedResponse;
    }

    public ReviewResponse getConversationsReviews() {
        if (savedConversationsReviews != null) {
            return savedConversationsReviews;
        }
        savedConversationsReviews = demoAssetsUtil.parseJsonFileFromAssets(
            "conversationsReviewsEnduranceCycles.json",
            ReviewResponse.class);
        return savedConversationsReviews;
    }

    public SummarisedFeaturesResponse getSummarisedFeatures() {
        if (savedSummarisedFeaturesResponse != null) {
            return savedSummarisedFeaturesResponse;
        }
        savedSummarisedFeaturesResponse = demoAssetsUtil.parseJsonFileFromAssets(
                "summarised_features_response.json",
                SummarisedFeaturesResponse.class);
        return savedSummarisedFeaturesResponse;
    }

    public FeaturesSentimentResponse getFeaturesSentimentResponse() {
        if (savedFeaturesSentimentResponse != null) {
            return savedFeaturesSentimentResponse;
        }
        savedFeaturesSentimentResponse = demoAssetsUtil.parseJsonFileFromAssets(
                "retrieve_features _response.json",
                FeaturesSentimentResponse.class);
        return savedFeaturesSentimentResponse;
    }

    private String getResponseAsJsonString(Object response) {
        return gson.toJson(response);
    }

    public Response getHttpResponse(Request originalRequest, Object response) {
        Response httpResponse = new Response.Builder()
            .code(200)
            .body(ResponseBody.create(MediaType.parse("json"), getResponseAsJsonString(response)))
            .request(originalRequest)
            .protocol(Protocol.HTTP_2)
            .message("mock response")
            .build();
        return httpResponse;
    }

    public List<StoreReview> getConversationsStoreReviews() {
        if (savedConversationsReviews != null) {
            return savedConversationsStoreReviews;
        }
        StoreReviewResponse storeReviewResponse = demoAssetsUtil.parseJsonFileFromAssets(
            "conversationsReviewsEnduranceCycles.json",
            StoreReviewResponse.class);
        savedConversationsStoreReviews.addAll(storeReviewResponse.getResults());
        return savedConversationsStoreReviews;
    }

    public QuestionAndAnswerResponse getConversationsQuestions() {
        if (savedConversationsQuestions != null) {
            return savedConversationsQuestions;
        }
        savedConversationsQuestions = demoAssetsUtil.parseJsonFileFromAssets(
            "conversationsQuestionsIncludeAnswers.json",
            QuestionAndAnswerResponse.class);
        return savedConversationsQuestions;
    }

    public List<Answer> getConversationsAnswers(String questionId) {
        List<Question> conversationsQuestions = getConversationsQuestions().getResults();
        List<Answer> conversationsAnswers = new ArrayList<>();

        for (int i=0; i<conversationsQuestions.size(); i++) {
            Question currQuestion = conversationsQuestions.get(i);
            if (currQuestion.getId().equals(questionId)) {
                conversationsAnswers = currQuestion.getAnswers();
            }
        }

        return conversationsAnswers;
    }

    public ProductDisplayPageResponse getConversationsPdp() {
        if (savedConversationsPdp != null) {
            return savedConversationsPdp;
        }
        savedConversationsPdp =  demoAssetsUtil.parseJsonFileFromAssets(
            "conversationsProductsIncludeStats.json",
            ProductDisplayPageResponse.class);
        return savedConversationsPdp;
    }

    public BulkRatingsResponse getConversationsBulkRatings() {
        if (savedBulkRatings != null) {
            return savedBulkRatings;
        }
        savedBulkRatings = demoAssetsUtil.parseJsonFileFromAssets(
            "conversationsBulkRatings.json",
            BulkRatingsResponse.class);
        return savedBulkRatings;
    }

    public AuthorsResponse getConversationsAuthors() {
        if (savedAuthors != null) {
            return savedAuthors;
        }
        savedAuthors = demoAssetsUtil.parseJsonFileFromAssets(
            "conversationsAuthors.json",
            AuthorsResponse.class);
        return savedAuthors;
    }

    public CommentsResponse getConversationsComments() {
        if (savedComments != null) {
            return savedComments;
        }
        savedComments = demoAssetsUtil.parseJsonFileFromAssets(
            "conversationsComments.json",
            CommentsResponse.class
        );
        return savedComments;
    }

    public CurationsPostResponse getCurationsPostResponse() {
        if (savedCurationsPostResponse != null) {
            return savedCurationsPostResponse;
        }
        savedCurationsPostResponse = demoAssetsUtil.parseJsonFileFromAssets(
            "post_successfulCreation.json",
            CurationsPostResponse.class);
        return savedCurationsPostResponse;
    }

    public String getAdUnitId() {
        return AD_UNIT_ID;
    }
}
