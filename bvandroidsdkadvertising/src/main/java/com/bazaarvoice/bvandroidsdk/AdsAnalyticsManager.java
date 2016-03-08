/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper around the common AnalyticsManager for Ads specific Analytics
 */
class AdsAnalyticsManager {
    private AnalyticsManager analyticsManager;
    private String clientId;

    public AdsAnalyticsManager(AnalyticsManager analyticsManager, String clientId) {
        this.analyticsManager = analyticsManager;
        this.clientId = clientId;
    }

    /**
     * For setting an event's type before it gets sent
     */
    enum EventType {
        BeaconLocation("Location", "Beacon"),
        GeoLocation("Location", "Geo"),
        GeofenceLocation("Location", "Geofence"),
        GimbalSighting("Location", "GimbalSighting"),
        GimbalVisit("Location", "GimbalVisit");

        private final String eventClass;
        private final String eventType;

        EventType(String eventClass, String eventType) {
            this.eventClass = eventClass;
            this.eventType = eventType;
        }
    }

    /**
     * Builds event with initial parameters
     * @param type
     * @return
     */
    Map<String, Object> buildEventOfType(EventType type){
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("cl", type.eventClass);
        eventData.put("type", type.eventType);
        return eventData;
    }


    /**
     * Constructs and sends a personalization event
     */
    void onPersonalizationEvent(){
        analyticsManager.sendPersonalizationEvent();
    }

    void onAdRequested(AdTracker adTracker, BVAuthenticatedUser bvUser) {
        adEvent(adTracker, AdState.REQUESTED, bvUser);
    }

    /**
     * Constructs and sends event data for when an Ad loads
     * @param adTracker
     * @param bvUser
     */
    void onAdLoaded(AdTracker adTracker, BVAuthenticatedUser bvUser) {
        adEvent(adTracker, AdState.RECEIVED, bvUser);
    }

    /**
     * Constructs and sends event data for when an Ad is opened
     * @param adTracker
     * @param bvUser
     */
    void onAdOpened(AdTracker adTracker, BVAuthenticatedUser bvUser) {
        adEvent(adTracker, AdState.SHOWN, bvUser);
    }

    /**
     * Constructs and sends event data for when user leaves app and Ad is showing
     * @param adTracker
     * @param bvUser
     */
    void onAdLeftApplication(AdTracker adTracker, BVAuthenticatedUser bvUser) {
        adEvent(adTracker, AdState.CONVERSION, bvUser);
    }

    /**
     * Contructs and sends event data for when an Ad fails to load
     * @param adTracker
     * @param errorCode
     * @param bvUser
     */
    void onAdFailedToLoad(AdTracker adTracker, int errorCode, BVAuthenticatedUser bvUser) {
        adEvent(adTracker, AdState.FAILED, bvUser, errorCode);
    }

    /**
     * Constructs and sends event data for when an Ad is closed
     * @param adTracker
     * @param bvUser
     */
    void onAdClosed(AdTracker adTracker, BVAuthenticatedUser bvUser) {
        adEvent(adTracker, AdState.DISMISSED, bvUser);
    }

    private AdLifecyclePartialSchema getAdLifecyclePartialSchema(AdTracker adTracker, AdState adState, BVAuthenticatedUser bvUser) {
        ShopperProfile shopperProfile = bvUser.getShopperProfile();
        String flattenedBrandsValue = (shopperProfile == null || shopperProfile.getProfile() == null) ? "" : shopperProfile.getProfile().getFlattenedBrands();
        String flattenedInterestsValue = (shopperProfile == null || shopperProfile.getProfile() == null) ? "" : shopperProfile.getProfile().getFlattenedInterests();

        AdLifecyclePartialSchema.Builder builder = new AdLifecyclePartialSchema.Builder(adTracker.getAdUnitId(), adTracker.getAdType(), adState.getValue())
                .loadId(adTracker.getLoadId());

        if (flattenedBrandsValue != null && !flattenedBrandsValue.isEmpty()) {
            builder.brands(flattenedBrandsValue);
        }

        if (flattenedInterestsValue != null && !flattenedInterestsValue.isEmpty()) {
            builder.interests(flattenedInterestsValue);
        }

        AdLifecyclePartialSchema schema = builder.build();

        return schema;
    }

