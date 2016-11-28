/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class SecondaryRatingsAverages {
    @SerializedName("AverageRating")
    private Float averageOverallRating;

    public Float getAverageOverallRating() {
        return averageOverallRating;
    }
}