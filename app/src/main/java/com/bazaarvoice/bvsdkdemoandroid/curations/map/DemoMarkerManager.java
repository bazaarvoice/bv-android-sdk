/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.curations.map;

import android.support.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.CurationsCoordinate;
import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class DemoMarkerManager {
    private GoogleMap googleMap;
    private HashMap<Marker, CurationsFeedItem> markerMap = new HashMap<>();

    public DemoMarkerManager(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void addMarkerToMap(CurationsFeedItem item) {
        CurationsCoordinate curationsCoordinate = item.getCoordinate();
        if (curationsCoordinate == null || curationsCoordinate.getLatitude() == null || curationsCoordinate.getLongitude() == null) {
            return;
        }
        LatLng itemLatLng = new LatLng(curationsCoordinate.getLatitude(), curationsCoordinate.getLongitude());
        MarkerOptions markerOptions = generateMarkerOptions(itemLatLng);
        Marker generatedMarker = googleMap.addMarker(markerOptions);
        markerMap.put(generatedMarker, item);
    }

    private MarkerOptions generateMarkerOptions(LatLng latLng) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_bv_camera);
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(bitmapDescriptor)
                .flat(true)
                .position(latLng);

        return markerOptions;
    }

    @Nullable
    public CurationsFeedItem getItemForMarker(Marker marker) {
        return markerMap.get(marker);
    }

}
