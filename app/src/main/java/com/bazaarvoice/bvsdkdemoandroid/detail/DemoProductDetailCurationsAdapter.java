/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsPhoto;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DemoProductDetailCurationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Inject Picasso picasso;

    private List<CurationsFeedItem> feedItems = Collections.emptyList();

    public interface CurationFeedItemTapListener {
        void onCurationFeedItemTapped(CurationsFeedItem curationsFeedItem);
    }

    private CurationFeedItemTapListener curationFeedItemTapListener;

    public DemoProductDetailCurationsAdapter(Context context) {
        DemoApp.get(context).getAppComponent().inject(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_product_conv_snippet_cell, parent, false);
        return new DemoProductDetailViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CurationsFeedItem feedItem = feedItems.get(position);
        DemoProductDetailViewHolder demoViewHolder = (DemoProductDetailViewHolder) holder;

        if (feedItem.getPhotos() != null && !feedItem.getPhotos().isEmpty()) {
            CurationsPhoto curationsPhoto = feedItem.getPhotos().get(0);
            String curationsPhotoUrl = curationsPhoto.getImageServiceUrl() + "&width=" + (DemoUtils.MAX_IMAGE_WIDTH/4) + "&height=" + (DemoUtils.MAX_IMAGE_HEIGHT/4);
            picasso.load(curationsPhotoUrl)
                    .resizeDimen(R.dimen.side_not_set, R.dimen.snippet_prod_image_side)
                    .into(demoViewHolder.image);
        }

        demoViewHolder.row.setTag(feedItem);
        demoViewHolder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curationFeedItemTapListener != null) {
                    CurationsFeedItem curationsFeedItem = (CurationsFeedItem) v.getTag();
                    curationFeedItemTapListener.onCurationFeedItemTapped(curationsFeedItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public void refreshFeedItems(List<CurationsFeedItem> feedItems) {
        this.feedItems = feedItems;
        notifyDataSetChanged();
    }


    private final class DemoProductDetailViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout row;
        ImageView image;

        public DemoProductDetailViewHolder(View itemView) {
            super(itemView);
            row = (RelativeLayout) itemView.findViewById(R.id.curations_snippet_container);
            image = (ImageView) itemView.findViewById(R.id.productThumbnailImage);
        }
    }
}
