/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.example.bazaarvoice.bv_android_sdk.settings;

import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.utils.DemoConfig;
import com.example.bazaarvoice.bv_android_sdk.utils.DemoConfigUtils;

public class DemoPreferencesFragment extends PreferenceFragmentCompat {

    private ListPreference selectedConfigPref;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.profile_preferences);

        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(getContext());
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
        super.onCreate(savedInstanceState);
        selectedConfigPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String selectedClient = (String) o;
                DemoConfig selectedConfig = DemoConfigUtils.getInstance(getContext()).getConfigFromClientId(selectedClient);
                updateUserProfile(selectedConfig);
                return true;
            }
        });

    }

    private void updateUserProfile(DemoConfig newSelectedConfig) {
        ListPreference selectedProfilePref = (ListPreference) findPreference(getString(R.string.key_selected_config));
        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(getContext());
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
