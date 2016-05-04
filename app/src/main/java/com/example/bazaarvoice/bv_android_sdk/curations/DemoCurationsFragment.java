package com.example.bazaarvoice.bv_android_sdk.curations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bazaarvoice.bv_android_sdk.DemoMainActivity;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.di.DemoAppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfiguration;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfigurationImpl;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemoCurationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemoCurationsFragment extends Fragment {


    public DemoCurationsFragment() {
        // Required empty public constructor
    }

    public static DemoCurationsFragment newInstance() {
        DemoCurationsFragment fragment = new DemoCurationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_curations, container, false);

        readyForDemo();
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
        return view;
    }

    private boolean readyForDemo() {
        DemoUserConfiguration demoUserConfiguration = DemoAppConfigurationImpl.getInstance().provideBvUserComponent();
        String curationsKey = demoUserConfiguration.provideApiKeyCurations();
        String clientId = demoUserConfiguration.provideBvClientId();

        String errorVal = null;
        if (curationsKey.equals(DemoUserConfigurationImpl.REPLACE_ME)) {
            errorVal = "BV_CURATIONS_API_KEY";
        } else if (clientId.equals(DemoUserConfigurationImpl.REPLACE_ME)) {
            errorVal = "BV_CLIENT_ID";
        }

        if (errorVal != null) {
            String errorMessage = String.format(getResources().getString(R.string.view_demo_error_message), errorVal);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(errorMessage);
            builder.setNegativeButton("Ok", null).create().show();
            return false;
        }

        return true;
    }
}
