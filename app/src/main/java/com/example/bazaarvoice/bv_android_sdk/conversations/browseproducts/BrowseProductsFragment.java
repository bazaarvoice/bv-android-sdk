/**
 * A simple {@link Fragment} subclass.
 *
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.conversations.browseproducts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bazaarvoice.bv_android_sdk.DemoMainActivity;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.di.DemoAppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfiguration;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfigurationImpl;

/**
 * TODO: Description Here
 */
public class BrowseProductsFragment extends Fragment {

    private EditText searchField;

    public BrowseProductsFragment() {
        // Required empty public constructor
    }

    public static BrowseProductsFragment getInstance() {
        return new BrowseProductsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_browse_products, container, false);

        readyForDemo();

        searchField = (EditText) view.findViewById(R.id.searchField);
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && readyForDemo()) {
                    performSearch();
                    return true;
                }
                return false;
            }

        });

        Button searchButton = (Button) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (readyForDemo()){
                    performSearch();
                }
            }

        });

        return view;
    }

    private boolean readyForDemo() {
        DemoUserConfiguration demoUserConfiguration = DemoAppConfigurationImpl.getInstance().provideBvUserComponent();
        String conversationsKey = demoUserConfiguration.provideApiKeyConversations();
        String clientId = demoUserConfiguration.provideBvClientId();

        String errorVal = null;
        if (conversationsKey.equals(DemoUserConfigurationImpl.REPLACE_ME)) {
            errorVal = "BV_CONVERSATIONS_API_KEY";
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


    /**
     * Launches the next activity and passes along the search term.
     */
    protected void performSearch() {
        ((DemoMainActivity)getActivity()).transitionToProductSearch(searchField.getText().toString());
    }
}
