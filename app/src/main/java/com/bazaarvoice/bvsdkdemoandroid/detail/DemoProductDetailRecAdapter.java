/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.detail;

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
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;

import java.util.Collections;
import java.util.List;

public class DemoProductDetailRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BVProduct> recommendedProducts = Collections.emptyList();

    public interface ProductTapListener {
        void onProductTapListener(BVProduct bvProduct);
    }

    private ProductTapListener productTapListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recSnippetRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_product_rec_snippet_cell, parent, false);
        return new DemoProductDetailViewHolder(recSnippetRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BVProduct bvProduct = recommendedProducts.get(position);
        DemoProductDetailViewHolder demoViewHolder = (DemoProductDetailViewHolder) holder;

        DemoUtils demoUtils = DemoUtils.getInstance(demoViewHolder.image.getContext());
        demoUtils.picassoThumbnailLoader()
                .load(bvProduct.getDisplayImageUrl())
                .resizeDimen(R.dimen.side_not_set, R.dimen.snippet_prod_image_side)
                .into(demoViewHolder.image);
        demoViewHolder.productName.setText(bvProduct.getDisplayName());
        demoViewHolder.productRating.setRating((int) bvProduct.getAverageRating());

        demoViewHolder.recommendationView.setBvProduct(bvProduct);

        demoViewHolder.row.setTag(bvProduct);
        demoViewHolder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productTapListener != null) {
                    productTapListener.onProductTapListener((BVProduct) v.getTag());
                }
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
            row = (RelativeLayout) itemView.findViewById(R.id.rec_snippet_container);
            image = (ImageView) itemView.findViewById(R.id.image);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productRating = (RatingBar) itemView.findViewById(R.id.product_rating);
        }
    }
}
