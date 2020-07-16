package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class ReviewHighlight {
    @SerializedName("presenceCount") private Integer presenceCount;
    @SerializedName("mentionsCount") public Integer mentionsCount;
    @SerializedName("bestExamples")
    private ArrayList<ReviewHighligtsReview> bestExamples;
    public String title;

}
