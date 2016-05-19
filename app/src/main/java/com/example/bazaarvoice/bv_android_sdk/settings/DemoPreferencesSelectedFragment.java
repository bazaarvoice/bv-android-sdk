/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.example.bazaarvoice.bv_android_sdk.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.example.bazaarvoice.bv_android_sdk.DemoApplication;
import com.example.bazaarvoice.bv_android_sdk.DemoConstants;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.utils.DemoConfig;
import com.example.bazaarvoice.bv_android_sdk.utils.DemoConfigUtils;

public class DemoPreferencesSelectedFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences_selected);
        updateNewConfig(DemoConfigUtils.getInstance(getContext()).getCurrentConfig());
        getContext().getSharedPreferences(DemoConfigUtils.CONFIG_SHARED_PREFS, Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_client_id))) {
            DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(getContext());
            String newClientId = demoConfigUtils.getClientId();
            DemoConfig newSelectedConfig = demoConfigUtils.getConfigFromClientId(newClientId);
            updateNewConfig(newSelectedConfig);
        }
    }

    private void updateNewConfig(DemoConfig newSelectedConfig) {
        // Get the ui preference objects
        Preference shopperAdPasskeyPref = findPreference(getString(R.string.key_shopper_ad_passkey));
        Preference conversationsPasskeyPref = findPreference(getString(R.string.key_conversations_passkey));
        Preference curationsPasskeyPref = findPreference(getString(R.string.key_curations_passkey));

        // Parse the values from the config
        String clientId = newSelectedConfig.clientId;
        String shopperAdPasskey = newSelectedConfig.apiKeyShopperAdvertising;
        String conversationsPasskey = newSelectedConfig.apiKeyConversations;
        String curationsPasskey = newSelectedConfig.apiKeyCurations;

        // Update the ui preference objects
        shopperAdPasskeyPref.setSummary(newSelectedConfig.hasShopperAds() ? shopperAdPasskey : getNotSetString());
        conversationsPasskeyPref.setSummary(newSelectedConfig.hasConversations() ? conversationsPasskey : getNotSetString());
        curationsPasskeyPref.setSummary(newSelectedConfig.hasCurations() ? curationsPasskey : getNotSetString());

        Log.d("Config", "New client data picked - clientId: " + clientId + ", shopperAdPasskey: " + shopperAdPasskey + ", conversationsPasskey: " + conversationsPasskey + ", curationsPasskey: " + curationsPasskey);

        // If the data is valid, restart the BVSDK with the new profile
        if (clientId != null) {
            BVSDK bvsdk = BVSDK.getInstance();
            bvsdk = null;
            DemoApplication.cleanUp();
            BVSDK newBvsdk = new BVSDK.Builder(getActivity().getApplication(), clientId)
                    .apiKeyShopperAdvertising(shopperAdPasskey)
                    .apiKeyConversations(conversationsPasskey)
                    .apiKeyCurations(curationsPasskey)
                    .bazaarEnvironment(DemoConstants.ENVIRONMENT)
                    .logLevel(DemoConstants.LOG_LEVEL)
                    .build();
            newBvsdk.setUserAuthString(DemoConstants.BV_USER_AUTH_STRING);
        } else {
            new AlertDialog.Builder(getContext())
                    .setMessage("No profile loaded for this client currently")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }
    }

    private String getNotSetString() {
        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(getContext());
        return demoConfigUtils.isDemoClient() ? "" : getString(R.string.product_not_setup);
    }
}
