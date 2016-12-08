/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

public class RatingDistribution {
    private final Integer oneStarCount;
    private final Integer twoStarCount;
    private final Integer threeStarCount;
    private final Integer fourStarCount0;
    private final Integer fiveStarCount;

    RatingDistribution(Integer oneStarCount, Integer twoStarCount, Integer threeStarCount, Integer fourStarCount0, Integer fiveStarCount) {
        this.oneStarCount = oneStarCount;
        this.twoStarCount = twoStarCount;
        this.threeStarCount = threeStarCount;
        this.fourStarCount0 = fourStarCount0;
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
        return fourStarCount0;
    }

    public Integer getFiveStarCount() {
        return fiveStarCount;
    }
}