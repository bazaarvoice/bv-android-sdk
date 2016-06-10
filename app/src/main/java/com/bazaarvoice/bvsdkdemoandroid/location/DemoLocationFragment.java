package com.bazaarvoice.bvsdkdemoandroid.location;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bazaarvoice.bvandroidsdk.BVLocationManager;
import com.bazaarvoice.bvandroidsdk.BVVisit;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;


public class DemoLocationFragment extends Fragment implements BVLocationManager.BVLocationListener, BVLocationManager.BVNotificationListener, LocationListener {

    public DemoLocationFragment() {
        // Required empty public constructor
    }

    public static DemoLocationFragment newInstance() {
        DemoLocationFragment fragment = new DemoLocationFragment();

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        readyForDemo();
    }

    private boolean readyForDemo() {
        DemoConfig currentConfig = DemoConfigUtils.getInstance(getContext()).getCurrentConfig();
        String locationKey = currentConfig.apiKeyLocation;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

        BVLocationManager.getInstance().addLocationVisitListener(this);
        BVLocationManager.getInstance().setNotificationListener(this);
        BVLocationManager.getInstance().startMonitoringLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_demo_location, container, false);

        return view;
    }


    @Override
    public void didBeginVisit(BVVisit visit) {


    }

    @Override
    public void didEndVisit(BVVisit visit) {

    }

    @Override
    public void onLocationChanged(Location location) {
        // Updates the location and zoom of the MapView

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

    @Override
    public boolean shouldPresentNotificationForVisit(BVVisit visit) {
        return true;
    }

    @Override
    public Notification.Builder configureNotificationForDisplay(Notification.Builder notificationBuilder, BVVisit visit) {
        notificationBuilder.setContentTitle("Another title");
        return notificationBuilder;
    }
}
