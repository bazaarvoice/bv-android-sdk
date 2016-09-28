/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConversationsStoresInclude {

    @SerializedName("Products")
    private Map<String, Store> storeMap;
    @SerializedName("Reviews")
    private Map<String, StoreReview> reviewMap;

    private transient List<Store> stores;
    private transient List<StoreReview> reviews;

    protected Map<String, Store> getStoreMap() {
        return storeMap;
    }

    protected Map<String, StoreReview> getReviewMap() {
        return reviewMap;
    }

    public List<StoreReview> getReviews() {
        if (this.reviews == null && this.reviewMap != null) {
            this.reviews = processContent(this.reviewMap);
        }
        return reviews;
    }

    public List<Store> getStores() {
        if (this.stores == null) {
            this.stores = processContent(this.storeMap);
        }
        return this.stores;
    }

    private <T extends IncludeableStoresContent> List<T> processContent(Map<String, T> contents) {
        List<T> contentList = new ArrayList<>();
        for (T content : contents.values()) {
            content.setIncludedIn(this);
            contentList.add(content);
        }

        return contentList;
    }
}