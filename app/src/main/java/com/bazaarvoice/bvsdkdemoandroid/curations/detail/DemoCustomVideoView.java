package com.bazaarvoice.bvsdkdemoandroid.curations.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Bazaarvoice on 4/7/16.
 */
public class DemoCustomVideoView extends VideoView {
    private int mForceHeight = 1;
    private int mForceWidth = 1;
    public DemoCustomVideoView(Context context) {
        super(context);
    }

    public DemoCustomVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoCustomVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDimensions(int w, int h) {
        this.mForceHeight = h;
        this.mForceWidth = w;
        getHolder().setFixedSize(w, h);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mForceHeight == 1){
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }else{
            setMeasuredDimension(mForceWidth / 100 * 99, mForceHeight);
        }
    }
}
