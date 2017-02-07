package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.bazaarvoice.bvandroidsdk_curationsui.R;

import static android.view.View.MeasureSpec.EXACTLY;

final class CurationsAspectRatioImageView extends AppCompatImageView {
  private static final String TAG = "AspRatioImageView";
  private int widthRatio;
  private int heightRatio;

  public CurationsAspectRatioImageView(Context context, int widthRatio, int heightRatio) {
    super(context);
    this.widthRatio = widthRatio;
    this.heightRatio = heightRatio;
  }

  public CurationsAspectRatioImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CurationsAspectRatioImageView);
    widthRatio = a.getInteger(R.styleable.CurationsAspectRatioImageView_widthRatio, 1);
    heightRatio = a.getInteger(R.styleable.CurationsAspectRatioImageView_heightRatio, 1);
    a.recycle();
  }

  public void setRatio(int widthRatio, int heightRatio) {
    this.widthRatio = widthRatio;
    this.heightRatio = heightRatio;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
    int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

    if (widthMode == EXACTLY) {
      if (heightMode != EXACTLY) {
        heightSize = (int) (widthSize * 1f / widthRatio * heightRatio);
      }
    } else if (heightMode == EXACTLY) {
      widthSize = (int) (heightSize * 1f / heightRatio * widthRatio);
    } else {
      throw new IllegalStateException("Either width or height must be EXACTLY.");
    }

    heightSize = (int) (getScaleY() * heightSize);
    widthSize = (int) (getScaleX() * widthSize);

    widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(widthSize, EXACTLY);
    heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(heightSize, EXACTLY);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

}