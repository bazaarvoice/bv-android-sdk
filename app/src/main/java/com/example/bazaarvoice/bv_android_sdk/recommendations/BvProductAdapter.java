/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.recommendations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description Here
 */
public class BvProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BVProduct> bvProducts = new ArrayList<>();
    private OnBvProductClickListener onItemClickListener;

    public interface OnBvProductClickListener {
        void onBvProductClickListener(BVProduct bvProduct, View row);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rawRecRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_rec_adapter_row, parent, false);
        return new BvViewHolder(rawRecRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BVProduct bvProduct = bvProducts.get(position);
        BvViewHolder bvViewHolder = (BvViewHolder) holder;

        Picasso.with(bvViewHolder.prodImage.getContext())
                .load(bvProduct.getImageUrl())
                .into(bvViewHolder.prodImage);

        bvViewHolder.productName.setText(bvProduct.getProductName());

        BVSDK.getInstance().sendProductImpressionEvent(bvProduct);

        if (bvProduct.getNumReviews() > 0) {
            String reviews = "Average Rating: ";
            for (int i=0; i<bvProduct.getAverageRating(); i++) {
                reviews += "⭐";
            }
            bvViewHolder.productRating.setText(reviews);
            bvViewHolder.productRating.setVisibility(View.VISIBLE);
        } else {
            bvViewHolder.productRating.setVisibility(View.GONE);
        }

        bvViewHolder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onBvProductClickListener(bvProduct, v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bvProducts.size();
    }

    private class BvViewHolder extends RecyclerView.ViewHolder {
        public ViewGroup row;
        public ImageView prodImage;
        public TextView productName;
        public TextView productRating;

        public BvViewHolder(View itemView) {
            super(itemView);
            row = (ViewGroup) itemView;
            prodImage = (ImageView) itemView.findViewById(R.id.prodImage);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productRating = (TextView) itemView.findViewById(R.id.productRating);
        }
    }

    public void setBvProducts(List<BVProduct> bvProducts) {
        this.bvProducts = bvProducts;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnBvProductClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
