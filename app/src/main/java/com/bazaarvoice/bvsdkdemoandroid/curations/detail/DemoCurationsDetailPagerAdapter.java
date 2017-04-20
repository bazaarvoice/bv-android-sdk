/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.curations.detail;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DemoCurationsDetailPagerAdapter extends FragmentPagerAdapter {

    @Inject Gson gson;
    private List<CurationsFeedItem> curationsFeedItems = new ArrayList<>();

    public DemoCurationsDetailPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        DemoApp.getAppComponent(context).inject(this);
    }

    @Override
    public Fragment getItem(int position) {
        boolean showLeftArrow = position > 0;
        boolean showRightArrow = position < (curationsFeedItems.size() - 1);
        return DemoCurationsDetailFragment.newInstance(gson, curationsFeedItems.get(position), position, showLeftArrow, showRightArrow);
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
