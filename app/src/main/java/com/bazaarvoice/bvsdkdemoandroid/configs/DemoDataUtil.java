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

import android.content.Context;

import com.bazaarvoice.bvandroidsdk.Answer;
import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsFeedResponse;
import com.bazaarvoice.bvandroidsdk.CurationsPostResponse;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.ProductDisplayPageResponse;
import com.bazaarvoice.bvandroidsdk.Question;
import com.bazaarvoice.bvandroidsdk.QuestionAndAnswerResponse;
import com.bazaarvoice.bvandroidsdk.Review;
import com.bazaarvoice.bvandroidsdk.ReviewResponse;
import com.bazaarvoice.bvandroidsdk.ShopperProfile;
import com.bazaarvoice.bvandroidsdk.StoreReview;
import com.bazaarvoice.bvandroidsdk.StoreReviewResponse;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DemoDataUtil {

    private static final String AD_UNIT_ID = "/5705/bv-incubator/IncubatorEnduranceCycles";
    private Context applicationContext;
    private List<BVProduct> savedDemoRecProds;
    private CurationsFeedResponse savedCurationsFeedResponse;
    private List<Review> savedConversationsReviews;
    private List<StoreReview> savedConversationsStoreReviews;
    private List<Question> savedConversationsQuestions;
    private Gson gson;
    private CurationsPostResponse curationsPostResponse;

    @Inject
    DemoDataUtil(Context context, Gson gson) {
        applicationContext = context;
        this.gson = gson;
    }

    public List<BVProduct> getRecommendedProducts() {
        if (savedDemoRecProds != null) {
            return savedDemoRecProds;
        }

        List<BVProduct> bvProducts = new ArrayList<>();
        try {
            InputStream inputStream = applicationContext.getAssets().open("recommendationsResult.json");
            Reader reader = new InputStreamReader(inputStream);
            ShopperProfile shopperProfile = gson.fromJson(reader, ShopperProfile.class);
            bvProducts = shopperProfile.getProfile().getRecommendedProducts();
            savedDemoRecProds = bvProducts;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bvProducts;
    }

    private CurationsFeedResponse getCurationsFeedReponse() {
        if (savedCurationsFeedResponse != null) {
            return savedCurationsFeedResponse;
        }

        CurationsFeedResponse curationsFeedResponse = null;
        try {
            InputStream inputStream = applicationContext.getAssets().open("curationsEnduranceCycles.json");
            Reader reader = new InputStreamReader(inputStream);
            curationsFeedResponse = gson.fromJson(reader, CurationsFeedResponse.class);
            savedCurationsFeedResponse = curationsFeedResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return curationsFeedResponse;
    }

    public List<CurationsFeedItem> getCurationsFeedItems() {
        return getCurationsFeedReponse().getUpdates();
    }

    public String getCurationsFeedResponseJsonString() {
        return gson.toJson(getCurationsFeedReponse());
    }

    public List<Review> getConversationsReviews() {
        if (savedConversationsReviews != null) {
            return savedConversationsReviews;
        }

        List<Review> conversationsReviews = new ArrayList<>();
        try {
            InputStream inputStream = applicationContext.getAssets().open("conversationsReviewsEnduranceCycles.json");
            conversationsReviews.addAll(convertResponseToReviews(inputStream));
            savedConversationsReviews = conversationsReviews;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return conversationsReviews;
    }

    public List<StoreReview> getConversationsStoreReviews() {
        if (savedConversationsReviews != null) {
            return savedConversationsStoreReviews;
        }

        List<StoreReview> conversationsReviews = new ArrayList<>();
        try {
            InputStream inputStream = applicationContext.getAssets().open("conversationsReviewsEnduranceCycles.json");
            conversationsReviews.addAll(convertResponseToStoreReviews(inputStream));
            savedConversationsStoreReviews = conversationsReviews;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return conversationsReviews;
    }

    private String readResponse(InputStream inputStream) throws IOException {
        BufferedReader reader =  new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();

        String line;
        while ((line=reader.readLine()) != null) {
            response.append(line);
        }

        return response.toString();
    }

    private List<Review> convertResponseToReviews(InputStream inputStream) throws JSONException, IOException {
        String jsonResponseStr = readResponse(inputStream);
        ReviewResponse reviewResponse = gson.fromJson(jsonResponseStr, ReviewResponse.class);
        return reviewResponse.getResults();
    }

    private List<StoreReview> convertResponseToStoreReviews(InputStream inputStream) throws JSONException, IOException {
        String jsonResponseStr = readResponse(inputStream);
        StoreReviewResponse reviewResponse = gson.fromJson(jsonResponseStr, StoreReviewResponse.class);
        return reviewResponse.getResults();
    }

    public List<Question> getConversationsQuestions() {
        if (savedConversationsQuestions != null) {
            return savedConversationsQuestions;
        }

        List<Question> conversationQuestions = new ArrayList<>();
        try {
            InputStream inputStream = applicationContext.getAssets().open("conversationsQuestionsIncludeAnswers.json");
            conversationQuestions = convertResponseToQuestions(inputStream);
            savedConversationsQuestions = conversationQuestions;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return conversationQuestions;
    }

    private List<Question> convertResponseToQuestions(InputStream inputStream) throws JSONException, IOException {
        String jsonResponseStr = readResponse(inputStream);
        QuestionAndAnswerResponse response = gson.fromJson(jsonResponseStr, QuestionAndAnswerResponse.class);
        return response.getResults();
    }

    public List<Answer> getConversationsAnswers(String questionId) {
        List<Question> conversationsQuestions = getConversationsQuestions();
        List<Answer> conversationsAnswers = new ArrayList<>();

        for (int i=0; i<conversationsQuestions.size(); i++) {
            Question currQuestion = conversationsQuestions.get(i);
            if (currQuestion.getId().equals(questionId)) {
                conversationsAnswers = currQuestion.getAnswers();
            }
        }

        return conversationsAnswers;
    }

    public Product getBazaarProductWithStats() {
        Product bazaarProduct = null;
        try {
            InputStream inputStream = applicationContext.getAssets().open("conversationsProductsIncludeStats.json");
            bazaarProduct = convertResponseToBazaarProduct(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bazaarProduct;
    }

    private Product convertResponseToBazaarProduct(InputStream inputStream) throws JSONException, IOException {
        String jsonResponseStr = readResponse(inputStream);
        ProductDisplayPageResponse response = gson.fromJson(jsonResponseStr, ProductDisplayPageResponse.class);
        return response.getResults().get(0);
    }

    public CurationsPostResponse getCurationsPostResponse() {
        CurationsPostResponse curationsPostResponse = null;
        try {
            InputStream inputStream = applicationContext.getAssets().open("post_successfulCreation.json");
            Reader reader = new InputStreamReader(inputStream);
            curationsPostResponse = gson.fromJson(reader, CurationsPostResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return curationsPostResponse;
    }

    public String getCurationsPostResponseJsonString() {
        return gson.toJson(getCurationsPostResponse());
    }

    public String getAdUnitId() {
        return AD_UNIT_ID;
    }
}
