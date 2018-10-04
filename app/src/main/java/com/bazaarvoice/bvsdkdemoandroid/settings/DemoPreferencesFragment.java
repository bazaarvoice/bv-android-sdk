/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.preference.PreferenceFragmentCompat;

public class DemoPreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Inject DemoClientConfigUtils demoClientConfigUtils;
    @Inject DemoClient demoClient;
    @Inject @Named("SettingsDisplayNames") CharSequence[] displayNames;
    @Inject @Named("SettingsClientIdNames") CharSequence[] clientIdNames;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.profile_preferences);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        DaggerDemoSettingsComponent.builder()
            .demoAppComponent(DemoApp.getAppComponent(getActivity()))
            .demoSettingsModule(new DemoSettingsModule())
            .build()
            .inject(this);

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void updateUserProfile(DemoClient newSelectedConfig, boolean showMockedData) {

        String oldClientId = demoClient.getClientId();

        // Parse the values from the config
        String clientId = newSelectedConfig.getClientId();
        if(showMockedData) {
            clientId = DemoClient.MOCK_DISPLAY_NAME;
        } else {
            for(CharSequence name: clientIdNames) {
                if(!name.equals(DemoClient.MOCK_DISPLAY_NAME) && !name.equals("REPLACE_ME")) {
                    clientId = name.toString();
                }
            }
        }

        // Update values on disk if the clientId has changed
        if (!oldClientId.equals(clientId)) {
            demoClientConfigUtils.putSharedPrefsClientId(clientId);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("show_mocked_data")) {
            boolean showMockedData = sharedPreferences.getBoolean("show_mocked_data", true);
            updateUserProfile(demoClient, showMockedData);
        }
    }
}
