/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bazaarvoice.bvsdkdemoandroid.BuildConfig;
import com.bazaarvoice.bvsdkdemoandroid.R;

/**
 * In order to add your own custom settings create a file named
 * plist file in the app/src/main/assets/ directory, and copy paste
 * the contents from the bv_profile_demo.xml file, swapping in
 * your desired values.
 */
public class DemoSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);

        ViewGroup selectedFragContainer = (ViewGroup) findViewById(R.id.frag_selected_container);
        selectedFragContainer.setVisibility(BuildConfig.DEBUG ? View.VISIBLE : View.GONE);
    }

    public static void transitionTo(Activity activity) {
        Intent intent = new Intent(activity, DemoSettingsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
