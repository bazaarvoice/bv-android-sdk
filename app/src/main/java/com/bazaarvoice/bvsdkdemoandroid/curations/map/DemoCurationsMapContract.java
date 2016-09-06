/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.curations.map;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.google.android.gms.maps.GoogleMap;

public interface DemoCurationsMapContract {

    interface View {
        void updateDetailInfo(CurationsFeedItem curationsFeedItem, String thumbnailUrl, String review, String channel);

        /**
         * Animates the detail card onto the screen.
         * Modifies the y value, so do not call more than once in a row.
         */
        void showDetailOnScreen();

        /**
         * Animates the detail card off of the screen.
         * Modifies the y value, so do not call more than once in a row.
         */
        void hideDetailOnScreen();

        void showDialog(String title, String message);

        void showLocationPermissionRequest();
    }

    interface UserActionListener {
        void onMapReady(GoogleMap googleMap);
        void loadCurationsFeedItems();
        void onUserAcceptedLocationPermission();
        void onLastKnownLocationUpdated(double latitude, double longitude);
    }

}
