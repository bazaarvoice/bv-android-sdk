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

/**
 * Statistics for a product
 */
public class ProductStatistics {

    @SerializedName("ReviewStatistics")
    private ReviewStatistics reviewStatistics;
    @SerializedName("NativeReviewStatistics")
    private ReviewStatistics nativeReviewStatistics;
    @SerializedName("QAStatistics")
    private QAStatistics qaStatistics;
    @SerializedName("ProductId")
    private String productId;

    public ReviewStatistics getReviewStatistics() {
        return reviewStatistics;
    }

    public ReviewStatistics getNativeReviewStatistics() {
        return nativeReviewStatistics;
    }

    public QAStatistics getQAStatistics() {
        return qaStatistics;
    }

    public String getProductId() {
        return productId;
    }
}