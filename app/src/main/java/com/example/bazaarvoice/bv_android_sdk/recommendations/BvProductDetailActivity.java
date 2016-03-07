/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.recommendations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.recommendations.data.BvRepository;
import com.example.bazaarvoice.bv_android_sdk.di.AppConfigurationImpl;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * TODO: Description Here
 */
public class BvProductDetailActivity extends AppCompatActivity implements BvRepository.LoadRecommendedProductsCallback {

    public static final String BV_PRODUCT_ID = "extra_bv_product_id";

    private ImageView prodImage;
    private TextView productName;
    private TextView productRating;
    private TextView productLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        prodImage = (ImageView) findViewById(R.id.prodImage);
        ViewGroup.LayoutParams layoutParams = prodImage.getLayoutParams();
        layoutParams.height = getResources().getDimensionPixelSize(R.dimen.product_image_height_detail);
        prodImage.setLayoutParams(layoutParams);
        productName = (TextView) findViewById(R.id.productName);
        productRating = (TextView) findViewById(R.id.productRating);
        productLink = (TextView) findViewById(R.id.productLink);

        BvRepository bvRepository = AppConfigurationImpl.getInstance().provideBvRepository();
        bvRepository.getRecommendedProducts(this);
    }

    private void showBvProduct(BVProduct bvProduct) {
        Picasso.with(prodImage.getContext())
                .load(bvProduct.getImageUrl())
                .into(prodImage);

        productName.setText(bvProduct.getProductName());
        if (bvProduct.getNumReviews() > 0) {
            String reviews = "Average Rating: ";
            for (int i=0; i<bvProduct.getAverageRating(); i++) {
                reviews += "⭐";
            }
            productRating.setText(reviews);
            productRating.setVisibility(View.VISIBLE);
        } else {
            productRating.setVisibility(View.GONE);
        }

        if (bvProduct.getProductPageUrl() != null) {
            productLink.setText(bvProduct.getProductPageUrl());
            Linkify.addLinks(productLink, Linkify.ALL);
            productLink.setVisibility(View.VISIBLE);
        } else {
            productLink.setVisibility(View.GONE);
        }

    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecommendedProductsLoaded(List<BVProduct> recommendedProducts) {
        String bvProductId = getIntent().getStringExtra(BV_PRODUCT_ID);
        for (BVProduct bvProduct : recommendedProducts) {
            if (bvProduct.getProductId().equals(bvProductId)) {
                showBvProduct(bvProduct);
                break;
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        showMessage(throwable.getMessage());
    }
}
