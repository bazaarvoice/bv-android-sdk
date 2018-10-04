package com.bazaarvoice.bvandroidsdk;

import android.widget.ImageView;

import androidx.annotation.Nullable;

public interface CurationsImageLoader {
  /**
   * Use this to cancel in flight requests. The tag will be one that
   * you provide in {@link #getTag(ImageView)}. This method will be
   * called when the view is unbound from the imageurl. The request
   * may or may not have completed already.
   * <br/><br/>
   * Picasso Example:<br/>
   * <pre>{@code picasso.cancelTag(tag);}</pre>
   *
   * @param tag Object used to identify the request in flight
   */
  void cancel(Object tag);

  /**
   * Use this to manage loading the provided imageUrl into the provided
   * imageView. This is here so that you may reuse any disk/memory caching,
   * and network layer you already have around image fetching.
   * <br/><br/>
   * The imageUrl will be for the relevant
   * {@link com.bazaarvoice.bvandroidsdk.CurationsFeedItem} with the width
   * and height of the image at imageUrl of widthPixels and heightPixels
   * <br/><br/>
   * Picasso Example:<br/>
   * <pre>{@code
   * imageView.setTag(imageUrl);
   * picasso.load(imageUrl)
   *  .tag(imageUrl)
   *  .centerCrop()
   *  .resize(imageSize.getWidthPixels(), imageSize.getHeightPixels())
   *  .into(imageView);}</pre>
   *
   * @param imageView {@link ImageView} to load the image into
   * @param imageUrl Url to fetch the image from
   * @param widthPixels Width in pixels of the hosted image. Optimized to container dimensions
   * @param heightPixels Height in pixels of the hosted image. Optimized to container dimensions
   */
  void loadInto(ImageView imageView, String imageUrl, int widthPixels, int heightPixels);

  /**
   * Use this to get a tag from an {@link ImageView} which you should
   * set while implementing {@link #loadInto(ImageView, String, int, int)}.
   * Although this is optional, it is encouraged to improve performance
   * when scrolling on a cold load.
   * <br/><br/>
   * A common implementation would be to call {@link android.view.View#setTag(Object tag)}
   * setting the tag to be the imageUrl
   * <br/><br/>
   * Picasso Example:<br/>
   * <pre>{@code return (String) imageView.getTag();}</pre>
   *
   * @param imageView The {@link ImageView} to obtain a
   *                  tag from.
   * @return Some object associated with the network request.
   */
  @Nullable
  Object getTag(ImageView imageView);
}