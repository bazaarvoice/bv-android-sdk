package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class PinClient {
    private static final String TAG = "PinClient";
    private static final int MSG_DISPATCH_PIN_REQUEST = 1;
    private static final int MSG_COMPLETE_PIN_REQUEST = 1;
    private PinBgHandler pinBgHandler;
    private PinMainHandler pinMainHandler;

    public PinClient() {
        BVSDK bvsdk = BVSDK.getInstance();
        pinBgHandler = new PinBgHandler(bvsdk.getBackgroundLooper(), this);
        pinMainHandler = new PinMainHandler(this);
    }

    public interface PinsCallback {
        void onSuccess(List<Pin> pins);
        void onFailure(Throwable throwable);
    }

    public void getPendingPins(PinsCallback callback) {
        WeakReference<PinsCallback> pinCbContainer = new WeakReference<>(callback);
        dispatchPinRequest(pinCbContainer);
    }

    private static final class ResponseData {
        private List<Pin> pins;
        private Throwable throwable;
        private WeakReference<PinsCallback> pinCbContainer;

        ResponseData(@Nullable List<Pin> pins, @Nullable Throwable throwable, @NonNull WeakReference<PinsCallback> pinCbContainer) {
            this.pins = pins;
            this.throwable = throwable;
            this.pinCbContainer = pinCbContainer;
        }

        @Nullable
        List<Pin> getPins() {
            return pins;
        }

        @Nullable
        Throwable getThrowable() {
            return throwable;
        }

        @NonNull
        WeakReference<PinsCallback> getPinCbContainer() {
            return pinCbContainer;
        }
    }

    private void dispatchPinRequest(WeakReference<PinsCallback> pinCbContainer) {
        pinBgHandler.sendMessage(pinBgHandler.obtainMessage(MSG_DISPATCH_PIN_REQUEST, pinCbContainer));
    }

    private void completePinRequest(ResponseData responseData) {
        pinMainHandler.sendMessage(pinMainHandler.obtainMessage(MSG_COMPLETE_PIN_REQUEST, responseData));
    }

    private static final class PinBgHandler extends Handler {
        private final PinClient pinClient;

        PinBgHandler(Looper looper, PinClient pinClient) {
            super(looper);
            this.pinClient = pinClient;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DISPATCH_PIN_REQUEST: {
                    WeakReference<PinsCallback> pinCbContainer = (WeakReference<PinsCallback>) msg.obj;
                    ResponseData responseData = pinClient.getPinResponseData(pinCbContainer);
                    pinClient.completePinRequest(responseData);
                    break;
                }
            }
        }
    }

    private static final class PinMainHandler extends Handler {
        private final PinClient pinClient;
        PinMainHandler(PinClient pinClient) {
            super(Looper.getMainLooper());
            this.pinClient = pinClient;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_COMPLETE_PIN_REQUEST: {
                    ResponseData responseData = (ResponseData) msg.obj;
                    pinClient.sendPinResponseData(responseData);
                    break;
                }
            }
        }
    }

    @WorkerThread
    private ResponseData getPinResponseData(WeakReference<PinsCallback> pinCbContainer) {
        List<Pin> pendingPins = null;
        Throwable throwable = null;

        BVSDK bvsdk = BVSDK.getInstance();
        Context appContext = bvsdk.getApplicationContext();
        OkHttpClient okHttpClient = bvsdk.getOkHttpClient();
        Gson gson = bvsdk.getGson();
        String bvUserAgent = bvsdk.getBvsdkUserAgent();

        AdIdResult adIdResult = AdIdRequestTask.getAdId(appContext);
        String adId = adIdResult.getAdId();
        if (adIdResult.isNonTracking()) {
            // Not going to have any pin stuff, return now
            pendingPins = new ArrayList<>();
        } else {
            String pinRequestUrl = new PinRequest().getUrlString(adId);
            Logger.d(TAG, pinRequestUrl);
            Request pinRequest = new Request.Builder()
                    .url(pinRequestUrl)
                    .addHeader("User-Agent", bvUserAgent)
                    .build();
            Response pinResponse = null;
            try {
                pinResponse = okHttpClient.newCall(pinRequest).execute();
                if (!pinResponse.isSuccessful()) {
                    Logger.e(TAG, "Unexpected code: " + pinResponse);
                    throwable = new Exception("Unexpected code: " + pinResponse);
                } else {
                    Pin[] pinsArr = gson.fromJson(pinResponse.body().charStream(), Pin[].class);
                    if (pinsArr == null) {
                        pendingPins = new ArrayList<>();
                    } else {
                        pendingPins = Arrays.asList(pinsArr);
                    }
                }
            } catch (IOException e) {
                Logger.e(TAG, "Failed get pending pins", e);
                throwable = e;
            } catch (JsonIOException | JsonSyntaxException e) {
                Logger.e(TAG, "Failed parse response", e);
                throwable = e;
            } finally {
                if (pinResponse != null && pinResponse.body() != null) {
                    pinResponse.body().close();
                }
            }
        }

        return new ResponseData(pendingPins, throwable, pinCbContainer);
    }

    @MainThread
    private void sendPinResponseData(ResponseData responseData) {
        WeakReference<PinsCallback> pinCbContainer = responseData.getPinCbContainer();
        PinsCallback pinsCallback = pinCbContainer.get();
        List<Pin> pins = responseData.getPins();
        Throwable throwable = responseData.getThrowable();
        if (pinsCallback != null) {
            if (throwable != null) {
                pinsCallback.onFailure(throwable);
            } else {
                pinsCallback.onSuccess(pins);
            }
        }
        pinCbContainer.clear();
    }
}
