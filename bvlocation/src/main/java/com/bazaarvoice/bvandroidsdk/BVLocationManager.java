/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.content.Intent;

import com.bazaarvoice.bvandroidsdk.internal.ListenerContainer;
import com.gimbal.android.Attributes;
import com.gimbal.android.Gimbal;
import com.gimbal.android.Place;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Visit;
import com.google.gson.Gson;

import static com.bazaarvoice.bvandroidsdk.LocationAnalyticsManager.ENTRY;
import static com.bazaarvoice.bvandroidsdk.LocationAnalyticsManager.EXIT;

/**
 * Class used to monitor users location and provide information about users retail location visits
 */
public class BVLocationManager {
    private static final String TAG = BVLocationManager.class.getSimpleName();
    private static BVLocationManager instance;
    private final ListenerContainer<BVLocationListener> visitListeners;
    private PlaceManager placeManager;
    private Context appContext;

    public static final String ACTION_GEOFENCE_VISIT = "com.bazaarvoice.bvandroidsdk.action.GEOFENCE_VISIT";
    private static final String EXTRA_DID_START = "extra_did_start";
    private static final String EXTRA_BV_VISIT = "extra_bv_visit";

    private final LocationListener proxyLocationListener;

    private BVLocationManager() {
        BVSDK bvsdk = BVSDK.getInstance();
        BVUserProvidedData bvUserProvidedData = bvsdk.getBvUserProvidedData();
        appContext = bvUserProvidedData.getAppContext();
        Gimbal.setApiKey(bvUserProvidedData.getApplication(), bvUserProvidedData.getBvConfig().getApiKeyLocation());
        visitListeners = new ListenerContainer<>();
        proxyLocationListener = new LocationListener(this, visitListeners, bvsdk.getBvWorkerData().getGson());
        placeManager = PlaceManager.getInstance();
        placeManager.addListener(proxyLocationListener);
    }

    public static BVLocationManager getInstance() {
        if (instance == null) {
            instance = new BVLocationManager();
        }

        return instance;
    }

    /**
     * Adds a listener to be called whenever a user enters and exits a retail location belonging to configured client.
     * Can add multiple
     * @param listener Listener to be notified
     * <em>Note:</em> This class keeps a weak reference to the {@link BVLocationListener} instance and will be
     * garbage collected if you do not keep a strong reference to it. }.
     */
    public void addLocationVisitListener(BVLocationListener listener) {
        if (listener == null) {
            return;
        }

        if (!visitListeners.contains(listener)) {
            visitListeners.add(listener);
        }
    }

    /**
     * Removes a listener from being called whenever a user enters and exits a retail location belonging to configured client.
     * @param listener Listener to be removed
     */
    public void removeLocationVisitListener(BVLocationListener listener) {
        if (listener != null) {
            visitListeners.remove(listener);
        }
    }

    /**
     * Starts monitoring users location
     * Must be called before and listeners can be called back
     */
    public void startMonitoringLocation() {
        Gimbal.start();
        placeManager.startMonitoring();
    }

    /**
     * Stops monitoring users location
     */
    public void stopMonitoringLocation() {
        placeManager.stopMonitoring();
        Gimbal.stop();
    }

    public interface BVLocationListener {
        void didBeginVisit(BVVisit visit);
        void didEndVisit(BVVisit visit);
    }

    BVVisit attributesToVisit(Attributes attributes, long dwellTimeMillis) {
        String name = attributes.getValue(PlaceAttribute.Name.getKey());
        String address = attributes.getValue(PlaceAttribute.Address.getKey());
        String city = attributes.getValue(PlaceAttribute.City.getKey());
        String state = attributes.getValue(PlaceAttribute.State.getKey());
        String zip = attributes.getValue(PlaceAttribute.Zip.getKey());
        String storeId = attributes.getValue(PlaceAttribute.StoreId.getKey());

        return new BVVisit(name, address, city, state, zip, storeId, dwellTimeMillis);
    }

    void broadcastIntent(Intent intent) {
        appContext.sendBroadcast(intent);
    }

    public static boolean didStart(Intent intent) {
        return intent.getBooleanExtra(EXTRA_DID_START, true);
    }

    public static BVVisit getBvVisit(Intent intent) {
        String bvVisitStr = intent.getStringExtra(EXTRA_BV_VISIT);
        Gson gson = BVSDK.getInstance().getBvWorkerData().getGson();
        return gson.fromJson(bvVisitStr, BVVisit.class);
    }

    static class LocationListener extends PlaceEventListener {
        private final BVLocationManager bvLocationManager;
        private final ListenerContainer<BVLocationListener> visitListeners;
        private final Gson gson;

        LocationListener(final BVLocationManager bvLocationManager, final ListenerContainer<BVLocationListener> visitListeners, final Gson gson) {
            this.bvLocationManager = bvLocationManager;
            this.visitListeners = visitListeners;
            this.gson = gson;
        }

        @Override
        public void onVisitStart(Visit visit) {
            LocationAnalyticsManager.sendLocationEventForGimbalVisit(visit, ENTRY);
            callbackRegisteredListeners(visit, true);
        }

        @Override
        public void onVisitEnd(Visit visit) {
            LocationAnalyticsManager.sendLocationEventForGimbalVisit(visit, EXIT);
            callbackRegisteredListeners(visit, false);
        }

        private void callbackRegisteredListeners(Visit visit, boolean didStart) {
            Place place = visit.getPlace();
            Attributes attributes = place.getAttributes();
            String type = attributes.getValue(PlaceAttribute.Type.getKey());
            String client = attributes.getValue(PlaceAttribute.ClientId.getKey());

            if (client == null) {
                return;
            }

            if (!client.equalsIgnoreCase(BVSDK.getInstance().getBvUserProvidedData().getBvConfig().getClientId())) {
                return;
            }

            if (type == null || !type.equalsIgnoreCase(PlaceType.Geofence.getValue())) {
                return;
            }

            BVVisit bvVisit = bvLocationManager.attributesToVisit(attributes, visit.getDwellTimeInMillis());

            Intent intent = new Intent(ACTION_GEOFENCE_VISIT);
            String bvVisitStr = gson.toJson(bvVisit);
            intent.putExtra(EXTRA_BV_VISIT, bvVisitStr);
            intent.putExtra(EXTRA_DID_START, didStart);
            bvLocationManager.broadcastIntent(intent);

            for (BVLocationListener locationListener : visitListeners.getListeners()) {
                if (locationListener != null) {
                    if (didStart) {
                        locationListener.didBeginVisit(bvVisit);
                    } else {
                        locationListener.didEndVisit(bvVisit);
                    }
                } else {
                    visitListeners.remove(locationListener);
                }
            }
        }
    }

}
