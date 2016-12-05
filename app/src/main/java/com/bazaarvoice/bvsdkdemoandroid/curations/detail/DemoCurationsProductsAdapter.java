package com.bazaarvoice.bvsdkdemoandroid.curations.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.CurationsProduct;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bazaarvoice on 4/6/16.
 */
public class DemoCurationsProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CurationsProduct> products = new ArrayList<>();
    private CurationsProductsOnClickListener listener;

    public interface CurationsProductsOnClickListener{
        public void onCurationsProductClicked(CurationsProduct product);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rawRecRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.curation_product_adapter_row, parent, false);
        return new BvViewHolder(rawRecRow);    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        BvViewHolder bvViewHolder = (BvViewHolder) holder;

        final CurationsProduct product = products.get(position);
        Picasso.with(bvViewHolder.productImage.getContext()).load(product.getDisplayImageUrl()).into(bvViewHolder.productImage);

        bvViewHolder.productName.setText(product.getDisplayName());

        if (product.getProductReviewStatistics().getNumReviews() > 0) {

            int starCount = Math.round(product.getProductReviewStatistics().getAvgRating());
            String reviews = "";
            for (int i=0; i< starCount; i++) {
                reviews += "⭐";
            }
            reviews += " " + String.format("%.1f",product.getProductReviewStatistics().getAvgRating()) + " (" + product.getProductReviewStatistics().getNumReviews() + ")";
            bvViewHolder.productRating.setText(reviews);
            bvViewHolder.productRating.setVisibility(View.VISIBLE);
        } else {
            bvViewHolder.productRating.setText("No reviews yet");
        }

        bvViewHolder.shopNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCurationsProductClicked(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<CurationsProduct> products){
        this.products = products;
        notifyDataSetChanged();
    }

    public void setListener(CurationsProductsOnClickListener listener){
        this.listener = listener;
    }

    private class BvViewHolder extends RecyclerView.ViewHolder {
        public ViewGroup row;
        public ImageView productImage;
        public TextView productName;
        public TextView productRating;
        public Button shopNowBtn;

        public BvViewHolder(View itemView) {
            super(itemView);
            row = (ViewGroup) itemView;
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productRating = (TextView) itemView.findViewById(R.id.product_rating);
            shopNowBtn = (Button) itemView.findViewById(R.id.shopNowButton);
        }
    }
}
