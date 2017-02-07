package com.bazaarvoice.bvandroidsdk;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bazaarvoice.bvandroidsdk_curationsui.R;

import java.util.ArrayList;
import java.util.List;

import static com.bazaarvoice.bvandroidsdk.CurationsInfiniteRecyclerView.getImageSize;
import static com.bazaarvoice.bvandroidsdk.CurationsInfiniteRecyclerView.isVertical;

class CurationsInfiniteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  // region Properties

  private static final String SERVICE_IMAGE_TEMPLATE = "%1$s&width=%2$d&height=%3$d";
  private final List<CurationsFeedItem> feedItems;
  private final CurationsImageLoader imageLoader;
  private final CurationsInfiniteContract.ViewProps viewProps;
  private final CurationsInfiniteRecyclerView.OnFeedItemClickListener feedItemClickListener;

  // endregion

  // region Constructor

  CurationsInfiniteAdapter(@NonNull final CurationsInfiniteContract.ViewProps viewProps, @NonNull CurationsImageLoader imageLoader, @Nullable final CurationsInfiniteRecyclerView.OnFeedItemClickListener feedItemClickListener) {
    this.viewProps = viewProps;
    this.imageLoader = imageLoader;
    this.feedItemClickListener = feedItemClickListener;
    feedItems = new ArrayList<>();
  }

  // endregion

  // region Adapter Implementation

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    CurationsView curationsView = (CurationsView) LayoutInflater.from(parent.getContext()).inflate(R.layout.curationsui_cell_item, parent, false);
    RelativeLayout cellContent = (RelativeLayout) curationsView.findViewById(R.id.curationCellContent);
    CurationsAspectRatioImageView imageView = (CurationsAspectRatioImageView) curationsView.findViewById(R.id.curationImage);

    // CurationsAspectRatioImageView manages setting the cells dimensions
    imageView.setRatio(viewProps.getCurationCellWidthRatio(), viewProps.getCurationCellHeightRatio());

    boolean isVertical = isVertical(viewProps);

    // Update CurationsView LayoutParams
    CurationsInfiniteRecyclerView.LayoutParams curLp = (CurationsInfiniteRecyclerView.LayoutParams) curationsView.getLayoutParams();
    curLp.width = isVertical ?
        CurationsInfiniteRecyclerView.LayoutParams.MATCH_PARENT : CurationsInfiniteRecyclerView.LayoutParams.WRAP_CONTENT;
    curLp.height = isVertical ?
        CurationsInfiniteRecyclerView.LayoutParams.WRAP_CONTENT : CurationsInfiniteRecyclerView.LayoutParams.MATCH_PARENT;
    curationsView.setLayoutParams(curLp);

    // Update Cell Container LayoutParams
    CurationsView.LayoutParams cellContentLp = (CurationsView.LayoutParams) cellContent.getLayoutParams();
    cellContentLp.width = isVertical ?
        CurationsView.LayoutParams.MATCH_PARENT : CurationsView.LayoutParams.WRAP_CONTENT;
    cellContentLp.height = isVertical ?
        CurationsView.LayoutParams.WRAP_CONTENT : CurationsView.LayoutParams.MATCH_PARENT;
    cellContent.setLayoutParams(cellContentLp);

    return new CurationsInfiniteViewHolder(curationsView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    CurationsInfiniteViewHolder viewHolder = (CurationsInfiniteViewHolder) holder;
    CurationsFeedItem currFeedItem = feedItems.get(position);
    viewHolder.container.setCurationsFeedItem(currFeedItem);
    viewHolder.container.setOnClickListener(new CurationFeedItemClickListener(feedItemClickListener, currFeedItem));

    ImageView iv = viewHolder.imageView;
    cancelRequestIfExists(iv);
    loadImageIntoImageView(currFeedItem, iv);
    loadBrandIntoBrandView(currFeedItem, viewHolder.brandImageView);
  }

  @Override
  public int getItemCount() {
    return feedItems.size();
  }

  // endregion

  // region Internal API

  private void cancelRequestIfExists(ImageView iv) {
    Object tag = imageLoader.getTag(iv);
    if (tag != null) {
      imageLoader.cancel(tag);
    }
  }

  private void loadImageIntoImageView(CurationsFeedItem feedItem, ImageView iv) {
    String imageThumbnailUrl = getPhotoThumbnailUrl(feedItem);
    if (imageThumbnailUrl != null) {
      iv.setTag(imageThumbnailUrl);
      CurationsInfiniteRecyclerView.ImageSize imageSize = getImageSize(viewProps);
      imageLoader.loadInto(iv, imageThumbnailUrl, imageSize.getWidthPixels(), imageSize.getHeightPixels());
      return;
    }
  }

  private void loadBrandIntoBrandView(CurationsFeedItem feedItem, ImageView brandImageView) {
    String channelStr = feedItem.getChannel();
    if (channelStr == null) {
      brandImageView.setVisibility(View.GONE);
      return;
    }
    Channel channel = Channel.toChannel(channelStr);
    if (channel == null) {
      brandImageView.setVisibility(View.GONE);
      return;
    }
    @DrawableRes int brandDrawableResId = 0;
    switch (channel) {
      case INSTAGRAM: {
        brandDrawableResId = R.drawable.curationsui_instagram;
        break;
      }
      case BAZAARVOICE: {
        brandDrawableResId = R.drawable.curationsui_bv;
        break;
      }
      case FACEBOOK: {
        brandDrawableResId = R.drawable.curationsui_facebook;
        break;
      }
      case GOOGLE_PLUS: {
        brandDrawableResId = R.drawable.curationsui_google_plus;
        break;
      }
      case PINTEREST: {
        brandDrawableResId = R.drawable.curationsui_pinterest;
        break;
      }
      case PLAY: {
        brandDrawableResId = R.drawable.curationsui_play;
        break;
      }
      case TWITTER: {
        brandDrawableResId = R.drawable.curationsui_twitter;
        break;
      }
      case YOUTUBE: {
        brandDrawableResId = R.drawable.curationsui_youtube;
        break;
      }
    }
    if (brandDrawableResId != 0) {
      Drawable brandDrawable;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        brandDrawable = brandImageView.getResources().getDrawable(brandDrawableResId, brandImageView.getContext().getTheme());
      } else {
        brandDrawable = brandImageView.getResources().getDrawable(brandDrawableResId);
      }
      brandImageView.setVisibility(View.VISIBLE);
      brandImageView.setImageDrawable(brandDrawable);
    }
  }

  /**
   * @param feedItem Input feed item to get an image for
   * @return Thumbnail url for photo or video content, null if one is
   * not found
   */
  @SuppressLint("DefaultLocale") @Nullable
  private String getPhotoThumbnailUrl(CurationsFeedItem feedItem) {
    String thumbnailUrl = null;
    CurationsInfiniteRecyclerView.ImageSize imageSize = getImageSize(viewProps);

    // Look for Photo Thumbnail
    List<CurationsPhoto> photos = feedItem.getPhotos();
    if (photos != null && !photos.isEmpty()) {
      String imageServiceUrl = photos.get(0).getImageServiceUrl();
      if (imageServiceUrl == null) {
        return null;
      }
      thumbnailUrl = String.format(SERVICE_IMAGE_TEMPLATE, imageServiceUrl, imageSize.getWidthPixels(), imageSize.getHeightPixels());
    }

    // Look for Video Thumbnail
    List<CurationsVideo> videos = feedItem.getVideos();
    if (videos != null && !videos.isEmpty()) {
      String imageServiceUrl = videos.get(0).getImageServiceUrl();
      if (imageServiceUrl == null) {
        return null;
      }
      thumbnailUrl = String.format(SERVICE_IMAGE_TEMPLATE, imageServiceUrl, imageSize.getWidthPixels(), imageSize.getHeightPixels());
    }

    return thumbnailUrl;
  }

  void update(List<CurationsFeedItem> newFeedItems) {
    // TODO use notifyItemInserted instead for optimal layout and to allow item animations
    this.feedItems.addAll(newFeedItems);
    notifyDataSetChanged();
  }

  private static class CurationsInfiniteViewHolder extends RecyclerView.ViewHolder {
    private CurationsView container;
    private CurationsAspectRatioImageView imageView;
    private ImageView brandImageView;

    CurationsInfiniteViewHolder(View itemView) {
      super(itemView);
      this.container = (CurationsView) itemView;
      this.imageView = (CurationsAspectRatioImageView) container.findViewById(R.id.curationImage);
      this.brandImageView = (ImageView) container.findViewById(R.id.curationCellBrandIcon);
    }
  }

  private static class CurationFeedItemClickListener implements View.OnClickListener {
    private final CurationsInfiniteRecyclerView.OnFeedItemClickListener feedItemClickListener;
    private final CurationsFeedItem curationsFeedItem;

    public CurationFeedItemClickListener(@Nullable final CurationsInfiniteRecyclerView.OnFeedItemClickListener feedItemClickListener, @NonNull final CurationsFeedItem curationsFeedItem) {
      this.feedItemClickListener = feedItemClickListener;
      this.curationsFeedItem = curationsFeedItem;
    }

    @Override
    public void onClick(View v) {
      if (feedItemClickListener != null) {
        feedItemClickListener.onClick(curationsFeedItem);
      }
    }
  }

  private enum Channel {
    INSTAGRAM("instagram"), BAZAARVOICE("bazaarvoice"),
    FACEBOOK("facebook"), GOOGLE_PLUS("google-plus"),
    PINTEREST("pinterest"), PLAY("play"),
    TWITTER("twitter"), YOUTUBE("youtube");

    private String name;

    Channel(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    @Nullable
    public static Channel toChannel(@NonNull String name) {
      Channel channel = null;

      if (name.equals(INSTAGRAM.getName())) {
        channel = INSTAGRAM;
      } else if (name.equals(BAZAARVOICE.getName())) {
        channel = BAZAARVOICE;
      } else if (name.equals(FACEBOOK.getName())) {
        channel = FACEBOOK;
      } else if (name.equals(GOOGLE_PLUS.getName())) {
        channel = GOOGLE_PLUS;
      } else if (name.equals(PINTEREST.getName())) {
        channel = PINTEREST;
      } else if (name.equals(PLAY.getName())) {
        channel = PLAY;
      } else if (name.equals(TWITTER.getName())) {
        channel = TWITTER;
      } else if (name.equals(YOUTUBE.getName())) {
        channel = YOUTUBE;
      }

      return channel;
    }
  }

  // endregion
}
