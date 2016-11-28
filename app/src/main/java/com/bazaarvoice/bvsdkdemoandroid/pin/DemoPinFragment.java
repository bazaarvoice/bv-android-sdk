package com.bazaarvoice.bvsdkdemoandroid.pin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bazaarvoice.bvandroidsdk.BVNotificationService;
import com.bazaarvoice.bvandroidsdk.Pin;
import com.bazaarvoice.bvandroidsdk.PinClient;
import com.bazaarvoice.bvandroidsdk.PinNotificationManager;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DemoPinFragment extends Fragment {

    private static final String TAG = "DemoPinFrag";
    private Unbinder unbinder;
    private static final String PRODUCT_ID = "1-bv";
    private PinClient pinClient;

    public static Fragment newInstance() {
        return new DemoPinFragment();
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pinClient = new PinClient();
        View view = inflater.inflate(R.layout.frag_demo_pin, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerNotificationReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterNotificationReceiver();
    }

    private void registerNotificationReceiver() {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BVNotificationService.ACTION_NOTIFICATION_BUTTON_TAPPED);
        activity.registerReceiver(notificationReceiver, intentFilter);
    }

    private void unregisterNotificationReceiver() {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.unregisterReceiver(notificationReceiver);
    }

    private boolean readyForDemo() {
        DemoConfig currentConfig = DemoConfigUtils.getInstance(getContext()).getCurrentConfig();
        if (currentConfig.isDemoClient()) {
            return true;
        }

        String curationsKey = currentConfig.apiKeyPIN;
        String displayName = currentConfig.displayName;

        if (!DemoConstants.isSet(curationsKey)) {
            String errorMessage = String.format(getString(R.string.view_demo_error_message), displayName, getString(R.string.demo_pin));
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(errorMessage);
            builder.setNegativeButton("Ok", null).create().show();
            return false;
        }

        return true;
    }

    @OnClick(R.id.queuePinNotifButton)
    public void onQueuePinNotificationTapped() {
        if (readyForDemo()) {
            PinNotificationManager.getInstance().scheduleNotification(PRODUCT_ID);
        }
    }

    @OnClick(R.id.getPins)
    public void onGetPinsTapped() {
        if (readyForDemo()) {
            pinClient.getPendingPins(new PinClient.PinsCallback() {
                @Override
                public void onSuccess(List<Pin> pins) {
                    StringBuilder message = new StringBuilder();
                    for (Pin pin : pins) {
                        message.append(pin.getDisplayName()).append("\n");
                    }
                    new AlertDialog.Builder(getContext())
                            .setTitle("Products To Review")
                            .setMessage(message.toString())
                            .setPositiveButton(getString(R.string.okay), null)
                            .show();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.e(TAG, "Failed to get pins", throwable);
                }
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BVNotificationService.ACTION_NOTIFICATION_BUTTON_TAPPED: {
                    String featureName = BVNotificationService.getFeatureName(intent);
                    switch (featureName) {
                        case PinNotificationManager.FEATURE_NAME: {
                            String productId = BVNotificationService.getNotificationCgcId(intent);
                            int buttonTapped = BVNotificationService.getButtonTapped(intent);
                            switch (buttonTapped) {
                                case BVNotificationService.POSITIVE: {
                                    showSnackbar("Tapped Review for " + productId);
                                    break;
                                }
                                case BVNotificationService.NEUTRAL: {
                                    showSnackbar("Tapped Later for " + productId);
                                    break;
                                }
                                case BVNotificationService.NEGATIVE: {
                                    showSnackbar("Tapped Dismiss for " + productId);
                                    break;
                                }
                            }
                            break;
                        }
                    }
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
