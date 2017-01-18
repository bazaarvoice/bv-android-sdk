/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;

/**
 * Number of ratings for each of the star counts.
 * e.g.
 * <ul>
 *     <li>10 1 star reviews</li>
 *     <li>5 2 star reviews</li>
 *     <li>12 3 star reviews</li>
 *     <li>83 4 star reviews</li>
 *     <li>142 5 star reviews</li>
 * </ul>
 */
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