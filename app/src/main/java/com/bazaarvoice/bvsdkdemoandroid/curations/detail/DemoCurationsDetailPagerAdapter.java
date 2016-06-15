/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.curations.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;

import java.util.Collections;
import java.util.List;

/**
 * TODO: Describe file here.
 */
class DemoCurationsDetailPagerAdapter extends FragmentPagerAdapter {

    private List<CurationsFeedItem> curationsFeedItems = Collections.emptyList();

    public DemoCurationsDetailPagerAdapter(FragmentManager fm, List<CurationsFeedItem> curationsFeedItems) {
        super(fm);
        this.curationsFeedItems = curationsFeedItems;
    }

    @Override
    public Fragment getItem(int position) {
        boolean showLeftArrow = position > 0;
        boolean showRightArrow = position < (curationsFeedItems.size() - 1);
        return DemoCurationsDetailFragment.newInstance(curationsFeedItems.get(position), position, showLeftArrow, showRightArrow);
    }

    @Override
    public int getCount() {
        return curationsFeedItems.size();
    }
}
