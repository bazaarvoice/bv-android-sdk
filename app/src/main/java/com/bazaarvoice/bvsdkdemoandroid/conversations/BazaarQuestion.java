/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils.safeParse;
import static com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils.safeParseString;

/**
 * @deprecated - Old API to be removed
 */
public class BazaarQuestion {

    private String productId;
    private String questionSummary;
    private String questionDetails;
    private String userNickname;
    private String id;
    private Date submissionTime;
    private int totalAnswerCount = 0;
    private List<BazaarAnswer> bazaarAnswers;

    public BazaarQuestion(JSONObject questionJsonObj, JSONObject answersJsonObj) throws JSONException {
        productId = safeParseString("ProductId", questionJsonObj);
        questionSummary = safeParseString("QuestionSummary", questionJsonObj);
        questionDetails = safeParseString("QuestionDetails", questionJsonObj);
        userNickname = safeParseString("UserNickname", questionJsonObj);
        totalAnswerCount = safeParse("TotalAnswerCount", questionJsonObj);
        String submissionTimeStr = safeParseString("SubmissionTime", questionJsonObj);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            submissionTime = sdf.parse(submissionTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        bazaarAnswers = new ArrayList<>();
        JSONArray answerIdsJSONArray = questionJsonObj.getJSONArray("AnswerIds");
        for (int i=0; i<answerIdsJSONArray.length(); i++) {
            String answerId = answerIdsJSONArray.getString(i);
            JSONObject answerJSONObj = answersJsonObj.getJSONObject(answerId);
            bazaarAnswers.add(new BazaarAnswer(answerJSONObj));
        }

        id = questionJsonObj.getString("Id");
    }

    public String getProductId() {
        return productId;
    }

    public int getTotalAnswerCount() {
        return totalAnswerCount;
    }

    public String getQuestionSummary() {
        return questionSummary;
    }

    public String getQuestionDetails() {
        return questionDetails;
    }

    public Date getSubmissionTime() {
        return submissionTime;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public String getId() {
        return id;
    }

    public List<BazaarAnswer> getBazaarAnswers() {
        return bazaarAnswers;
    }
}
