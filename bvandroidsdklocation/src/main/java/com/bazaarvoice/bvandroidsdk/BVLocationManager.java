/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.app.Notification;

import com.gimbal.android.Attributes;
import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.Gimbal;
import com.gimbal.android.Place;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Visit;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class used to monitor users location and provide information about users retail location visits
 */
public class BVLocationManager {

    private static BVLocationManager instance;
    private final List<ListenerContainer> visitListeners = new ArrayList<>();
    private PlaceManager placeManager;
    private CommunicationManager communicationManager;
    private BVNotificationListener notificationListener;

    LocationListener proxyLocationListener = new LocationListener();
    BVCommunicationListener proxyCommunicationListener = new BVCommunicationListener();

    private BVLocationManager() {

        Gimbal.setApiKey(BVSDK.getInstance().getApplication(), BVSDK.getInstance().getApiKeyLocation());
        placeManager = PlaceManager.getInstance();
        placeManager.addListener(proxyLocationListener);
        Gimbal.registerForPush("205299337611");
        communicationManager = CommunicationManager.getInstance();
        communicationManager.addListener(proxyCommunicationListener);
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

        if (getContainerForCallback(listener) == null) {
            ListenerContainer container = new ListenerContainer(listener);
            visitListeners.add(container);
        }
    }

    private ListenerContainer getContainerForCallback(BVLocationListener callback) {

        for (ListenerContainer container : visitListeners) {
            if (container.callbackWeakReference == callback) {
                return container;
            }
        }

        return null;
    }

    /**
     * Removes a listener from being called whenever a user enters and exits a retail location belonging to configured client.
     * @param listener Listener to be removed
     */
    public void removeLocationVisitListener(BVLocationListener listener) {
        ListenerContainer container = getContainerForCallback(listener);
        if (container != null) {
            visitListeners.remove(container);
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
    }

    public interface BVLocationListener {
        void didBeginVisit(BVVisit visit);
        void didEndVisit(BVVisit visit);
    }


    public interface BVNotificationListener {
        boolean shouldPresentNotificationForVisit(BVVisit visit);
        Notification.Builder configureNotificationForDisplay(Notification.Builder notificationBuilder, BVVisit visit);
    }


    private class ListenerContainer {

        private final WeakReference<BVLocationListener> callbackWeakReference;

        ListenerContainer(BVLocationListener callback) {
            this.callbackWeakReference = new WeakReference(callback);
        }
    }

    public void setNotificationListener(BVNotificationListener listener) {
        this.notificationListener = listener;
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

    class LocationListener extends PlaceEventListener {

        @Override
        public void onVisitStart(Visit visit) {

            if (gimbalPlaceIsValid(visit.getPlace())) {
                LocationAnalyticsManager.sendLocationEventForGimbalVisit(visit);
            }

            callbackRegisteredListeners(visit.getPlace(), true);
        }

        @Override
        public void onVisitEnd(Visit visit) {
            if (gimbalPlaceIsValid(visit.getPlace())) {
                LocationAnalyticsManager.sendLocationEventForGimbalVisit(visit);
            }

            callbackRegisteredListeners(visit.getPlace(), false);
        }

        private void callbackRegisteredListeners(Place place, boolean didStart) {
            if (visitListeners.size() == 0) {
                return;
            }
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



            BVVisit visit = attributesToVisit(attributes);

            List<ListenerContainer> containers = visitListeners;
            for (ListenerContainer container : containers) {

                if (container.callbackWeakReference.get() != null) {
                    if (didStart) {
                        container.callbackWeakReference.get().didBeginVisit(visit);
                    }else {
                        container.callbackWeakReference.get().didEndVisit(visit);
                    }

                }else {
                    visitListeners.remove(container);
                }
            }
        }

        private boolean gimbalPlaceIsValid(Place place) {
            return place.getAttributes().getValue(PlaceAttribute.Id.getKey()) != null;
        }
    }

    class BVCommunicationListener extends CommunicationListener {

        public Collection<Communication> presentNotificationForCommunications(Collection<Communication> communications, Visit visit) {

            List<Communication> filteredCommunications = new ArrayList<>();

            Attributes attributes = visit.getPlace().getAttributes();
            String type = attributes.getValue(PlaceAttribute.Type.getKey());

            String client = attributes.getValue(PlaceAttribute.ClientId.getKey());
            if (client == null || !client.equalsIgnoreCase(BVSDK.getInstance().getClientId())) {
                return null;
            }

            if (type == null || !type.equalsIgnoreCase(PlaceType.Geofence.getValue())) {
                return null;
            }

            BVVisit bvVisit = attributesToVisit(visit.getPlace().getAttributes());
            for (Communication communication : communications) {
                if (notificationListener != null && notificationListener.shouldPresentNotificationForVisit(bvVisit)) {
                    filteredCommunications.add(communication);
                }
            }

            return communications;
        }

        public Notification.Builder prepareCommunicationForDisplay(Communication communication, Visit visit, int notificationId) {

            if (notificationListener != null) {
                Notification.Builder builder = new Notification.Builder(BVSDK.getInstance().getApplicationContext());
                builder.setContentTitle(communication.getTitle());
                builder.setContentText(communication.getDescription());
                builder.setSmallIcon(R.drawable.ic_stat_action_room);

                BVVisit bvVisit = attributesToVisit(visit.getPlace().getAttributes());
                return notificationListener.configureNotificationForDisplay(builder, bvVisit);
            }

            return null;
        }
    }
}
