/**
 * A simple {@link Fragment} subclass.
 *
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.conversations.browseproducts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bazaarvoice.bv_android_sdk.MainActivity;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.di.AppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.UserConfiguration;

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

        checkForDemoInput();

        searchField = (EditText) view.findViewById(R.id.searchField);
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
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
                performSearch();
            }

        });

        return view;
    }

    private void checkForDemoInput() {
        UserConfiguration userConfiguration = AppConfigurationImpl.getInstance().provideBvUserComponent();
        String conversationsKey = userConfiguration.provideApiKeyConversations();
        String clientId = userConfiguration.provideBvClientId();

        String errorVal = null;
        if (conversationsKey.equals("REPLACE_ME")) {
            errorVal = "conversationsKey";
        } else if (clientId.equals("REPLACE_ME")) {
            errorVal = "clientId";
        }

        if (errorVal != null) {
            String errorMessage = String.format(getResources().getString(R.string.view_demo_error_message), errorVal);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Launches the next activity and passes along the search term.
     */
    protected void performSearch() {
        ((MainActivity)getActivity()).transitionToProductSearch(searchField.getText().toString());
    }
}
