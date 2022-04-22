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

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Averages for ratings for specific qualities
 */
public class SecondaryRatingsAverages {
    @SerializedName("Id")
    private String id;
    @SerializedName("AverageRating")
    private Float averageOverallRating;
    @SerializedName("MaxLabel")
    private String maxLabel;
    @SerializedName("DisplayType")
    private String displayType;
    @SerializedName("MinLabel")
    private String minLabel;
    @SerializedName("Label")
    private String label;
    @SerializedName("ValueLabel")
    private List<String> valueLabel;
    @SerializedName("ValueRange")
    private Integer valueRange;

    public Float getAverageOverallRating() {
        return averageOverallRating;
    }

    public String getId() {
        return id;
    }

    public String getMaxLabel() {
        return maxLabel;
    }

    public String getDisplayType() {
        return displayType;
    }

    public String getMinLabel() {
        return minLabel;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getValueLabel() {
        return valueLabel;
    }

    public Integer getValueRange() {
        return valueRange;
    }
}