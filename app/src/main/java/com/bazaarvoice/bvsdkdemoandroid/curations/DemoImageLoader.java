package com.bazaarvoice.bvsdkdemoandroid.curations;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import com.bazaarvoice.bvandroidsdk.CurationsImageLoader;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.squareup.picasso.Picasso;

public class DemoImageLoader implements CurationsImageLoader {
  private Picasso picasso;

  public DemoImageLoader(Picasso picasso) {
    this.picasso = picasso;
  }

  @Override
  public void cancel(Object tag) {
    picasso.cancelTag(tag);
  }

  @Override
  public void loadInto(ImageView imageView, String imageUrl, int widthPixels, int heightPixels) {
    Drawable loadingDrawable;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      loadingDrawable = imageView.getResources().getDrawable(R.drawable.ic_cloud_download_black_24dp, imageView.getContext().getTheme());
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        loadingDrawable.setTint(imageView.getContext().getColor(R.color.colorPrimary));
      }
    } else {
      loadingDrawable = imageView.getResources().getDrawable(R.drawable.ic_cloud_download_black_24dp);
    }

    Drawable errorDrawable;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      errorDrawable = imageView.getResources().getDrawable(R.drawable.ic_error_outline_black_24dp, imageView.getContext().getTheme());
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        errorDrawable.setTint(imageView.getContext().getColor(R.color.bv_orange_1));
      }
    } else {
      errorDrawable = imageView.getResources().getDrawable(R.drawable.ic_error_outline_black_24dp);
    }

    imageView.setTag(imageUrl);
    picasso.load(imageUrl)
        .tag(imageUrl)
        .centerCrop()
        .config(Bitmap.Config.RGB_565)
        .resize(widthPixels, heightPixels)
        .placeholder(loadingDrawable)
        .error(errorDrawable)
        .into(imageView);
  }

  @Override
  public String getTag(ImageView imageView) {
    return (String) imageView.getTag();
  }
}