package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ReviewHighlights extends IncludedContentBase.ProductIncludedContentBase{
    @SerializedName("positive")
    private Map<String, ReviewHighlight> positive;
    @SerializedName("negative")
    private Map<String, ReviewHighlight> negative;

}