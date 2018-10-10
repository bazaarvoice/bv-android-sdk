/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.stores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.Store;
import com.bazaarvoice.bvsdkdemoandroid.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoStoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Store> stores = new ArrayList<>();
    private DemoStoreAdapter.OnBvProductClickListener onItemClickListener;
    private static final int ROW_STORE = 0;

    public interface OnBvProductClickListener {
        void onStoreClickListener(Store store, View row);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case ROW_STORE:
            default:
                View rawRecRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_store, parent, false);
                viewHolder = new DemoStoreAdapter.BvViewHolder(rawRecRow);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case ROW_STORE:
                bindStoreRow(position, holder);
                break;
        }

    }

    private void bindStoreRow(int position, RecyclerView.ViewHolder holder) {
        final Store store = stores.get(position);
        DemoStoreAdapter.BvViewHolder bvViewHolder = (DemoStoreAdapter.BvViewHolder) holder;

        bvViewHolder.storeName.setText(store.getDisplayName());

        bvViewHolder.storeRating.setRating(store.getReviewStatistics().getAverageOverallRating());
        bvViewHolder.storeRatingCount.setText("(" + store.getReviewStatistics().getTotalReviewCount() + ")");

        bvViewHolder.storeStreetName.setText(store.getLocationAttribute(Store.StoreAttributeType.ADDRESS));
        bvViewHolder.storeCityStateZip.setText(store.getLocationAttribute(Store.StoreAttributeType.POSTALCODE));
        bvViewHolder.storePhoneNumber.setText(store.getLocationAttribute(Store.StoreAttributeType.PHONE));
        bvViewHolder.storeOpenHours.setText("10am - 9pm");

        bvViewHolder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onStoreClickListener(store, v);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return ROW_STORE;
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    static class BvViewHolder extends RecyclerView.ViewHolder {
        public ViewGroup row;
        @BindView(R.id.storeName) TextView storeName;
        @BindView(R.id.storeRating) RatingBar storeRating;
        @BindView(R.id.storeRatingCount) TextView storeRatingCount;
        @BindView(R.id.storeStreetName) TextView storeStreetName;
        @BindView(R.id.storeCityStateZip) TextView storeCityStateZip;
        @BindView(R.id.storePhoneNumber) TextView storePhoneNumber;
        @BindView(R.id.storeOpenHours) TextView storeOpenHours;

        public BvViewHolder(View itemView) {
            super(itemView);
            row = (ViewGroup) itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(DemoStoreAdapter.OnBvProductClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}