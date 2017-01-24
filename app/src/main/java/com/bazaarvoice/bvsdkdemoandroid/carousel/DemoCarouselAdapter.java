/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvsdkdemoandroid.carousel;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoCarouselAdapter<ProductType extends BVDisplayableProductContent> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductType> contentList = new ArrayList<>();
    private final boolean carouselItemShowImage;
    private final boolean carouselItemShowTitle;
    private final boolean carouselItemShowRating;
    private final Picasso picasso;
    private DemoCarouselContract.OnItemClickListener onItemClickListener;

    DemoCarouselAdapter(final boolean carouselItemShowImage,
                        final boolean carouselItemShowTitle,
                        final boolean carouselItemShowRating,
                        final Picasso picasso) {
        this.carouselItemShowImage = carouselItemShowImage;
        this.carouselItemShowTitle = carouselItemShowTitle;
        this.carouselItemShowRating = carouselItemShowRating;
        this.picasso = picasso;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View productThumbnail = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_product_row_common, parent, false);
        return new DemoCarouselViewHolder(productThumbnail);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final BVDisplayableProductContent content = contentList.get(position);
        final DemoCarouselViewHolder viewHolder = (DemoCarouselViewHolder) holder;

        if (onItemClickListener != null) {
            viewHolder.itemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(content);
                    }
                }
            });
        }

        // Set content
        picasso.load(content.getDisplayImageUrl()).into(viewHolder.productThumbnailImage);
        viewHolder.productThumbnailTitle.setText(content.getDisplayName());
        viewHolder.productThumbnailRating.setRating(content.getAverageRating());

        // Set visibility
        viewHolder.productThumbnailImage.setVisibility(carouselItemShowImage ? View.VISIBLE : View.GONE);
        viewHolder.productThumbnailTitle.setVisibility(carouselItemShowTitle ? View.VISIBLE : View.GONE);
        viewHolder.productThumbnailRating.setVisibility(carouselItemShowRating ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    void updateContent(List<ProductType> contentList) {
        this.contentList = contentList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(DemoCarouselContract.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class DemoCarouselViewHolder extends RecyclerView.ViewHolder {
        ViewGroup itemRoot;
        @BindView(R.id.productThumbnailImage) ImageView productThumbnailImage;
        @BindView(R.id.productThumbnailTitle) TextView productThumbnailTitle;
        @BindView(R.id.productThumbnailRating) RatingBar productThumbnailRating;

        public DemoCarouselViewHolder(View itemView) {
            super(itemView);
            itemRoot = (ViewGroup) itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
