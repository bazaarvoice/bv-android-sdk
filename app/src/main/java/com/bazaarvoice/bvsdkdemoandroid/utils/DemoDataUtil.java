/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.utils;

import android.content.Context;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsFeedResponse;
import com.bazaarvoice.bvandroidsdk.CurationsPostResponse;
import com.bazaarvoice.bvandroidsdk.ShopperProfile;
import com.bazaarvoice.bvsdkdemoandroid.conversations.BazaarAnswer;
import com.bazaarvoice.bvsdkdemoandroid.conversations.BazaarProduct;
import com.bazaarvoice.bvsdkdemoandroid.conversations.BazaarQuestion;
import com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts.BazaarReview;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class DemoDataUtil {

    private static DemoDataUtil instance;

    private Context applicationContext;
    private List<BVProduct> savedDemoRecProds;
    private List<CurationsFeedItem> savedCurationsFeedItems;
    private List<BazaarReview> savedConversationsReviews;
    private List<BazaarQuestion> savedConversationsQuestions;
    private Gson gson;
    private CurationsPostResponse curationsPostResponse;

    private DemoDataUtil(Context context) {
        applicationContext = context.getApplicationContext();
        gson = new Gson();
    }

    public static DemoDataUtil getInstance(Context context) {
        if (instance == null) {
            instance = new DemoDataUtil(context);
        }

        return instance;
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

    public List<CurationsFeedItem> getCurationsFeedItems() {
        if (savedCurationsFeedItems != null) {
            return savedCurationsFeedItems;
        }

        List<CurationsFeedItem> curationsFeedItems = new ArrayList<>();
        try {
            InputStream inputStream = applicationContext.getAssets().open("curationsEnduranceCycles.json");
            Reader reader = new InputStreamReader(inputStream);
            CurationsFeedResponse curationsFeedResponse = gson.fromJson(reader, CurationsFeedResponse.class);
            curationsFeedItems = curationsFeedResponse.getUpdates();
            savedCurationsFeedItems = curationsFeedItems;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return curationsFeedItems;
    }

    public List<BazaarReview> getConversationsReviews() {
        if (savedConversationsReviews != null) {
            return savedConversationsReviews;
        }

        List<BazaarReview> conversationsReviews = new ArrayList<>();
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

    private String readResponse(InputStream inputStream) throws IOException {
        BufferedReader reader =  new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();

        String line;
        while ((line=reader.readLine()) != null) {
            response.append(line);
        }

        return response.toString();
    }

    private List<BazaarReview> convertResponseToReviews(InputStream inputStream) throws JSONException, IOException {
        List<BazaarReview> bazaarReviewList = new ArrayList<>();
        String jsonResponseStr = readResponse(inputStream);
        JSONObject jsonObject = new JSONObject(jsonResponseStr);
        JSONArray resultsJsonArray = jsonObject.getJSONArray("Results");
        for (int i=0; i<resultsJsonArray.length(); i++) {
            JSONObject reviewJsonObject = resultsJsonArray.getJSONObject(i);
            BazaarReview bazaarReview = new BazaarReview(reviewJsonObject);
            bazaarReviewList.add(bazaarReview);
        }
        return bazaarReviewList;
    }

    public List<BazaarQuestion> getConversationsQuestions() {
        if (savedConversationsQuestions != null) {
            return savedConversationsQuestions;
        }

        List<BazaarQuestion> conversationQuestions = new ArrayList<>();
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

    private List<BazaarQuestion> convertResponseToQuestions(InputStream inputStream) throws JSONException, IOException {
        List<BazaarQuestion> bazaarQuestions = new ArrayList<>();
        String jsonResponseStr = readResponse(inputStream);
        JSONObject response = new JSONObject(jsonResponseStr);
        JSONArray resultsJsonArray = response.getJSONArray("Results");
        JSONObject includesJSONObj = response.getJSONObject("Includes");
        JSONObject answersJSONObj = includesJSONObj.getJSONObject("Answers");
        for (int i=0; i<resultsJsonArray.length(); i++) {
            JSONObject questionJsonObj = resultsJsonArray.getJSONObject(i);
            BazaarQuestion bazaarQuestion = new BazaarQuestion(questionJsonObj, answersJSONObj);
            bazaarQuestions.add(bazaarQuestion);
        }
        return bazaarQuestions;
    }

    public List<BazaarAnswer> getConversationsAnswers(String questionId) {
        List<BazaarQuestion> conversationsQuestions = getConversationsQuestions();
        List<BazaarAnswer> conversationsAnswers = new ArrayList<>();

        for (int i=0; i<conversationsQuestions.size(); i++) {
            BazaarQuestion currQuestion = conversationsQuestions.get(i);
            if (currQuestion.getId().equals(questionId)) {
                conversationsAnswers = currQuestion.getBazaarAnswers();
            }
        }

        return conversationsAnswers;
    }

    public BazaarProduct getBazaarProductWithStats() {
        BazaarProduct bazaarProduct = null;
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

    private BazaarProduct convertResponseToBazaarProduct(InputStream inputStream) throws JSONException, IOException {
        String jsonResponseStr = readResponse(inputStream);
        JSONObject response = new JSONObject(jsonResponseStr);
        JSONArray resultsJsonArray = response.getJSONArray("Results");
        JSONObject productJsonObj = resultsJsonArray.getJSONObject(0);
        return new BazaarProduct(productJsonObj);
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
}
