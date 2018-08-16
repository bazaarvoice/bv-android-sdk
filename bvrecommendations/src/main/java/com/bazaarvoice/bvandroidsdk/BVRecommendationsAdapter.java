package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public abstract class BVRecommendationsAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    ShopperProfile shopperProfile;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(!(holder.itemView instanceof RecommendationView)) {
            throw new IllegalStateException("ItemView must be of type RecommendationView");
        }

        if(shopperProfile != null &&
                shopperProfile.getProfile() != null
                && shopperProfile.getProfile().getRecommendedProducts() != null) {
            RecommendationView recommendationView = (RecommendationView) holder.itemView;
            recommendationView.setBvProduct(shopperProfile.getProfile().getRecommendedProducts().get(position));
        }

    }

    public void setShopperProfile(ShopperProfile shopperProfile) {
        this.shopperProfile = shopperProfile;
    }
}
