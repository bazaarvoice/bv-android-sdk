/**
 * A simple {@link Fragment} subclass.
 *
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid;


import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 */
public class DemoHomePageFragment extends Fragment {

    TextView bvsdkWiki;

    public DemoHomePageFragment() {
        // Required empty public constructor
    }

    public static DemoHomePageFragment newInstance() {
        DemoHomePageFragment fragment = new DemoHomePageFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_home_page, container, false);

        bvsdkWiki = (TextView) view.findViewById(R.id.greetingText);
        Linkify.addLinks(bvsdkWiki, Linkify.WEB_URLS);

        return view;
    }

}
