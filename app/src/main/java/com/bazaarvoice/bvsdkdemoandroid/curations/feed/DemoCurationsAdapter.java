package com.bazaarvoice.bvsdkdemoandroid.curations.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsView;
import com.bazaarvoice.bvandroidsdk.CurationsPhoto;
import com.bazaarvoice.bvandroidsdk.CurationsVideo;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bazaarvoice on 4/4/16.
 */
public class DemoCurationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private OnCurationsUpdatePressedListener listener;

    public interface OnCurationsUpdatePressedListener{
        public void onCurationsUpdatePressed(CurationsFeedItem update, View row);
    }

    private List<CurationsFeedItem> values = new ArrayList<>();
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rawRecRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.curations_item, parent, false);
        return new BvViewHolder(rawRecRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BvViewHolder bvViewHolder = (BvViewHolder) holder;


        final CurationsFeedItem curationsFeedItem = values.get(position);

        CurationsView view = (CurationsView) bvViewHolder.row;
        view.setCurationsFeedItem(curationsFeedItem);

        String origin = null;
        String imageUrl = null;
        if (curationsFeedItem.getPhotos().size() > 0) {
            CurationsPhoto photo = curationsFeedItem.getPhotos().get(0);
            origin = photo.getOrigin();
            imageUrl = photo.getLocalUrl();
            bvViewHolder.videoIcon.setVisibility(View.GONE);
        }else if (curationsFeedItem.getVideos().size() > 0){
            CurationsVideo video = curationsFeedItem.getVideos().get(0);
            origin = video.getOrigin();
            imageUrl = curationsFeedItem.getVideos().get(0).getImageUrl();
            bvViewHolder.videoIcon.setVisibility(View.VISIBLE);
        }

        bvViewHolder.socialImage.setBackgroundColor(bvViewHolder.socialImage.getResources().getColor(R.color.bv_gray_1));
        Picasso.with(bvViewHolder.socialImage.getContext())
                .load(imageUrl)
                .into(bvViewHolder.socialImage);

        loadPhotoOriginIcon(origin, bvViewHolder.srcLogo);

        bvViewHolder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onCurationsUpdatePressed(curationsFeedItem, v);
                }
            }
        });
    }

    private void loadPhotoOriginIcon(String src, ImageView imageView){

        int id = 0;

        if (src != null) {
            switch (src) {
                case "youtube":
                    id = R.drawable.youtube;
                    break;
                case "pinterest":
                    id = R.drawable.pinterest;
                    break;
                case "twitter":
                    id = R.drawable.twitter;
                    break;
                case "instagram":
                    id = R.drawable.instagram;
                    break;
                case "google-plus":
                    id = R.drawable.google_plus;
                    break;
                case "bazaarvoice":
                    id = R.drawable.bv;
                    break;
                default:
                    break;
            }
        }
        if (id != 0) {
            imageView.setVisibility(View.VISIBLE);
            Picasso.with(imageView.getContext())
                    .load(id)
                    .into(imageView);
        }else{
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.values.size();
    }

    private class BvViewHolder extends RecyclerView.ViewHolder {
        public ViewGroup row;
        public ImageView socialImage;
        public ImageView srcLogo;
        public ImageView videoIcon;

        public BvViewHolder(View itemView) {
            super(itemView);
            row = (ViewGroup) itemView;
            socialImage = (ImageView) itemView.findViewById(R.id.imageView);
            srcLogo = (ImageView) itemView.findViewById(R.id.src_logo);
            videoIcon = (ImageView) itemView.findViewById(R.id.videoIcon);
        }
    }

    public void setValues(List<CurationsFeedItem> values) {
        this.values = values;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnCurationsUpdatePressedListener onItemPressedListener) {
        this.listener = onItemPressedListener;
    }
}
