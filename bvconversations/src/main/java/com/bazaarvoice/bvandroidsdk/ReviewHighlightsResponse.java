package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewHighlightsResponse extends IncludesResponse<ReviewHighlights, ConversationsIncludeProduct>  {

    @SerializedName("subjects") private ReviewHighlights reviewHighlights;

    @Override
    public Boolean getHasErrors() {
        return false;
    }
}
