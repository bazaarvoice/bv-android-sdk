/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  @deprecated - Old API to be removed
 */
public class BazaarAnswer {

    private String answerText;
    private String userNickname;
    private Date submissionTime;
    private int positiveFeedbackCount;
    private int feedbackCount;

    public BazaarAnswer(JSONObject answerJsonObj) throws JSONException {
        this.answerText = answerJsonObj.getString("AnswerText");
        this.userNickname = answerJsonObj.getString("UserNickname");
        this.positiveFeedbackCount = answerJsonObj.getInt("TotalPositiveFeedbackCount");
        this.feedbackCount = answerJsonObj.getInt("TotalFeedbackCount");
        String submissionTimeStr = answerJsonObj.getString("SubmissionTime");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            submissionTime = sdf.parse(submissionTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getAnswerText() {
        return answerText;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public Date getSubmissionTime() {
        return submissionTime;
    }

    public int getPositiveFeedbackCount() {
        return positiveFeedbackCount;
    }

    public int getFeedbackCount() {
        return feedbackCount;
    }
}
