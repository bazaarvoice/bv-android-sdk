/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.recshome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;

public class DemoHeaderPageFragment extends Fragment {

    private static final String ARG_INDEX = "arg_index";

    public DemoHeaderPageFragment() {
    }

    public static DemoHeaderPageFragment getInstance(int index) {
        DemoHeaderPageFragment demoHeaderPageFragment = new DemoHeaderPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_INDEX, index);
        demoHeaderPageFragment.setArguments(bundle);
        return demoHeaderPageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_header, container, false);

        int index = getArguments().getInt(ARG_INDEX);

        int resId;
        switch (index) {
            case 0:
                resId = R.drawable.endurance_banner_1;
                break;
            case 1:
                resId = R.drawable.endurance_banner_2;
                break;
            case 2:
            default:
                resId = R.drawable.endurance_banner_3;
                break;
        }

        ImageView background = (ImageView) view.findViewById(R.id.background);

        DemoUtils demoUtils = DemoUtils.getInstance(getContext());
        demoUtils.picassoThumbnailLoader()
                .load(resId)
                .resizeDimen(R.dimen.side_not_set, R.dimen.detail_backdrop_height)
                .into(background);

        return view;
    }

}
