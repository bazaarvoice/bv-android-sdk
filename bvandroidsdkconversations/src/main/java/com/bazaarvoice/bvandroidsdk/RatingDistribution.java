/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

public class RatingDistribution {
    private final Integer oneStarCount;
    private final Integer twoStarCount;
    private final Integer threeStarCount;
    private final Integer fourStarCount;
    private final Integer fiveStarCount;

    RatingDistribution(Integer oneStarCount, Integer twoStarCount, Integer threeStarCount, Integer fourStarCount, Integer fiveStarCount) {
        this.oneStarCount = oneStarCount;
        this.twoStarCount = twoStarCount;
        this.threeStarCount = threeStarCount;
        this.fourStarCount = fourStarCount;
        this.fiveStarCount = fiveStarCount;
    }

    public Integer getOneStarCount() {
        return oneStarCount;
    }

    public Integer getTwoStarCount() {
        return twoStarCount;
    }

    public Integer getThreeStarCount() {
        return threeStarCount;
    }

    public Integer getFourStarCount() {
        return fourStarCount;
    }

    public Integer getFiveStarCount() {
        return fiveStarCount;
    }
}