package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReviewSummaryResponse extends ConversationsDisplayResponse {
    @SerializedName("status")
    private Integer status;
    @SerializedName("summary")
    private String summary;
    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("detail")
    private String detail;

    @SerializedName("disclaimer")
    private String disclaimer;

    @SerializedName("error")
    private String error;

    private transient List<Error> errors;

    // Getters
    public Integer getStatus() { return status; }
    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getDetail() { return detail; }
    public String getDisclaimer() { return disclaimer; }

    public Boolean getHasErrors() {
        return error != null;
    }

    public List<Error> getErrors() {
        if (errors == null) {
            Error reviewSummaryError = new Error(error, null);
            errors = new ArrayList<>(Collections.singletonList(reviewSummaryError));
        }
        return errors;
    }

    // Optional: Helper to convert to ReviewSummary
    public ReviewSummary getReviewSummary() {
        ReviewSummary reviewSummary = new ReviewSummary();
        reviewSummary.setStatus(status);
        reviewSummary.setSummary(summary);
        reviewSummary.setType(type);
        reviewSummary.setTitle(title);
        reviewSummary.setDetail(detail);
        reviewSummary.setDisclaimer(disclaimer);
        return reviewSummary;
    }

}

