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
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;

import javax.inject.Inject;

public class DemoPreferencesFragment extends PreferenceFragmentCompat {

    private ListPreference selectedConfigPref;

    @Inject DemoConfigUtils demoConfigUtils;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.profile_preferences);

        CharSequence[] displayNames = demoConfigUtils.getDisplayNames();
        CharSequence[] clientIdNames = demoConfigUtils.getClientIdNames();

        String defaultClientId = demoConfigUtils.getCurrentConfig().clientId;

        selectedConfigPref = (ListPreference) findPreference(getString(R.string.key_selected_config));
        selectedConfigPref.setDefaultValue(defaultClientId);
        selectedConfigPref.setEntries(displayNames);
        selectedConfigPref.setEntryValues(clientIdNames);

        DemoConfig selectedConfig = demoConfigUtils.getCurrentConfig();
        updateUserProfile(selectedConfig);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        DemoApp.get(getContext()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        selectedConfigPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String selectedClient = (String) o;
                DemoConfig selectedConfig = demoConfigUtils.getConfigFromClientId(selectedClient);
                updateUserProfile(selectedConfig);
                return true;
            }
        });

    }

    private void updateUserProfile(DemoConfig newSelectedConfig) {
        ListPreference selectedProfilePref = (ListPreference) findPreference(getString(R.string.key_selected_config));
        String oldClientId = demoConfigUtils.getClientId();

        // Parse the values from the config
        String clientId = newSelectedConfig.clientId;

        // Update the ui preference objects
        selectedProfilePref.setSummary(newSelectedConfig.toString());

        // Update values on disk if the clientId has changed
        if (!oldClientId.equals(clientId)) {
            demoConfigUtils.putClientId(clientId);
        }
    }

}
