/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.recshome;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.RecommendationView;
import com.bazaarvoice.bvandroidsdk.RecommendationsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoRecsAdapter extends RecommendationsRecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = DemoRecsAdapter.class.getSimpleName();
    private List<RowData> rowDataList = new ArrayList<>();
    private List<BVProduct> bvProducts = new ArrayList<>();
    private NativeContentAd nativeContentAd;

    private static final class RowData<RowDataType> {
        private RowDataType rowData;
        private int rowType;

        public RowData(RowDataType rowData) {
            this.rowData = rowData;
            if (rowData instanceof RowDataRec) {
                this.rowType = ROW_REC;
            } else if (rowData instanceof RowDataAd) {
                this.rowType = ROW_AD;
            } else {
                throw new IllegalStateException("Invalid row data type");
            }
        }

        public RowDataType getRowData() {
            return rowData;
        }

        public int getRowType() {
            return rowType;
        }
    }

    private static final class RowDataRec {
        private BVProduct leftProd;
        private BVProduct rightProd;

        public RowDataRec(BVProduct leftProd, BVProduct rightRec) {
            this.leftProd = leftProd;
            this.rightProd = rightRec;
        }

        public BVProduct getLeftProd() {
            return leftProd;
        }

        public BVProduct getRightProd() {
            return rightProd;
        }
    }

    private static final class RowDataAd {
        private NativeContentAd nativeContentAd;

        public RowDataAd(NativeContentAd nativeContentAd) {
            this.nativeContentAd = nativeContentAd;
        }

        public NativeContentAd getNativeContentAd() {
            return nativeContentAd;
        }
    }

    private static final int ROW_REC = 0;
    private static final int ROW_AD = 1;
    public static final int ROW_COUNT = 2;
    private static final int AD_ROW_INDEX = 2;

    public interface RecTapListener {
        void onProductTapped(BVProduct bvProduct);
    }

    private RecTapListener recTapListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case ROW_AD:
                View adRowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ad, parent, false);
                viewHolder = new AdViewHolder(adRowView);
                break;
            case ROW_REC:
            default:
                View recRowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_recs, parent, false);
                viewHolder = new RecRowViewHolder(recRowView);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ROW_AD:
                RowDataAd rowDataAd = (RowDataAd) rowDataList.get(position).getRowData();
                bindAdRow(holder, rowDataAd);
                break;
            case ROW_REC:
            default:
                RowDataRec rowDataRec = (RowDataRec) rowDataList.get(position).getRowData();
                BVProduct leftProduct = rowDataRec.getLeftProd();
                BVProduct rightProduct = rowDataRec.getRightProd();
                bindRecRow(holder, leftProduct, rightProduct);
                break;
        }
    }

    private void bindAdRow(RecyclerView.ViewHolder holder, RowDataAd rowDataAd) {
        final AdViewHolder adViewHolder = (AdViewHolder) holder;
        NativeContentAd nativeContentAd = rowDataAd != null ? rowDataAd.getNativeContentAd() : null;
        NativeContentAdView nativeContentAdView = adViewHolder.nativeContentAdView;
        ImageView imageView = adViewHolder.imageView;
        Context context = imageView.getContext();
        DemoUtils demoUtils = DemoUtils.getInstance(context);
        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(context);
        Picasso picasso = demoUtils.picassoThumbnailLoader();

        RequestCreator requestCreator = null;
        if (nativeContentAd != null) {
            List<NativeAd.Image> images = nativeContentAd.getImages();
            if (images != null && images.size() > 0) {
                Uri adViewImageUri = images.get(0).getUri();
                requestCreator = picasso.load(adViewImageUri);
            }
            nativeContentAdView.setImageView(imageView);
            nativeContentAdView.setNativeAd(nativeContentAd);

            adViewHolder.headlineText.setText(nativeContentAd.getHeadline());
            nativeContentAdView.setAdvertiserView(adViewHolder.headlineText);

            adViewHolder.bodyText.setText(nativeContentAd.getBody());
            nativeContentAdView.setBodyView(adViewHolder.bodyText);

            adViewHolder.callToActionText.setText(nativeContentAd.getCallToAction());
            nativeContentAdView.setCallToActionView(adViewHolder.callToActionText);
        }
        if (requestCreator != null) {
            requestCreator.resizeDimen(R.dimen.side_not_set, R.dimen.ad_row_image_height).into(imageView);
        }
    }

    private void bindRecRow(RecyclerView.ViewHolder holder, BVProduct leftProduct, BVProduct rightProduct) {
        RecRowViewHolder recRowViewHolder = (RecRowViewHolder) holder;

        DemoUtils demoUtils = DemoUtils.getInstance(recRowViewHolder.row.getContext());

        LinearLayout recRowContainer = recRowViewHolder.recRowContainer;
        recRowContainer.removeAllViews();
        RecommendationView leftRecView = (RecommendationView) LayoutInflater.from(recRowContainer.getContext()).inflate(R.layout.include_product_rec_snippet_cell, recRowContainer, false);
        RecItemViewHolder leftRec = new RecItemViewHolder(leftRecView);

        demoUtils.picassoThumbnailLoader()
                .load(leftProduct.getImageUrl())
                .resizeDimen(R.dimen.side_not_set, R.dimen.snippet_prod_image_side)
                .into(leftRec.image);
        leftRec.prodName.setText(leftProduct.getProductName());
        if (TextUtils.isEmpty(leftProduct.getPrice())){
            leftRec.prodPrice.setVisibility(View.GONE);
        } else {
            leftRec.prodPrice.setText(leftProduct.getPrice());
        }
        leftRec.prodRating.setRating((int)leftProduct.getAverageRating());
        leftRec.recView.setBvProduct(leftProduct);
        leftRec.recView.setTag(leftProduct);
        leftRec.recView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recTapListener != null) {
                    recTapListener.onProductTapped((BVProduct) v.getTag());
                }
            }
        });
        recRowContainer.addView(leftRecView);

        if (rightProduct != null) {
            RecommendationView rightRecView = (RecommendationView) LayoutInflater.from(recRowContainer.getContext()).inflate(R.layout.include_product_rec_snippet_cell, recRowContainer, false);
            RecItemViewHolder rightRec = new RecItemViewHolder(rightRecView);

            demoUtils.picassoThumbnailLoader()
                    .load(rightProduct.getImageUrl())
                    .resizeDimen(R.dimen.side_not_set, R.dimen.snippet_prod_image_side)
                    .into(rightRec.image);
            rightRec.prodName.setText(rightProduct.getProductName());
            if (TextUtils.isEmpty(rightProduct.getPrice())){
                rightRec.prodPrice.setVisibility(View.GONE);
            } else {
                rightRec.prodPrice.setText(rightProduct.getPrice());
            }
            rightRec.prodRating.setRating((int)rightProduct.getAverageRating());
            rightRec.recView.setBvProduct(rightProduct);
            rightRec.recView.setTag(rightProduct);
            rightRec.recView.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recTapListener != null) {
                        recTapListener.onProductTapped((BVProduct) v.getTag());
                    }
                }
            });
            rightRec.recView.setVisibility(View.VISIBLE);
            recRowContainer.addView(rightRecView);
        }
    }

    @Override
    public int getItemCount() {
        return rowDataList.size();
    }

    public int getRecommendationCount() {
        return bvProducts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return rowDataList.get(position).getRowType();
    }

    public void refreshProducts(List<BVProduct> bvProducts) {
        this.bvProducts = bvProducts;
        refreshRows();
    }

    public void refreshAd(NativeContentAd nativeContentAd) {
        this.nativeContentAd = nativeContentAd;
        refreshRows();
    }

    private void refreshRows() {
        this.rowDataList.clear();
        for (int i=0; i<bvProducts.size(); i+=2) {
            BVProduct leftProd = bvProducts.get(i);
            BVProduct rightProd = null;
            if (i+1 < bvProducts.size()) {
                rightProd = bvProducts.get(i+1);
            }
            RowDataRec rowDataRec = new RowDataRec(leftProd, rightProd);
            rowDataList.add(new RowData<>(rowDataRec));
        }
        if (rowDataList.size() > AD_ROW_INDEX) {
            rowDataList.add(AD_ROW_INDEX, new RowData<>(new RowDataAd(nativeContentAd)));
        } else {
            rowDataList.add(new RowData<>(new RowDataAd(nativeContentAd)));
        }
        notifyDataSetChanged();
    }

    public void setRecTapListener(RecTapListener recTapListener) {
        this.recTapListener = recTapListener;
    }

    private final class RecRowViewHolder extends RecyclerView.ViewHolder {
        ViewGroup row;
        LinearLayout recRowContainer;

        public RecRowViewHolder(View itemView) {
            super(itemView);
            this.row = (ViewGroup) itemView;
            this.recRowContainer = (LinearLayout) itemView.findViewById(R.id.rec_row_container);
        }
    }

    static class RecItemViewHolder {
        RecommendationView recView;
        @BindView(R.id.image) ImageView image;
        @BindView(R.id.product_name) TextView prodName;
        @BindView(R.id.product_price) TextView prodPrice;
        @BindView(R.id.product_rating) RatingBar prodRating;
        RecItemViewHolder(RecommendationView recView) {
            this.recView = recView;
            ButterKnife.bind(this, recView);
        }
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        NativeContentAdView nativeContentAdView;
        @BindView(R.id.image) ImageView imageView;
        @BindView(R.id.headline_text) TextView headlineText;
        @BindView(R.id.call_to_action_text) TextView callToActionText;
        @BindView(R.id.body_text) TextView bodyText;
        public AdViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            nativeContentAdView = (NativeContentAdView) itemView;
        }
    }
}
