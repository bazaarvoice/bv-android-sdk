package com.bazaarvoice.bvsdkdemoandroid.curations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.DemoMainActivity;
import com.bazaarvoice.bvsdkdemoandroid.DemoRouter;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemoCurationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemoCurationsFragment extends Fragment {

    @Inject DemoClient demoClient;

    public static DemoCurationsFragment newInstance() {
        DemoCurationsFragment fragment = new DemoCurationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoApp.get(getContext()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_curations, container, false);

        Button feedBtn = (Button) view.findViewById(R.id.viewCurationsBtn);
        feedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    ((DemoMainActivity) getActivity()).transitionToCurationsFeed();
                }
            }
        });

        Button postBtn = (Button) view.findViewById(R.id.postCurationsBtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    ((DemoMainActivity) getActivity()).transitionToCurationsPost();
                }
            }
        });

        Button locBtn = (Button) view.findViewById(R.id.viewCurationsLocBtn);
        locBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    DemoRouter.transitionToCurationsMapView(DemoCurationsFragment.this.getContext(), "");
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        readyForDemo();
    }

    private boolean readyForDemo() {
        if (demoClient.isMockClient()) {
            return true;
        }

        String curationsKey = demoClient.getApiKeyCurations();
        String displayName = demoClient.getDisplayName();

        if (!DemoConstants.isSet(curationsKey)) {
            String errorMessage = String.format(getString(R.string.view_demo_error_message), displayName, getString(R.string.demo_curations));
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(errorMessage);
            builder.setNegativeButton("Ok", null).create().show();
            return false;
        }

        return true;
    }

}
