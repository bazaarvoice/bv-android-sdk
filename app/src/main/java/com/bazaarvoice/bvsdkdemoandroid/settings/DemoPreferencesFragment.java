/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.settings;

import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;

import javax.inject.Inject;
import javax.inject.Named;

public class DemoPreferencesFragment extends PreferenceFragmentCompat {

    private ListPreference selectedConfigPref;

    @Inject DemoClientConfigUtils demoClientConfigUtils;
    @Inject DemoClient demoClient;
    @Inject @Named("SettingsDisplayNames") CharSequence[] displayNames;
    @Inject @Named("SettingsClientIdNames") CharSequence[] clientIdNames;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.profile_preferences);

        String defaultClientId = demoClient.getClientId();

        selectedConfigPref = (ListPreference) findPreference(getString(R.string.key_selected_config));
        selectedConfigPref.setDefaultValue(defaultClientId);
        selectedConfigPref.setEntries(displayNames);
        selectedConfigPref.setEntryValues(clientIdNames);

        updateUserProfile(demoClient);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        DaggerDemoSettingsComponent.builder()
            .demoAppComponent(DemoApp.getAppComponent(getActivity()))
            .demoSettingsModule(new DemoSettingsModule())
            .build()
            .inject(this);

        super.onCreate(savedInstanceState);

        selectedConfigPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String selectedClient = (String) o;
                DemoClient selectedConfig = demoClientConfigUtils.getConfigFromClientId(selectedClient);
                updateUserProfile(selectedConfig);
                return true;
            }
        });

    }

    private void updateUserProfile(DemoClient newSelectedConfig) {
        ListPreference selectedProfilePref = (ListPreference) findPreference(getString(R.string.key_selected_config));
        String oldClientId = demoClient.getClientId();

        // Parse the values from the config
        String clientId = newSelectedConfig.getClientId();

        // Update the ui preference objects
        selectedProfilePref.setSummary(newSelectedConfig.toString());

        // Update values on disk if the clientId has changed
        if (!oldClientId.equals(clientId)) {
            demoClientConfigUtils.putSharedPrefsClientId(clientId);
        }
    }

}
