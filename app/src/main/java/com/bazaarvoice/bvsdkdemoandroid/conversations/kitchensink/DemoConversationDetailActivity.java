/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid.conversations.kitchensink;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bazaarvoice.bvsdkdemoandroid.R;

/**
 *  @deprecated - Old API to be removed
 */
public class DemoConversationDetailActivity extends AppCompatActivity {

    public final static String URL_EXTRA_KEY = "urlextrakey";
    public final static String RESPONSE_EXTRA_KEY = "responseextrakey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bvconversation_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView urlText = (TextView) findViewById(R.id.requestText);
        urlText.setText(getIntent().getStringExtra(URL_EXTRA_KEY));

        TextView responseText = (TextView) findViewById(R.id.responseText);
        responseText.setText(getIntent().getStringExtra(RESPONSE_EXTRA_KEY));
    }

}
