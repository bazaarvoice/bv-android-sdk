/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.bazaarvoice.bvandroidsdk.internal.ListenerContainer;
import com.gimbal.android.Attributes;
import com.gimbal.android.Gimbal;
import com.gimbal.android.Place;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Visit;

/**
 * Class used to monitor users location and provide information about users retail location visits
 */
public class BVLocationManager {
    private static final String TAG = BVLocationManager.class.getSimpleName();
    private static BVLocationManager instance;
    private final ListenerContainer<BVLocationListener> visitListeners;
    private PlaceManager placeManager;
    private final ListenerContainer<BVRateStoreListener> rateStoreListeners;

    private final LocationListener proxyLocationListener;

    private BVLocationManager() {
        BVSDK bvsdk = BVSDK.getInstance();
        Gimbal.setApiKey(bvsdk.getApplication(), bvsdk.getApiKeys().getApiKeyLocations());
        BVNotificationManager bvNotificationManager = BVNotificationManager.getInstance();
        visitListeners = new ListenerContainer<>();
        rateStoreListeners = new ListenerContainer<>();
        proxyLocationListener = new LocationListener(this, visitListeners, bvNotificationManager);
        placeManager = PlaceManager.getInstance();
        placeManager.addListener(proxyLocationListener);
        Gimbal.registerForPush("205299337611");
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

    public interface BVRateStoreListener {
        void onRateStoreClicked(String storeId);
    }

    public void addRateStoreListener(BVRateStoreListener rateStoreListener) {
        this.rateStoreListeners.add(rateStoreListener);
    }

    public void removeRateStoreListener(BVRateStoreListener rateStoreListener) {
        this.rateStoreListeners.remove(rateStoreListener);
    }

    public ListenerContainer<BVRateStoreListener> getRateStoreListeners() {
        return rateStoreListeners;
    }

    private BVVisit attributesToVisit(Attributes attributes) {
        String name = attributes.getValue(PlaceAttribute.Name.getKey());
        String address = attributes.getValue(PlaceAttribute.Address.getKey());
        String city = attributes.getValue(PlaceAttribute.City.getKey());
        String state = attributes.getValue(PlaceAttribute.State.getKey());
        String zip = attributes.getValue(PlaceAttribute.Zip.getKey());
        String storeId = attributes.getValue(PlaceAttribute.StoreId.getKey());

        return new BVVisit(name, address, city, state, zip, storeId);
    }

    static class LocationListener extends PlaceEventListener {
        private final BVLocationManager bvLocationManager;
        private final ListenerContainer<BVLocationListener> visitListeners;
        private final BVNotificationManager bvNotificationManager;

        public LocationListener(final BVLocationManager bvLocationManager, final ListenerContainer<BVLocationListener> visitListeners, final BVNotificationManager bvNotificationManager) {
            this.bvLocationManager = bvLocationManager;
            this.visitListeners = visitListeners;
            this.bvNotificationManager = bvNotificationManager;
        }

        @Override
        public void onVisitStart(Visit visit) {
            LocationAnalyticsManager.sendLocationEventForGimbalVisit(visit, VisitLocationSchema.TransitionState.Entry);
            callbackRegisteredListeners(visit, true);
        }

        @Override
        public void onVisitEnd(Visit visit) {
            LocationAnalyticsManager.sendLocationEventForGimbalVisit(visit, VisitLocationSchema.TransitionState.Exit);
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

            if (!client.equalsIgnoreCase(BVSDK.getInstance().getClientId())) {
                return;
            }

            if (type == null || !type.equalsIgnoreCase(PlaceType.Geofence.getValue())) {
                return;
            }

            BVVisit bvVisit = bvLocationManager.attributesToVisit(attributes);
            bvNotificationManager.scheduleNotification(bvVisit.getStoreId(), visit.getDwellTimeInMillis());

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
