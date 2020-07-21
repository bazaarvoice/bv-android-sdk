package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ReviewHighlightsResponse  extends ConversationsDisplayResponse {

   @SerializedName("subjects") private ReviewHighlights reviewHighlights;

    public  ReviewHighlights getReviewHighlights() {
        return reviewHighlights;
    }

    public Boolean getHasErrors() {
        //TODO error handling changes
        return false;
    }
}
