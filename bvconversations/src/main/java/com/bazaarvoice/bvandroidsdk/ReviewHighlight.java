package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewHighlight {
    @SerializedName("presenceCount") public Integer presenceCount;
    @SerializedName("mentionsCount") public Integer mentionsCount;
    @SerializedName("bestExamples")
    public ArrayList<ReviewHighligtsReview> bestExamples;
    public String title;

}
