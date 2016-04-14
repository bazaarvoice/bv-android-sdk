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
import com.bazaarvoice.bvandroidsdk.RecommendationView;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DemoProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BVProduct> bvProducts = new ArrayList<>();
    private OnBvProductClickListener onItemClickListener;
    private static final int ROW_BV_PRODUCT = 0;
    private static final int ROW_NO_RECS_FOUND = 1;

    public interface OnBvProductClickListener {
        void onBvProductClickListener(BVProduct bvProduct, View row);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case ROW_BV_PRODUCT:
                View rawRecRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_rec_adapter_row, parent, false);
                viewHolder = new BvViewHolder(rawRecRow);
                break;
            case ROW_NO_RECS_FOUND:
            default:
                View noRecsRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_no_recs, parent, false);
                viewHolder = new BvEmptyViewHOlder(noRecsRow);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case ROW_BV_PRODUCT:
                bindBvProductRow(position, holder);
                break;
        }

    }

    private void bindBvProductRow(int position, RecyclerView.ViewHolder holder) {
        final BVProduct bvProduct = bvProducts.get(position);
        BvViewHolder bvViewHolder = (BvViewHolder) holder;

        Picasso.with(bvViewHolder.prodImage.getContext())
                .load(bvProduct.getImageUrl())
                .error(R.drawable.ic_shopping_basket_black_24dp)
                .into(bvViewHolder.prodImage);

        bvViewHolder.productName.setText(bvProduct.getProductName());

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

        bvViewHolder.row.setBvProduct(bvProduct);
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
    public int getItemViewType(int position) {
        if (bvProducts.isEmpty()) {
            return ROW_NO_RECS_FOUND;
        } else {
            return ROW_BV_PRODUCT;
        }
    }

    @Override
    public int getItemCount() {
        return bvProducts.isEmpty() ? 1 : bvProducts.size();
    }

    private class BvViewHolder extends RecyclerView.ViewHolder {
        public RecommendationView row;
        public ImageView prodImage;
        public TextView productName;
        public TextView productRating;

        public BvViewHolder(View itemView) {
            super(itemView);
            row = (RecommendationView) itemView;
            prodImage = (ImageView) itemView.findViewById(R.id.prodImage);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productRating = (TextView) itemView.findViewById(R.id.productRating);
        }
    }

    private class BvEmptyViewHOlder extends RecyclerView.ViewHolder {
        public TextView noRecsFound;

        public BvEmptyViewHOlder(View rowView) {
            super(rowView);
            noRecsFound = (TextView) rowView;
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
