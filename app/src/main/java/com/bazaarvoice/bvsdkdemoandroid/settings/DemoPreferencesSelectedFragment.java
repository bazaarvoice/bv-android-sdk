/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoLaunchIntentUtil;
import com.jakewharton.processphoenix.ProcessPhoenix;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class DemoPreferencesSelectedFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject DemoClientConfigUtils demoClientConfigUtils;
    @Inject DemoClient demoClient;
    @Inject SharedPreferences sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        DemoApp.getAppComponent(getContext()).inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences_selected);
        updateSettingsUi(demoClient);
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_client_id))) {
            String newClientId = demoClientConfigUtils.getSharedPrefsClientId();
            DemoClient newSelectedConfig = demoClientConfigUtils.getConfigFromClientId(newClientId);
            updateSettingsUi(newSelectedConfig);
            updateNewConfig(newSelectedConfig);
        }
    }

    private void updateSettingsUi(DemoClient selectedConfig) {
        // Get the ui preference objects
        Preference shopperAdPasskeyPref = findPreference(getString(R.string.key_shopper_ad_passkey));
        Preference conversationsPasskeyPref = findPreference(getString(R.string.key_conversations_passkey));
        Preference curationsPasskeyPref = findPreference(getString(R.string.key_curations_passkey));
        Preference progressiveSubmissionPasskeyPref = findPreference(getString(R.string.key_progressive_passkey));

        // Parse the values from the config
        String shopperAdPasskey = selectedConfig.getApiKeyShopperAdvertising();
        String conversationsPasskey = selectedConfig.getApiKeyConversations();
        String curationsPasskey = selectedConfig.getApiKeyCurations();
        String progressiveSubmissionPasskey = selectedConfig.getApiKeyProgressiveSubmission();

        // Update the ui preference objects
        shopperAdPasskeyPref.setSummary(selectedConfig.hasShopperAds() ? shopperAdPasskey : getNotSetString());
        conversationsPasskeyPref.setSummary(selectedConfig.hasConversations() ? conversationsPasskey : getNotSetString());
        curationsPasskeyPref.setSummary(selectedConfig.hasCurations() ? curationsPasskey : getNotSetString());
        progressiveSubmissionPasskeyPref.setSummary(progressiveSubmissionPasskey);

    }

    private void updateNewConfig(DemoClient newSelectedConfig) {
        // Parse the values from the config
        String clientId = newSelectedConfig.getClientId();
        String shopperAdPasskey = newSelectedConfig.getApiKeyShopperAdvertising();
        String conversationsPasskey = newSelectedConfig.getApiKeyConversations();
        String curationsPasskey = newSelectedConfig.getApiKeyCurations();

        Log.d("Config", "New client data picked - clientId: " + clientId + ", shopperAdPasskey: " + shopperAdPasskey + ", conversationsPasskey: " + conversationsPasskey + ", curationsPasskey: " + curationsPasskey);

        // If the data is valid, restart the BVSDK with the new profile
        if (clientId != null) {
            try {
                Thread.sleep(1000);
                BVSDK bvsdk = BVSDK.getInstance();
                bvsdk = null;
                DemoApp.cleanUp();
                if (getActivity() != null && !getActivity().isFinishing()) {
                    int launchCode = ((DemoSettingsActivity) getActivity()).getLaunchCode();
                    Intent nextIntent = DemoLaunchIntentUtil.getLaunchIntent(getContext(), launchCode);
                    ProcessPhoenix.triggerRebirth(getContext(), nextIntent);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            new AlertDialog.Builder(getContext())
                    .setMessage("No profile loaded for this client currently")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }
    }

    private String getNotSetString() {
        return demoClient.isMockClient() ? "" : getString(R.string.product_not_setup);
    }
}
