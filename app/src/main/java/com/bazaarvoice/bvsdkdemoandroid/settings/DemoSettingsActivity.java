/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bazaarvoice.bvsdkdemoandroid.BuildConfig;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoLaunchIntentUtil;

/**
 * In order to add your own custom settings create a file named
 * plist file in the app/src/main/assets/ directory, and copy paste
 * the contents from the bv_profile_demo.xml file, swapping in
 * your desired values.
 */
public class DemoSettingsActivity extends AppCompatActivity {
    private static final String EXTRA_LAUNCH_CODE = "extra_launch_code";
    private int launchCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            launchCode = getIntent().getIntExtra(EXTRA_LAUNCH_CODE, DemoLaunchIntentUtil.LaunchCode.API);
        }
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);

        ViewGroup selectedFragContainer = (ViewGroup) findViewById(R.id.frag_selected_container);
        selectedFragContainer.setVisibility(BuildConfig.DEBUG ? View.VISIBLE : View.GONE);
    }

    public int getLaunchCode() {
        return launchCode;
    }

    /**
     * @param activityContext Context of activity transitioning from
     * @param launchCode Launch code from {@link com.bazaarvoice.bvsdkdemoandroid.utils.DemoLaunchIntentUtil.LaunchCode}
     */
    public static void transitionTo(Context activityContext, int launchCode) {
        Intent intent = new Intent(activityContext, DemoSettingsActivity.class);
        intent.putExtra(EXTRA_LAUNCH_CODE, launchCode);
        activityContext.startActivity(intent);
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
