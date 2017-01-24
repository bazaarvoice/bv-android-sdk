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

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public final class Author extends IncludedContentBase {

    @SerializedName("Location")
    private String location;

    @SerializedName("ReviewStatistics")
    private ReviewStatistics reviewStatistics;
    @SerializedName("QAStatistics")
    private QAStatistics qaStatistics;
    @SerializedName("SecondaryRatings")
    private Map<String, SecondaryRating> secondaryRatings;
    @SerializedName("TagDimensions")
    private Map<String, DimensionElement> tagDimensions;

    public String getLocation() {
        return location;
    }

    @Nullable
    public ReviewStatistics getReviewStatistics() {
        return reviewStatistics;
    }


    @Nullable
    public QAStatistics getQaStatistics() {
        return qaStatistics;
    }

    public Map<String, SecondaryRating> getSecondaryRatings() {
        return secondaryRatings;
    }

    public Map<String, DimensionElement> getTagDimensions() {
        return tagDimensions;
    }
}
