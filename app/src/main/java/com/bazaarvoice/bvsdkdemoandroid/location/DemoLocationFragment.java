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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;

public class DemoLocationFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = DemoLocationFragment.class.getSimpleName();
    private static final int REQUEST_LOC = 3;
    private TextView lastLocEventTv;

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
        boolean givenLocPermission = permissions != null && grantResults!= null && permissions.length == 1 && grantResults.length ==1 && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED;
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
        getContext().startService(new Intent(getContext(), DemoLocationService.class));
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
        getContext().unregisterReceiver(demoLocationReceiver);
    }

    private boolean readyForDemo() {
        DemoConfig currentConfig = DemoConfigUtils.getInstance(getContext()).getCurrentConfig();
        String locationKey = currentConfig.apiKeyLocationAndroid;
        String displayName = currentConfig.displayName;

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
        getContext().startService(new Intent(getContext(), DemoLocationService.class));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DemoLocationService.ACTION_VISIT_START);
        intentFilter.addAction(DemoLocationService.ACTION_VISIT_END);
        intentFilter.addAction(DemoLocationService.ACTION_RATE_STORE);
        getContext().registerReceiver(demoLocationReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_demo_location, container, false);
        lastLocEventTv = (TextView) view.findViewById(R.id.lastLocEvent);
        String lastLocEvent = DemoConfigUtils.getInstance(getContext()).getLastLocationEvent();
        updateLastLocEvent(lastLocEvent);
        return view;
    }

    private void updateLastLocEvent(String lastLocEvent) {
        DemoConfigUtils.getInstance(getContext()).putLastLocationEvent(lastLocEvent);
        String formattedLocEvent = String.format(getString(R.string.last_loc_event_template), lastLocEvent);
        Log.d(TAG, "updateLastLocEvent: " + formattedLocEvent);
        lastLocEventTv.setText(formattedLocEvent);
    }

    private BroadcastReceiver demoLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                switch (intent.getAction()) {
                    case DemoLocationService.ACTION_VISIT_START: {
                        String storeId = intent.getStringExtra(DemoLocationService.EXTRA_STORE_ID);
                        updateLastLocEvent("visit start for " + storeId);
                        break;
                    }
                    case DemoLocationService.ACTION_VISIT_END: {
                        String storeId = intent.getStringExtra(DemoLocationService.EXTRA_STORE_ID);
                        updateLastLocEvent("visit end for " + storeId);
                        break;
                    }
                    case DemoLocationService.ACTION_RATE_STORE: {
                        String storeId = intent.getStringExtra(DemoLocationService.EXTRA_STORE_ID);
                        updateLastLocEvent("user tapped rate store for " + storeId);
                        break;
                    }
                }
            }
        }
    };
}
