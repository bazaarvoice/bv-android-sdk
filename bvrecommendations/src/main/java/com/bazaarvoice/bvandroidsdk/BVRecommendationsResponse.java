package com.bazaarvoice.bvandroidsdk;

import java.util.ArrayList;
import java.util.List;

public class BVRecommendationsResponse {

    private boolean didSucceed;
    private Throwable errorThrowable;
    private ShopperProfile shopperProfile;

    BVRecommendationsResponse(boolean didSucceed, Throwable errorThrowable, ShopperProfile shopperProfile) {
        this.didSucceed = didSucceed;
        this.errorThrowable = errorThrowable;
        this.shopperProfile = shopperProfile;
    }

    boolean isDidSucceed() {
        return didSucceed;
    }

    Throwable getErrorThrowable() {
        return errorThrowable;
    }

    public List<BVProduct> getRecommendedProducts() {
        if (shopperProfile != null
                && shopperProfile.getProfile() != null
                && shopperProfile.getProfile().getRecommendedProducts() != null) {
            return shopperProfile.getProfile().getRecommendedProducts();
        }
        return new ArrayList<>();
    }

    ShopperProfile getShopperProfile() {
        return shopperProfile;
    }
}
