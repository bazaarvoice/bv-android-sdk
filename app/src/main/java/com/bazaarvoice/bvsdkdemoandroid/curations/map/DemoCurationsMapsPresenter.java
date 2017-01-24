/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.curations.map;

import android.util.Log;

import com.bazaarvoice.bvandroidsdk.BVCurations;
import com.bazaarvoice.bvandroidsdk.CurationsFeedCallback;
import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsFeedRequest;
import com.bazaarvoice.bvandroidsdk.CurationsMedia;
import com.bazaarvoice.bvandroidsdk.CurationsPhoto;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DemoCurationsMapsPresenter implements DemoCurationsMapContract.UserActionListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, CurationsFeedCallback {

    private static final LatLng BV_HQ_LAT_LNG = new LatLng(30.3993992, -97.7368935);
    private static final String TAG = DemoCurationsMapsPresenter.class.getSimpleName();

    private DemoCurationsMapContract.View view;
    private DemoDataUtil demoDataUtil;
    private Picasso picasso;
    private List<CurationsFeedItem> curationsFeedItems = new ArrayList<>();
    private DemoMarkerManager markerManager;
    private String productId;
    private boolean haveNewCurationsItems = false;
    private boolean isDetailOnScreen = false;
    private int detailRowHeight;
    private boolean haveFineLocationPermission = false;
    private GoogleMap googleMap;
    private Double lastKnownLatitude, lastKnownLongitude;

    public DemoCurationsMapsPresenter(DemoCurationsMapContract.View view, DemoDataUtil demoDataUtil, Picasso picasso, String productId, int detailRowHeight, boolean haveFineLocationPermission) {
        this.view = view;
        this.demoDataUtil = demoDataUtil;
        this.picasso = picasso;
        this.productId = productId;
        this.detailRowHeight = detailRowHeight;
        this.haveFineLocationPermission = haveFineLocationPermission;
        if (!haveFineLocationPermission) {
            view.showLocationPermissionRequest();
        }
        view.hideDetailOnScreen();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        markerManager = new DemoMarkerManager(googleMap);
        customizeMap(googleMap);
        maybeUpdateMap();
    }

    @Override
    public void loadCurationsFeedItems() {
        if (!curationsFeedItems.isEmpty()) {
            haveNewCurationsItems = true;
            maybeUpdateMap();
            return;
        }

        prefetchImages();

        BVCurations bvCurations = new BVCurations();
        CurationsFeedRequest.Builder builder = new CurationsFeedRequest.Builder(DemoConstants.CURATIONS_GROUPS)
                .limit(20)
                .media(new CurationsMedia("photo", DemoUtils.MAX_IMAGE_WIDTH / 2, DemoUtils.MAX_IMAGE_HEIGHT / 2))
                .externalId(productId)
                .withProductData(true);
        if (lastKnownLatitude != null && lastKnownLongitude != null) {
            builder.location(lastKnownLatitude, lastKnownLongitude);
        }
        CurationsFeedRequest request = builder.build();
        bvCurations.getCurationsFeedItems(request, this);
    }

    @Override
    public void onUserAcceptedLocationPermission() {
        customizeMapWithFineLocationPermission(googleMap);
    }

    @Override
    public void onLastKnownLocationUpdated(double latitude, double longitude) {
        this.lastKnownLatitude = latitude;
        this.lastKnownLongitude = longitude;
        loadCurationsFeedItems();
    }

    private void customizeMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (haveFineLocationPermission) {
            customizeMapWithFineLocationPermission(googleMap);
        }
        googleMap.setPadding(0, 0, 0, detailRowHeight);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(BV_HQ_LAT_LNG));
        // Hide the zoom controls as the button panel will cover it.
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
    }

    private void customizeMapWithFineLocationPermission(GoogleMap googleMap) {
        try {
            googleMap.setLocationSource(null);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException e) {
            Log.e(TAG, "Unexpected state for location permission", e);
        }
    }

    private void updateCurationsFeedItems(List<CurationsFeedItem> curationsFeedItems) {
        this.curationsFeedItems.clear();
        this.curationsFeedItems.addAll(curationsFeedItems);
    }

    private void prefetchImages() {
        for (CurationsFeedItem item : curationsFeedItems) {
            String thumbnailUrl = getThumbnailUrl(item);
            picasso.load(thumbnailUrl).fetch();
        }
    }

    private void maybeUpdateMap() {
        boolean mapReady = markerManager != null;

        if (mapReady && haveNewCurationsItems) {
            updateMap();
            haveNewCurationsItems = false;
        }
    }

    private void updateMap() {
        for (CurationsFeedItem item : curationsFeedItems) {
            markerManager.addMarkerToMap(item);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        CurationsFeedItem curationsFeedItem = markerManager.getItemForMarker(marker);
        if (curationsFeedItem == null) {
            return false;
        }

        view.updateDetailInfo(curationsFeedItem, getThumbnailUrl(curationsFeedItem), curationsFeedItem.getText(), curationsFeedItem.getChannel());
        if (isDetailOnScreen) {
            return false;
        }
        view.showDetailOnScreen();
        isDetailOnScreen = true;
        return false;
    }

    private String getThumbnailUrl(CurationsFeedItem item) {
        List<CurationsPhoto> curationsPhotos = item.getPhotos();
        String thumbnailUrl = (curationsPhotos != null && !curationsPhotos.isEmpty()) ? curationsPhotos.get(0).getImageServiceUrl() + "&width=200&height=200" : "";
        return thumbnailUrl;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (!isDetailOnScreen) {
            return;
        }
        view.hideDetailOnScreen();
        isDetailOnScreen = false;
    }

    @Override
    public void onSuccess(List<CurationsFeedItem> feedItems) {
        haveNewCurationsItems = true;
        updateCurationsFeedItems(feedItems);
        maybeUpdateMap();
    }

    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();
    }
}
