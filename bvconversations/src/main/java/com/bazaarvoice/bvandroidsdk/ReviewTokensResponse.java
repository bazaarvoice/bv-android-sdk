package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReviewTokensResponse extends ConversationsDisplayResponse {
    @Override
    public Boolean getHasErrors() {
        Boolean hasErrors = super.getHasErrors();
        if (hasErrors != null) return hasErrors;
        if (status > 0) return status >= 400;
        return isNonEmpty(type) || isNonEmpty(title) || isNonEmpty(detail);
    }
    private static boolean isNonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }
    @SerializedName("data")
    private List<String> data;

    @SerializedName("status")
    private int status;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("detail")
    private String detail;

    public List<String> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }
}
