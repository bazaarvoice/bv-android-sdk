package com.bazaarvoice.bvsdkdemoandroid.location;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVLocationManager;
import com.bazaarvoice.bvandroidsdk.BVNotificationService;
import com.bazaarvoice.bvandroidsdk.BVVisit;
import com.bazaarvoice.bvandroidsdk.StoreNotificationManager;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.pin.DemoRateActivity;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;

import javax.inject.Inject;

public class DemoLocationFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = DemoLocationFragment.class.getSimpleName();
    private static final int REQUEST_LOC = 3;
    private TextView lastLocEventTv;

    @Inject DemoClient demoClient;
    @Inject DemoClientConfigUtils demoClientConfigUtils;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean givenLocPermission = permissions.length == 1 && grantResults.length == 1 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (givenLocPermission) {
            startLocationUpdates();
        }
    }

    private LocationManager locationManager;

    public static DemoLocationFragment newInstance() {
        return new DemoLocationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoApp.getAppComponent(getContext()).inject(this);
        locationManager = (LocationManager) getContext().getSystemService(Service.LOCATION_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        tryStartLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        tryRemoveLocationUpdates();
    }

    private void tryStartLocationUpdates() {
        if (isLocationEnabled()) {
            startLocationUpdates();
        } else {
            requestLocationPermission();
        }
    }

    private void tryRemoveLocationUpdates() {
        if (isLocationEnabled()) {
            locationManager.removeUpdates(locationListener);
        }
    }

    private boolean isLocationEnabled() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOC);
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private void startLocationUpdates() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        readyForDemo();
        startGeofenceService();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopGeofenceService();
    }

    private boolean readyForDemo() {
        String locationKey = demoClient.getApiKeyLocationAndroid();
        String displayName = demoClient.getDisplayName();

        if (!DemoConstants.isSet(locationKey)) {
            String errorMessage = String.format(getString(R.string.view_demo_error_message), displayName, getString(R.string.demo_location));
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(errorMessage);
            builder.setNegativeButton("OK", null).create().show();
            return false;
        }

        return true;
    }

    private void startGeofenceService() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BVLocationManager.ACTION_GEOFENCE_VISIT);
        intentFilter.addAction(BVNotificationService.ACTION_NOTIFICATION_BUTTON_TAPPED);
        getContext().registerReceiver(demoLocationReceiver, intentFilter);
        BVLocationManager.getInstance().startMonitoringLocation();
    }

    private void stopGeofenceService() {
        BVLocationManager.getInstance().stopMonitoringLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_demo_location, container, false);
        lastLocEventTv = (TextView) view.findViewById(R.id.lastLocEvent);
        String lastLocEvent = demoClientConfigUtils.getLastLocationEvent();
        updateLastLocEvent(lastLocEvent);
        return view;
    }

    private void updateLastLocEvent(String lastLocEvent) {
        demoClientConfigUtils.putLastLocationEvent(lastLocEvent);
        String formattedLocEvent = String.format(getString(R.string.last_loc_event_template), lastLocEvent);
        Log.d(TAG, "updateLastLocEvent: " + formattedLocEvent);
        lastLocEventTv.setText(formattedLocEvent);
    }

    private BroadcastReceiver demoLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (intentAction != null) {
                switch (intentAction) {
                    case BVLocationManager.ACTION_GEOFENCE_VISIT: {
                        onGeofenceEvent(intent);
                        break;
                    }
                    case BVNotificationService.ACTION_NOTIFICATION_BUTTON_TAPPED: {
                        onNotificationButtonTappedEvent(intent);
                        break;
                    }
                }
            }
        }

        private void onGeofenceEvent(Intent intent) {
            boolean didStart = BVLocationManager.didStart(intent);
            BVVisit bvVisit = BVLocationManager.getBvVisit(intent);
            if (didStart) {
                updateLastLocEvent("visit start for " + bvVisit.getStoreId());
            } else {
                updateLastLocEvent("visit end for " + bvVisit.getStoreId());
            }
        }

        private void onNotificationButtonTappedEvent(Intent intent) {
            String featureName = BVNotificationService.getFeatureName(intent);
            switch (featureName) {
                case StoreNotificationManager.FEATURE_NAME: {
                    String storeId = BVNotificationService.getNotificationCgcId(intent);
                    int buttonTapped = BVNotificationService.getButtonTapped(intent);
                    switch (buttonTapped) {
                        case BVNotificationService.POSITIVE: {
                            showSnackbar("Tapped Review for " + storeId);
                            if (getActivity() != null && !getActivity().isFinishing()) {
                                DemoRateActivity.transitionTo(getActivity());
                            }
                            break;
                        }
                        case BVNotificationService.NEUTRAL: {
                            showSnackbar("Tapped Later for " + storeId);
                            break;
                        }
                        case BVNotificationService.NEGATIVE: {
                            showSnackbar("Tapped Dismiss for " + storeId);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    };

    private void showSnackbar(String message) {
        View view = getView();
        if (view == null || getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.okay), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

}
