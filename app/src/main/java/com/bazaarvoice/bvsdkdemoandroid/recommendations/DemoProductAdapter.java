/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid.recommendations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.RecommendationView;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DemoProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BVProduct> bvProducts = new ArrayList<>();
    private OnBvProductClickListener onItemClickListener;
    private static final int ROW_BV_PRODUCT = 0;

    public interface OnBvProductClickListener {
        void onBvProductClickListener(BVProduct bvProduct, View row);
    }

    public interface AddProductToCartLister {
        void addProductToCart(BVProduct product);
    }

    private AddProductToCartLister addProductToCartLister;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case ROW_BV_PRODUCT:
            default:
                View rawRecRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_rec_adapter_row, parent, false);
                viewHolder = new BvViewHolder(rawRecRow);
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
                .load(bvProduct.getDisplayImageUrl())
                .error(R.drawable.ic_shopping_basket_black_24dp)
                .into(bvViewHolder.prodImage);



        bvViewHolder.addToCartButtonListener = new BvViewHolder.AddToCartButtonListener() {
            @Override
            public void addToCartPressed() {
                addProductToCartLister.addProductToCart(bvProduct);
            }
        };

        bvViewHolder.productName.setText(bvProduct.getDisplayName());

        if (bvProduct.getNumReviews() > 0) {
            String reviews = "Average Rating: ⭐⭐⭐⭐⭐"; // TODO RATING
//            for (int i=0; i<bvProduct.getAverageRating(); i++) {
//                reviews += "⭐";
//            }
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
        return ROW_BV_PRODUCT;
    }

    @Override
    public int getItemCount() {
        return bvProducts.size();
    }

    private static class BvViewHolder extends RecyclerView.ViewHolder {
        public RecommendationView row;
        public ImageView prodImage;
        public TextView productName;
        public TextView productRating;
        private Button addToCartButton;
        private AddToCartButtonListener addToCartButtonListener;

        public interface AddToCartButtonListener {
            void addToCartPressed();
        }

        public BvViewHolder(View itemView) {
            super(itemView);
            row = (RecommendationView) itemView;
            prodImage = (ImageView) itemView.findViewById(R.id.prodImage);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productRating = (TextView) itemView.findViewById(R.id.productRating);
            addToCartButton = (Button) itemView.findViewById(R.id.addToCartButton);
            addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToCartButtonListener.addToCartPressed();
                }
            });
        }
    }

    public void setBvProducts(List<BVProduct> bvProducts) {
        this.bvProducts = bvProducts;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnBvProductClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void  setAddProductToCartLister(AddProductToCartLister lister){
        this.addProductToCartLister = lister;
    }
}
