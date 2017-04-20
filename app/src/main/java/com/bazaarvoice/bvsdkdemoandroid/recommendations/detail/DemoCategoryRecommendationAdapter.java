/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.recommendations.detail;

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

import java.util.Collections;
import java.util.List;

public class DemoCategoryRecommendationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final BVProduct detailProduct;
    private List<BVProduct> recommendedProducts = Collections.emptyList();
    private static final int ROW_HEADER_DETAIL = 0;
    private static final int ROW_EXTRA_HEADER = 1;
    private static final int ROW_EXTRA_PRODUCT = 2;

    public interface OnExtraProductTappedListener {
        void onExtraProductTapped(BVProduct bvProduct);
    }

    private OnExtraProductTappedListener onExtraProductTappedListener;

    public interface AddProductToCartLister {
        void addProductToCart(BVProduct product);
    }

    private AddProductToCartLister addProductToCartLister;

    public DemoCategoryRecommendationAdapter(BVProduct bvProduct) {
        this.detailProduct = bvProduct;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case ROW_HEADER_DETAIL:
                viewHolder = createHeaderDetailViewHolder(parent);
                break;
            case ROW_EXTRA_HEADER:
                viewHolder = createExtraHeaderViewHolder(parent);
                break;
            case ROW_EXTRA_PRODUCT:
            default:
                viewHolder = createExtraProductViewHolder(parent);
                break;
        }
        return viewHolder;
    }

    private RecyclerView.ViewHolder createHeaderDetailViewHolder(ViewGroup parent) {
        View headerDetailView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_product_detail_header, parent, false);
        return new HeaderDetailViewHolder(headerDetailView);
    }

    private RecyclerView.ViewHolder createExtraHeaderViewHolder(ViewGroup parent) {
        View extraHeaderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_extra_product_title, parent, false);
        return new ExtraHeaderViewHolder(extraHeaderView);
    }

    private RecyclerView.ViewHolder createExtraProductViewHolder(ViewGroup parent) {
        View extraProductView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_detail_recommended_product_row, parent, false);
        return new ExtraProductViewHolder(extraProductView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);

        switch (itemViewType) {
            case ROW_HEADER_DETAIL:
                bindHeaderDetailViewHolder(holder, position);
                break;
            case ROW_EXTRA_HEADER:
                bindExtraHeaderViewHolder(holder);
                break;
            case ROW_EXTRA_PRODUCT:
                bindExtraProductViewHolder(holder, position);
                break;
        }
    }

    private void bindHeaderDetailViewHolder(RecyclerView.ViewHolder holder, int position) {
        HeaderDetailViewHolder headerDetailViewHolder = (HeaderDetailViewHolder) holder;

        ImageView prodImage = headerDetailViewHolder.prodImage;
        ViewGroup.LayoutParams layoutParams = prodImage.getLayoutParams();
        layoutParams.height = prodImage.getResources().getDimensionPixelSize(R.dimen.product_image_height_detail);
        prodImage.setLayoutParams(layoutParams);
        Picasso.with(prodImage.getContext())
                .load(detailProduct.getDisplayImageUrl())
                .error(R.drawable.ic_shopping_basket_black_24dp)
                .into(prodImage);


        headerDetailViewHolder.addToCartButtonListener = new HeaderDetailViewHolder.AddToCartButtonListener() {
            @Override
            public void addToCartPressed() {
                DemoCategoryRecommendationAdapter.this.addProductToCartLister.addProductToCart(detailProduct);
            }
        };

        TextView productName = headerDetailViewHolder.prodName;
        productName.setText(detailProduct.getDisplayName());

        TextView productRating = headerDetailViewHolder.prodRating;
        if (detailProduct.getNumReviews() > 0) {
            String reviews = "Average Rating: ⭐⭐⭐⭐⭐"; // TODO RATING
//            for (int i=0; i<detailProduct.getAverageRating(); i++) {
//                reviews += "⭐";
//            }
            productRating.setText(reviews);
            productRating.setVisibility(View.VISIBLE);
        } else {
            productRating.setVisibility(View.GONE);
        }
    }

    private void bindExtraHeaderViewHolder(RecyclerView.ViewHolder holder) {
        ExtraHeaderViewHolder extraHeaderViewHolder = (ExtraHeaderViewHolder) holder;
    }

    private void bindExtraProductViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExtraProductViewHolder headerDetailViewHolder = (ExtraProductViewHolder) holder;
        final BVProduct bvProduct = recommendedProducts.get(position - 2);

        TextView textView = headerDetailViewHolder.title;
        textView.setText(bvProduct.getDisplayName());

        ImageView imageView = headerDetailViewHolder.image;
        Picasso.with(imageView.getContext())
                .load(bvProduct.getDisplayImageUrl())
                .error(R.drawable.ic_shopping_basket_black_24dp)
                .into(imageView);

        RecommendationView recommendationView = headerDetailViewHolder.recommendationView;
        recommendationView.setBvProduct(bvProduct);

        if (onExtraProductTappedListener != null) {
            recommendationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onExtraProductTappedListener.onExtraProductTapped(bvProduct);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ROW_HEADER_DETAIL;
        } else if (position == 1) {
            return ROW_EXTRA_HEADER;
        } else {
            return ROW_EXTRA_PRODUCT;
        }
    }

    @Override
    public int getItemCount() {
        int numDetailRows = 1;
        int numExtraProductRows = recommendedProducts.size();
        int numExtraHeaderRows = numExtraProductRows > 0 ? 1 : 0;
        return numDetailRows + numExtraHeaderRows + numExtraProductRows;
    }

    public void setRecommendedProducts(List<BVProduct> recommendedProducts) {
        this.recommendedProducts = recommendedProducts;
        notifyDataSetChanged();
    }

    private static final class HeaderDetailViewHolder extends RecyclerView.ViewHolder {
//        private RecommendationView detailBvView;
        private TextView prodName;
        private ImageView prodImage;
        private TextView prodRating;
        private Button addToCartButton;
        private AddToCartButtonListener addToCartButtonListener;

        public interface AddToCartButtonListener {
            void addToCartPressed();
        }

        public HeaderDetailViewHolder(View itemView) {
            super(itemView);
//            detailBvView = (RecommendationView) itemView.findViewById(R.id.bvView);
            prodName = (TextView) itemView.findViewById(R.id.productName);
            prodImage = (ImageView) itemView.findViewById(R.id.prodImage);
            prodRating = (TextView) itemView.findViewById(R.id.productRating);
            addToCartButton = (Button) itemView.findViewById(R.id.addToCartButton);
            addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToCartButtonListener.addToCartPressed();
                }
            });
        }
    }

    private final class ExtraHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ExtraHeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }

    private final class ExtraProductViewHolder extends RecyclerView.ViewHolder {
        private RecommendationView recommendationView;
        private TextView title;
        private ImageView image;

        public ExtraProductViewHolder(View itemView) {
            super(itemView);
            recommendationView = (RecommendationView) itemView;
            title = (TextView) itemView.findViewById(R.id.text);
            image = (ImageView) itemView.findViewById(R.id.productThumbnailImage);
        }
    }

    public void setOnExtraProductTappedListener(OnExtraProductTappedListener listener) {
        this.onExtraProductTappedListener = listener;
    }

    public void  setAddProductToCartLister(AddProductToCartLister lister){
        this.addProductToCartLister = lister;
    }
}
