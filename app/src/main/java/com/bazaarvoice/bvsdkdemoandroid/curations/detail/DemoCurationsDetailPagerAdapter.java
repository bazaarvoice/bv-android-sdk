/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.curations.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;

import java.util.ArrayList;
import java.util.List;

class DemoCurationsDetailPagerAdapter extends FragmentPagerAdapter {

    private List<CurationsFeedItem> curationsFeedItems = new ArrayList<>();

    public DemoCurationsDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        boolean showLeftArrow = position > 0;
        boolean showRightArrow = position < (curationsFeedItems.size() - 1);
        return DemoCurationsDetailFragment.newInstance(curationsFeedItems.get(position), position, showLeftArrow, showRightArrow);
    }

    public void setCurationsFeedItems(List<CurationsFeedItem> curationsFeedItems) {
        this.curationsFeedItems.clear();
        this.curationsFeedItems.addAll(curationsFeedItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return curationsFeedItems.size();
    }
}
