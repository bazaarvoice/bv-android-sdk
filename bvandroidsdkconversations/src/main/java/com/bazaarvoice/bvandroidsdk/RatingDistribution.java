/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

public class RatingDistribution {
    private final Integer oneStarCount;
    private final Integer twoStarCount;
    private final Integer threeStarCount0;
    private final Integer fourStarCount0;
    private final Integer fiveStarCount;

    RatingDistribution(Integer oneStarCount, Integer twoStarCount, Integer threeStarCount0, Integer fourStarCount0, Integer fiveStarCount) {
        this.oneStarCount = oneStarCount;
        this.twoStarCount = twoStarCount;
        this.threeStarCount0 = threeStarCount0;
        this.fourStarCount0 = fourStarCount0;
        this.fiveStarCount = fiveStarCount;
    }

    public Integer getOneStarCount() {
        return oneStarCount;
    }

    public Integer getTwoStarCount() {
        return twoStarCount;
    }

    public Integer getThreeStarCount0() {
        return threeStarCount0;
    }

    public Integer getFourStarCount0() {
        return fourStarCount0;
    }

    public Integer getFiveStarCount() {
        return fiveStarCount;
    }
}