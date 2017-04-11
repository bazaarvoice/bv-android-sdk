/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.bazaarvoice.bvsdkdemoandroid.configs;

import android.content.Context;
import android.content.SharedPreferences;

import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;
import com.bazaarvoice.bvsdkdemoandroid.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * The main interface to this package for information about the current
 * state of the BVSDK options that are currently chosen, or available to
 * choose from.
 */
public class DemoClientConfigUtils {
    private final Context context;
    private final BazaarEnvironment bazaarEnvironment;
    private final DemoMultiTenantSource demoMultiTenantSource;
    private final DemoSingleTenantSource demoSingleTenantSource;
    private final List<DemoClient> allStagingSources, allProdSources;
    private final SharedPreferences sharedPrefs;

    @Inject
    DemoClientConfigUtils(
        Context context,
        BazaarEnvironment bazaarEnvironment,
        DemoMultiTenantSource demoMultiTenantSource,
        DemoSingleTenantSource demoSingleTenantSource,
        SharedPreferences sharedPrefs) {
        this.context = context;
        this.bazaarEnvironment = bazaarEnvironment;
        this.demoMultiTenantSource = demoMultiTenantSource;
        this.demoSingleTenantSource = demoSingleTenantSource;
        this.sharedPrefs = sharedPrefs;
        allStagingSources = new ArrayList<>();
        allStagingSources.add(DemoClient.EMPTY_CONFIG);
        allStagingSources.addAll(demoSingleTenantSource.getStagingClients());
        allStagingSources.addAll(demoMultiTenantSource.getStagingClients());
        allProdSources = new ArrayList<>();
        allProdSources.add(DemoClient.EMPTY_CONFIG);
        allProdSources.addAll(demoSingleTenantSource.getProdClients());
        allProdSources.addAll(demoMultiTenantSource.getProdClients());
    }

    // region SharedPrefs Helper Methods

    private void putStringInPrefs(String key, String value) {
        sharedPrefs.edit().putString(key, value).commit();
    }

    private String getStringInPrefs(String key, String defaultValue) {
        return sharedPrefs.getString(key, defaultValue);
    }

    // endregion

    /**
     * @param clientId The new chosen clientId to persist
     */
    public void putSharedPrefsClientId(String clientId) {
        String keyClientId = context.getString(R.string.key_client_id);
        putStringInPrefs(keyClientId, clientId);
    }

    /**
     * @return The current persisted clientId for the current config
     */
    public String getSharedPrefsClientId() {
        String keyClientId = context.getString(R.string.key_client_id);
        String defaultClientId = DemoClient.EMPTY_CONFIG.getClientId();
        return getStringInPrefs(keyClientId, defaultClientId);
    }

    /**
     * @return The current chosen config based on 1) the current persisted clientId, and 2) the current chosen environment
     */
    public DemoClient getCurrentConfig() {
        return getConfigFromClientId(getSharedPrefsClientId());
    }

    /**
     * @param clientId A clientId of one of the available {@link DemoClientSource}s
     * @return The config based on 1) the provided clientId, and 2) the current chosen environment
     */
    public DemoClient getConfigFromClientId(String clientId) {
        List<DemoClient> demoClients = bazaarEnvironment == BazaarEnvironment.STAGING ?
            allStagingSources :
            allProdSources;
        return DemoClientSource.Util.getConfigFromClientId(clientId, demoClients);
    }

    /**
     * @return Whether the multi-tenant config file is in the assets directory
     */
    public boolean otherSourcesExist() {
        return demoMultiTenantSource.doesSourceExist() ||
            demoSingleTenantSource.doesSourceExist();
    }

    /**
     * @return The available configs for the current chosen environment
     */
    public List<DemoClient> getCurrentSources() {
        return bazaarEnvironment == BazaarEnvironment.STAGING ? allStagingSources : allProdSources;
    }

    // region Location Persistance - TODO Should go in own Util class, not here

    public void putLastLocationEvent(String lastLocationEvent) {
        String keyLastLocationEvent = context.getString(R.string.key_last_location_event);
        putStringInPrefs(keyLastLocationEvent, lastLocationEvent);
    }

    public String getLastLocationEvent() {
        String keyLastLocationEvent = context.getString(R.string.key_last_location_event);
        return getStringInPrefs(keyLastLocationEvent, "none");
    }

    // endregion
}
