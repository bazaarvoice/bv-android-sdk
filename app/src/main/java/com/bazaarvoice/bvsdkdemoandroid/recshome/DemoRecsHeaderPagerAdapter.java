/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.recshome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class DemoRecsHeaderPagerAdapter extends FragmentPagerAdapter {

    public DemoRecsHeaderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return DemoHeaderPageFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

}
