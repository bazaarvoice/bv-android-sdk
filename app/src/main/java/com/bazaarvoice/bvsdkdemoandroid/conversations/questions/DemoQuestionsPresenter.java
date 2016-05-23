/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.questions;

import android.util.Log;

import com.bazaarvoice.bvandroidsdk.BazaarRequest;
import com.bazaarvoice.bvandroidsdk.DisplayParams;
import com.bazaarvoice.bvandroidsdk.OnBazaarResponse;
import com.bazaarvoice.bvandroidsdk.types.Equality;
import com.bazaarvoice.bvandroidsdk.types.IncludeStatsType;
import com.bazaarvoice.bvandroidsdk.types.IncludeType;
import com.bazaarvoice.bvandroidsdk.types.RequestType;
import com.bazaarvoice.bvsdkdemoandroid.conversations.BazaarQuestion;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DemoQuestionsPresenter implements DemoQuestionsContract.UserActionsListener, OnBazaarResponse {

    private DemoQuestionsContract.View view;
    private DemoConfigUtils demoConfigUtils;
    private DemoDataUtil demoDataUtil;
    private String productId;
    private boolean fetched = false;

    public DemoQuestionsPresenter(DemoQuestionsContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, String productId) {
        this.view = view;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
        this.productId = productId;
    }

    @Override
    public void loadQuestions(boolean forceRefresh) {
        fetched = false;
        DemoConfig currentConfig = demoConfigUtils.getCurrentConfig();
        if (currentConfig.isDemoClient()) {
            showQuestions(demoDataUtil.getConversationsQuestions());
            return;
        }

        List<BazaarQuestion> cachedQuestions = DemoQuestionsCache.getInstance().getDataItem(productId);
        boolean haveLocalCache = cachedQuestions!=null;
        boolean shouldHitNetwork = forceRefresh || !haveLocalCache;

        if (shouldHitNetwork) {
            view.showLoadingQuestions(true);
            BazaarRequest bazaarRequest = new BazaarRequest();
            DisplayParams displayParams = new DisplayParams();
            displayParams.addFilter("ProductId", Equality.EQUAL, productId);
            displayParams.addInclude(IncludeType.PRODUCTS);
            displayParams.addInclude(IncludeType.ANSWERS);
            displayParams.addStats(IncludeStatsType.QUESTIONS);
            displayParams.setLimit(50);
            bazaarRequest.sendDisplayRequest(RequestType.QUESTIONS, displayParams, this);
        } else {
            showQuestions(cachedQuestions);
        }
    }

    @Override
    public void onQandATapped() {
        List<BazaarQuestion> cachedQuestions = DemoQuestionsCache.getInstance().getDataItem(productId);
        int numQs = cachedQuestions != null ? cachedQuestions.size() : 0;
        if (fetched && numQs > 0) {
            view.transitionToQandA();
        } else if (fetched) {
            view.showAskQuestionDialog();
        }
    }

    private void showQuestions(List<BazaarQuestion> questions) {
        fetched = true;
        view.showLoadingQuestions(false);
        DemoQuestionsCache.getInstance().putDataItem(productId, questions);

        if (questions.size() > 0) {
            view.showQuestions(questions);
        } else {
            view.showNoQuestions();
        }
    }

    @Override
    public void onResponse(String url, JSONObject response) {
        Log.d("QuestionsPresent", "url: " + url);
        List<BazaarQuestion> bazaarQuestions = new ArrayList<>();
        JSONArray resultsJsonArray = null;
        try {
            resultsJsonArray = response.getJSONArray("Results");
            JSONObject includesJSONObj = response.getJSONObject("Includes");
            JSONObject answersJSONObj = includesJSONObj.getJSONObject("Answers");
            for (int i=0; i<resultsJsonArray.length(); i++) {
                JSONObject questionJsonObj = resultsJsonArray.getJSONObject(i);
                BazaarQuestion bazaarQuestion = new BazaarQuestion(questionJsonObj, answersJSONObj);
                bazaarQuestions.add(bazaarQuestion);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showQuestions(bazaarQuestions);
    }

    @Override
    public void onException(String message, Throwable exception) {
        exception.printStackTrace();
        showQuestions(Collections.<BazaarQuestion>emptyList());
    }
}
