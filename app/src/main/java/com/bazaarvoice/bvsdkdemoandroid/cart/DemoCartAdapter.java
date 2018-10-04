/**
 * Copyright 2017 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

public class DemoCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private DemoCart cart;
    private OnBvProductClickListener onItemClickListener;


    public interface OnBvProductClickListener {
        void onBvProductClickListener(BVProduct bvProduct, View row);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_product, parent, false);
        return new BvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BvViewHolder viewHolder = (BvViewHolder) holder;
        BVDisplayableProductContent product = cart.get(position);

        Picasso.get()
                .load(product.getDisplayImageUrl())
                .error(R.drawable.ic_shopping_basket_black_24dp)
                .into(viewHolder.productImageView);

        viewHolder.productNameTextView.setText(product.getDisplayName());
        viewHolder.productPriceTextView.setText("$0.00");
    }

    @Override
    public int getItemCount() {
        return cart.numberOfItems();
    }

    public void setOnItemClickListener(OnBvProductClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setCart(DemoCart cart) {
        this.cart = cart;
        notifyDataSetChanged();
    }


    private static class BvViewHolder extends RecyclerView.ViewHolder {

        private final ImageView productImageView;
        private final TextView productNameTextView;
        private final TextView productPriceTextView;

        public BvViewHolder(View itemView) {
            super(itemView);
            productImageView = (ImageView) itemView.findViewById(R.id.cartProductImageView);
            productNameTextView = (TextView) itemView.findViewById(R.id.cartProductNameTextView);
            productPriceTextView = (TextView) itemView.findViewById(R.id.cartProductPriceTextView);
        }
    }
}