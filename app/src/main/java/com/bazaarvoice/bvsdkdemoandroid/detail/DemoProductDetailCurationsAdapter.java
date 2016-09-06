/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsPhoto;
import com.bazaarvoice.bvandroidsdk.CurationsView;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;

import java.util.Collections;
import java.util.List;

public class DemoProductDetailCurationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CurationsFeedItem> feedItems = Collections.emptyList();

    public interface CurationFeedItemTapListener {
        void onCurationFeedItemTapped(CurationsFeedItem curationsFeedItem);
    }

    private CurationFeedItemTapListener curationFeedItemTapListener;

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
            DemoUtils demoUtils = DemoUtils.getInstance(demoViewHolder.image.getContext());
            demoUtils.picassoThumbnailLoader()
                    .load(curationsPhotoUrl)
                    .resizeDimen(R.dimen.side_not_set, R.dimen.snippet_prod_image_side)
                    .into(demoViewHolder.image);
        }
        demoViewHolder.curationsView.setCurationsFeedItem(feedItem);
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

    public void setCurationFeedItemTapListener(CurationFeedItemTapListener curationFeedItemTapListener) {
        this.curationFeedItemTapListener = curationFeedItemTapListener;
    }

    private final class DemoProductDetailViewHolder extends RecyclerView.ViewHolder {
        CurationsView curationsView;
        RelativeLayout row;
        ImageView image;

        public DemoProductDetailViewHolder(View itemView) {
            super(itemView);
            curationsView = (CurationsView) itemView;
            row = (RelativeLayout) itemView.findViewById(R.id.curations_snippet_container);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
