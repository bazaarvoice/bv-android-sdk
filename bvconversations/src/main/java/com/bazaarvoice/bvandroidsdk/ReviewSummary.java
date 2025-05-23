package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class ReviewSummary extends IncludeableContent {
    @SerializedName("summary")
    private String summary;
    @SerializedName("status")
    private Integer status;
    @SerializedName("type")
    private String type;
    @SerializedName("title")
    private String title;
    @SerializedName("detail")
    private String detail;
    @SerializedName("disclaimer")
    private String disclaimer;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

}