    public enum AdState {
        REQUESTED("requested"), RECEIVED("received"), SHOWN("shown"),
        DISMISSED("dismissed"), CONVERSION("conversion"), FAILED("failed");

        private String value;

        AdState(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private void adEvent(AdTracker adTracker, AdState adState, BVAuthenticatedUser bvUser) {
        adEvent(adTracker, adState, bvUser, -1);
    }

    /**
     * Adds data about an Ad lifecycle event and then sends said event
     * @param adTracker
     * @param adState
     * @param bvUser
     */
    private void adEvent(AdTracker adTracker, AdState adState, BVAuthenticatedUser bvUser, int errorCode) {
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        AdLifecyclePartialSchema adLifecyclePartialSchema = getAdLifecyclePartialSchema(adTracker, adState, bvUser);
        MobileAdLifecycleSchema.Builder builder = new MobileAdLifecycleSchema.Builder(magpieMobileAppPartialSchema, adLifecyclePartialSchema);

        if (adState == AdState.FAILED) {
            builder.errorMessage("Ad load failed with code: " + errorCode);
        }

        MobileAdLifecycleSchema schema = builder.build();

        analyticsManager.enqueueEvent(schema.getDataMap());
    }

    /**
     * Adds location data to an event
     * @param eventData
     * @param interestTier1
     * @param interestTier2
     */
    private void addLocationData(Map<String, Object> eventData, String interestTier1, String interestTier2) {
        eventData.put("tier1", interestTier1);
        eventData.put("tier2", interestTier2);
    }

    /**
     * Adds gimbal visit data to an event
     * @param eventData
     * @param arrivalTimeInMillis
     * @param departureTimeInMillis
     * @param identifier
     * @param name
     */
    private void addGimbalVisitData(Map<String, Object> eventData, long arrivalTimeInMillis, long departureTimeInMillis, String identifier, String name) {
        eventData.put("arrivalDate", arrivalTimeInMillis);
        eventData.put("departureDate", departureTimeInMillis);
        eventData.put("identifier", identifier);
        eventData.put("name", name);
    }

    /**
     * Adds gimbal sighting data to an event
     * @param eventData
     * @param rssi
     * @param timeInMillis
     * @param beaconIdentifier
     * @param beaconName
     */
    private void addGimbalSightingData(Map<String, Object> eventData, Integer rssi, long timeInMillis, String beaconIdentifier, String beaconName) {
        eventData.put("rssi", rssi);
        eventData.put("date", timeInMillis);
        eventData.put("identifier", beaconIdentifier);
        eventData.put("name", beaconName);
    }

    /**
     * Adds data from gimbal beacon when a beacon visit begins
     * Sends event once data is added
     * @param arrivalTimeInMillis
     * @param departureTimeInMillis
     * @param identifier
     * @param name
     * @param interestTier1
     * @param interestTier2
     */
    void gimbalOnVisitStart(long arrivalTimeInMillis, long departureTimeInMillis, String identifier, String name, String interestTier1, String interestTier2) {
        Map<String, Object> eventData = buildEventOfType(EventType.GimbalVisit);

        eventData.put("begin", true);
        eventData.put("end", false);
        analyticsManager.addPersonalizationData(eventData);
        addLocationData(eventData, interestTier1, interestTier2);
        addGimbalVisitData(eventData, arrivalTimeInMillis, departureTimeInMillis, identifier, name);

        analyticsManager.enqueueEvent(eventData);
    }

    /**
     * Adds data from gimbal beacon when a beacon visit ends
     * Sends event once data is added
     * @param arrivalTimeInMillis
     * @param departureTimeInMillis
     * @param identifier
     * @param name
     * @param interestTier1
     * @param interestTier2
     */
    void gimbalOnVisitEnd(long arrivalTimeInMillis, long departureTimeInMillis, String identifier, String name, String interestTier1, String interestTier2) {
        Map<String, Object> eventData = buildEventOfType(EventType.GimbalVisit);

        eventData.put("begin", false);
        eventData.put("end", true);
        analyticsManager.addPersonalizationData(eventData);
        addLocationData(eventData, interestTier1, interestTier2);
        addGimbalVisitData(eventData, arrivalTimeInMillis, departureTimeInMillis, identifier, name);

        analyticsManager.enqueueEvent(eventData);
    }

    /**
     * Adds gimbal visit data when a beacon is sighted and visited
     * Sends event once data is added
     * @param rssi
     * @param timeInMillis
     * @param beaconIdentifier
     * @param beaconName
     * @param arrivalTimeInMillis
     * @param departureTimeInMillis
     * @param visitIdentifier
     * @param visitName
     * @param interestTier1
     * @param interestTier2
     */
    void gimbalOnBeaconSightingForVisit(Integer rssi, long timeInMillis, String beaconIdentifier, String beaconName, long arrivalTimeInMillis, long departureTimeInMillis, String visitIdentifier, String visitName, String interestTier1, String interestTier2) {
        Map<String, Object> eventData = buildEventOfType(EventType.GimbalSighting);

        analyticsManager.addPersonalizationData(eventData);
        addLocationData(eventData, interestTier1, interestTier2);
        addGimbalVisitData(eventData, arrivalTimeInMillis, departureTimeInMillis, visitIdentifier, visitName);
        addGimbalSightingData(eventData, rssi, timeInMillis, beaconIdentifier, beaconName);

        analyticsManager.enqueueEvent(eventData);
    }

    /**
     * Adds data to event when a beacon is sighted
     * Sends event once data is added
     * @param rssi
     * @param timeInMillis
     * @param beaconIdentifier
     * @param beaconName
     * @param interestTier1
     * @param interestTier2
     */
    void gimbalOnBeaconSighting(Integer rssi, long timeInMillis, String beaconIdentifier, String beaconName, String interestTier1, String interestTier2) {
        Map<String, Object> eventData = buildEventOfType(EventType.GimbalSighting);

        analyticsManager.addPersonalizationData(eventData);
        addLocationData(eventData, interestTier1, interestTier2);
        addGimbalSightingData(eventData, rssi, timeInMillis, beaconIdentifier, beaconName);


        analyticsManager.enqueueEvent(eventData);
    }

    /**
     * Adds beacon data to event
     * @param eventData
     * @param proximityUUID
     * @param name
     * @param major
     * @param minor
     * @param rssi
     */
    void addBeaconData(Map<String, Object> eventData, String proximityUUID, String name, Integer major, Integer minor, Integer rssi) {
        eventData.put("uuid", proximityUUID);
        eventData.put("name", name);
        eventData.put("major", major);
        eventData.put("minor", minor);
        eventData.put("rssi", rssi);
    }

    /**
     * Adds beacon data to event when a region is entered
     * Sends event once data is added
     * @param proximityUUID
     * @param name
     * @param major
     * @param minor
     * @param rssi
     * @param interestTier1
     * @param interestTier2
     */
    void beaconOnEnterRegion(String proximityUUID, String name, Integer major, Integer minor, Integer rssi, String interestTier1, String interestTier2){
        Map<String, Object> eventData = buildEventOfType(EventType.BeaconLocation);

        analyticsManager.addPersonalizationData(eventData);
        eventData.put("enter", true);
        eventData.put("exit", false);
        addBeaconData(eventData, proximityUUID, name, major, minor, rssi);
        addLocationData(eventData, interestTier1, interestTier2);

        analyticsManager.enqueueEvent(eventData);
    }

    /**
     * Adds beacon data to event when a region is left
     * Sends event once data is added
     * @param proximityUUID
     * @param name
     * @param major
     * @param minor
     * @param rssi
     * @param interestTier1
     * @param interestTier2
     */
    void beaconOnExitRegion(String proximityUUID, String name, Integer major, Integer minor, Integer rssi, String interestTier1, String interestTier2){
        Map<String, Object> eventData = buildEventOfType(EventType.BeaconLocation);

        analyticsManager.addPersonalizationData(eventData);
        eventData.put("enter", false);
        eventData.put("exit", true);
        addBeaconData(eventData, proximityUUID, name, major, minor, rssi);
        addLocationData(eventData, interestTier1, interestTier2);

        analyticsManager.enqueueEvent(eventData);
    }

    /**
     * Adds beacon data when a beacon is in range
     * Sends event once data is added
     * @param proximityUUID
     * @param name
     * @param major
     * @param minor
     * @param rssi
     * @param interestTier1
     * @param interestTier2
     */
    void beaconOnRanging(String proximityUUID, String name, Integer major, Integer minor, Integer rssi, String interestTier1, String interestTier2){
        Map<String, Object> eventData = buildEventOfType(EventType.BeaconLocation);

        analyticsManager.addPersonalizationData(eventData);
        eventData.put("ranging", true);
        addBeaconData(eventData, proximityUUID, name, major, minor, rssi);
        addLocationData(eventData, interestTier1, interestTier2);

        analyticsManager.enqueueEvent(eventData);
    }

    /**
     * Adds geo location data to an event
     * @param eventData
     * @param latitude
     * @param longitude
     * @param accuracy
     */
    void addGeoLocationData(Map<String, Object> eventData, double latitude, double longitude, double accuracy) {
        eventData.put("latitude", latitude);
        eventData.put("longitude", longitude);
        eventData.put("accuracy", accuracy);
    }

    /**
     * Adds data to event when the location changes
     * Sends event once data is added
     * @param latitude
     * @param longitude
     * @param accuracy
     * @param interestTier1
     * @param interestTier2
     */
    void locationChanged(double latitude, double longitude, double accuracy, String interestTier1, String interestTier2) {
        Map<String, Object> eventData = buildEventOfType(EventType.GeoLocation);

        analyticsManager.addPersonalizationData(eventData);
        addGeoLocationData(eventData, latitude, longitude, accuracy);
        addLocationData(eventData, interestTier1, interestTier2);

        analyticsManager.enqueueEvent(eventData);
    }

    /**
     * Adds geofence data to event
     * @param eventData
     * @param centerLatitude
     * @param centerLongitude
     * @param accuracy
     */
    void addGeoFenceData(Map<String, Object> eventData, double centerLatitude, double centerLongitude, double accuracy) {
        eventData.put("centerLatitude", centerLatitude);
        eventData.put("centerLongitude", centerLongitude);
        eventData.put("accuracy", accuracy);
    }

    /**
     * Adds data to event when a geofence is entered
     * Sends event once data is added
     * @param centerLatitude
     * @param centerLongitude
     * @param accuracy
     * @param interestTier1
     * @param interestTier2
     */
    void locationDidEnterGeofence(double centerLatitude, double centerLongitude, double accuracy, String interestTier1, String interestTier2) {
        Map<String, Object> eventData = buildEventOfType(EventType.GeofenceLocation);

        analyticsManager.addPersonalizationData(eventData);
        eventData.put("enter", true);
        eventData.put("exit", false);
        addGeoFenceData(eventData, centerLatitude, centerLongitude, accuracy);
        addLocationData(eventData, interestTier1, interestTier2);

        analyticsManager.enqueueEvent(eventData);
    }

    /**
     * Adds data to event when a geofence is left
     * Sends event once data is added
     * @param centerLatitude
     * @param centerLongitude
     * @param accuracy
     * @param interestTier1
     * @param interestTier2
     */
    void locationDidExitGeofence(double centerLatitude, double centerLongitude, double accuracy, String interestTier1, String interestTier2) {
        Map<String, Object> eventData = buildEventOfType(EventType.GeofenceLocation);

        analyticsManager.addPersonalizationData(eventData);
        eventData.put("enter", false);
        eventData.put("exit", true);
        addGeoFenceData(eventData, centerLatitude, centerLongitude, accuracy);
        addLocationData(eventData, interestTier1, interestTier2);

        analyticsManager.enqueueEvent(eventData);
    }

}
