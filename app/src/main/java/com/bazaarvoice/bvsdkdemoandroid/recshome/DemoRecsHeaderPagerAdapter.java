/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.recshome;

import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;

class DemoRecsHeaderPagerAdapter extends PagerAdapter {

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        DemoRecsHeader header = DemoRecsHeader.values()[position];
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ImageView headerView = (ImageView) inflater.inflate(R.layout.frag_home_header, container, false);
        DemoUtils demoUtils = DemoUtils.getInstance(container.getContext());
        demoUtils.picassoThumbnailLoader()
                .load(header.getDrawableResId())
                .resizeDimen(R.dimen.side_not_set, R.dimen.detail_backdrop_height)
                .into(headerView);
        container.addView(headerView);
        return headerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return DemoRecsHeader.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    enum DemoRecsHeader {
        BANNER_1(R.drawable.endurance_banner_1),
        BANNER_2(R.drawable.endurance_banner_2),
        BANNER_3(R.drawable.endurance_banner_3);

        @DrawableRes private int drawableResId;

        DemoRecsHeader(@DrawableRes int drawableResId) {
            this.drawableResId = drawableResId;
        }

        public int getDrawableResId() {
            return drawableResId;
        }
    }
}
