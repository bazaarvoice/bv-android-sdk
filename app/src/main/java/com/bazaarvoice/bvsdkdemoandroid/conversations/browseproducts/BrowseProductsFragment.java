/**
 * A simple {@link Fragment} subclass.
 *
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts;


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

import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.DemoMainActivity;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;

/**
 * @deprecated Using ConversationsDemoFragment
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
                if (readyForDemo()) {
                    performSearch();
                }
            }

        });

        return view;
    }

    /**
     * Launches the next activity and passes along the search term.
     */
    protected void performSearch() {
        ((DemoMainActivity)getActivity()).transitionToProductSearch(searchField.getText().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        readyForDemo();
    }

    private boolean readyForDemo() {
        DemoConfig currentConfig = DemoConfigUtils.getInstance(getContext()).getCurrentConfig();
        String conversationsKey = currentConfig.apiKeyConversations;
        String displayName = currentConfig.displayName;

        if (!DemoConstants.isSet(conversationsKey)) {
            String errorMessage = String.format(getString(R.string.view_demo_error_message), displayName, getString(R.string.demo_conversations));
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(errorMessage);
            builder.setNegativeButton("Ok", null).create().show();
            return false;
        }

        return true;
    }

}
