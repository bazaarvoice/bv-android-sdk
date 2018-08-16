/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.RecommendationView;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DemoProductDetailRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Inject Picasso picasso;

    private List<BVProduct> recommendedProducts = Collections.emptyList();

    public interface ProductTapListener {
        void onProductTapListener(BVProduct bvProduct);
    }

    private ProductTapListener productTapListener;

    public DemoProductDetailRecAdapter(Context context) {
        DemoApp.getAppComponent(context).inject(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recSnippetRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_product_rec_snippet_cell, parent, false);
        return new DemoProductDetailViewHolder(recSnippetRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BVProduct bvProduct = recommendedProducts.get(position);
        DemoProductDetailViewHolder demoViewHolder = (DemoProductDetailViewHolder) holder;

        picasso.load(bvProduct.getDisplayImageUrl())
                .resizeDimen(R.dimen.side_not_set, R.dimen.snippet_prod_image_side)
                .into(demoViewHolder.image);
        demoViewHolder.productName.setText(bvProduct.getDisplayName());
        demoViewHolder.productRating.setRating(5); // TODO RATING (int) bvProduct.getAverageRating());

        demoViewHolder.recommendationView.setBvProduct(bvProduct);

        demoViewHolder.row.setTag(bvProduct);
        demoViewHolder.row.setOnClickListener(v -> {
            if (productTapListener != null) {
                productTapListener.onProductTapListener((BVProduct) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendedProducts.size();
    }

    public void refreshRecs(List<BVProduct> bvProducts) {
        this.recommendedProducts = bvProducts;
        notifyDataSetChanged();
    }

    public void setProductTapListener(ProductTapListener productTapListener) {
        this.productTapListener = productTapListener;
    }

    private final class DemoProductDetailViewHolder extends RecyclerView.ViewHolder {
        RecommendationView recommendationView;
        RelativeLayout row;
        ImageView image;
        TextView productName;
        RatingBar productRating;

        public DemoProductDetailViewHolder(View itemView) {
            super(itemView);
            recommendationView = (RecommendationView) itemView;
            row = itemView.findViewById(R.id.rec_snippet_container);
            image = itemView.findViewById(R.id.productThumbnailImage);
            productName = itemView.findViewById(R.id.product_name);
            productRating = itemView.findViewById(R.id.product_rating);
        }
    }
}
