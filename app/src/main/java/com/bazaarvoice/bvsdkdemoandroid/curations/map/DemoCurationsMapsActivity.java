package com.bazaarvoice.bvsdkdemoandroid.curations.map;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.DemoRouter;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoCurationsMapsActivity extends FragmentActivity implements OnMapReadyCallback, DemoCurationsMapContract.View, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_REQUEST_LOCATION_FINE = 7;
    public static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private static final String TAG = "DemoMapsActivity";

    private DemoCurationsMapContract.UserActionListener mapUserActionListener;

    @BindView(R.id.map_container)
    ViewGroup mapContainer;
    @BindView(R.id.location_description_card)
    CardView locationDescriptionCard;
    @BindView(R.id.productThumbnailImage)
    ImageView image;
    @BindView(R.id.item_text)
    TextView itemText;
    @BindView(R.id.channel)
    TextView channel;

    private GoogleApiClient client;

    @Inject
    DemoMockDataUtil demoMockDataUtil;
    @Inject Picasso picasso;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_curations_maps);
        ButterKnife.bind(this);
        DemoApp.getAppComponent(this).inject(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(map);
//        mapFragment.getMapAsync(this);

        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);

        int detailRowHeight = getResources().getDimensionPixelSize(R.dimen.snippet_prod_image_side);
        boolean haveFineLocPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        mapUserActionListener = new DemoCurationsMapsPresenter(this, demoMockDataUtil, picasso, productId, detailRowHeight, haveFineLocPermission);
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapUserActionListener.loadCurationsFeedItems();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapUserActionListener.onMapReady(googleMap);
    }

    @Override
    public void updateDetailInfo(CurationsFeedItem curationsFeedItem, String thumbnailUrl, String itemText, String channelText) {
        locationDescriptionCard.setTag(curationsFeedItem);

        // update the detail card info
        if (!TextUtils.isEmpty(thumbnailUrl)) {
            picasso.load(thumbnailUrl)
                    .resizeDimen(R.dimen.snippet_prod_image_side, R.dimen.snippet_prod_image_side)
                    .into(image);
        }

        if (!TextUtils.isEmpty(itemText)) {
            this.itemText.setText(itemText);
        }

        if (!TextUtils.isEmpty(channelText)) {
            String formattedChannelText = String.format(getString(R.string.curations_map_subtitle_template), channelText);
            channel.setText(formattedChannelText);
        }
    }

    @OnClick(R.id.location_description_card)
    public void onLocationDetailClicked() {
        CurationsFeedItem curationsFeedItem = (CurationsFeedItem) locationDescriptionCard.getTag();
        String productId = curationsFeedItem != null && curationsFeedItem.getProducts() != null && !curationsFeedItem.getProducts().isEmpty() ? curationsFeedItem.getProducts().get(0).getId() : "";
        String feedItemId = String.valueOf(curationsFeedItem.getId());
        DemoRouter.transitionToCurationsFeedItem(this, productId, feedItemId);
    }

    @Override
    public void showDetailOnScreen() {
        // animate in the card
        ObjectAnimator.ofFloat(locationDescriptionCard, RelativeLayout.Y, getDetailOffScreenY(), getDetailOnScreenY()).start();
    }

    @Override
    public void hideDetailOnScreen() {
        // animate out the card
        ObjectAnimator.ofFloat(locationDescriptionCard, RelativeLayout.Y, getDetailOnScreenY(), getDetailOffScreenY()).start();
    }

    @Override
    public void showLocationPermissionRequest() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION_FINE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION_FINE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapUserActionListener.onUserAcceptedLocationPermission();
                }
                break;
        }
    }

    private int getDetailOnScreenY() {
        int locCardHeightPixels = getResources().getDimensionPixelSize(R.dimen.snippet_prod_image_side);
        int screenHeight = DemoUtils.getScreenDimensions(this).y;
        return screenHeight - locCardHeightPixels - 70;
    }

    private int getDetailOffScreenY() {
        return DemoUtils.getScreenDimensions(this).y;
    }

    @Override
    public void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .show();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(client);
            if (lastKnownLocation != null) {
                mapUserActionListener.onLastKnownLocationUpdated(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Unexpected state for location permission", e);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
