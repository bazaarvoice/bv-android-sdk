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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * A Bazaarvoice Product from the Curations API
 */
public class CurationsProduct implements BVDisplayableProductContent{

    //public Brand Brand;
    @SerializedName("Description")
    protected String productDescription;
    //public Attributes att
    @SerializedName("ProductPageUrl")
    protected String productPageUrl;

    @SerializedName("ImageUrl")
    protected String productImageUrl;
    @SerializedName("Name")
    protected String productName;
    @SerializedName("ReviewStatistics")
    protected ReviewStatistics productReviewStatistics;
    @SerializedName("Id")
    protected String id;

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductPageUrl() {
        return productPageUrl;
    }

    @Deprecated //Use getDisplayImageUrl
    public String getProductImageUrl() {
        return productImageUrl;
    }

    @Deprecated //Use getDisplayName
    public String getProductName() {
        return productName;
    }

    public ReviewStatistics getProductReviewStatistics() {
        return productReviewStatistics;
    }

    public class ReviewStatistics{
        @SerializedName("AverageOverallRating")
        protected float avgRating;

        @SerializedName("TotalReviewCount")
        protected int numReviews;

        public int getNumReviews() {
            return numReviews;
        }

        public float getAvgRating() {
            return avgRating;
        }
    }


    @Override @NonNull
    public String getId() {
        return id;
    }

    @Override @Nullable
    public String getDisplayName() {
        return productName;
    }

    @Override @Nullable
    public String getDisplayImageUrl() {
        return productImageUrl;
    }

    @Override
    public float getAverageRating() {
        return productReviewStatistics.getAvgRating();
    }

}
