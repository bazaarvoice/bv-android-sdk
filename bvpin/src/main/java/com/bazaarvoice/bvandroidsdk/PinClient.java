package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.internal.ListenerContainer;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class PinClient {
    private static final String TAG = "PinClient";
    private BVSDK bvsdk;
    private ListenerContainer<PinsCallback> pinCbContainer;

    public PinClient() {
        bvsdk = BVSDK.getInstance();
        pinCbContainer = new ListenerContainer<>();
    }

    public interface PinsCallback {
        void onSuccess(List<Pin> pins);
        void onFailure(Throwable throwable);
    }

    public void getPendingPins(PinsCallback callback) {
        pinCbContainer.add(callback);
        PinTask pinTask = new PinTask(pinCbContainer);
        pinTask.execute(bvsdk);
    }

    private static final class ResponseData {
        private List<Pin> pins;
        private Throwable throwable;

        public ResponseData(@Nullable List<Pin> pins, @Nullable Throwable throwable) {
            this.pins = pins;
            this.throwable = throwable;
        }

        @Nullable
        public List<Pin> getPins() {
            return pins;
        }

        @Nullable
        public Throwable getThrowable() {
            return throwable;
        }
    }

    private static final class PinTask extends AsyncTask<BVSDK, Void, ResponseData> {
        private ListenerContainer<PinsCallback> pinCbContainer;

        PinTask(ListenerContainer<PinsCallback> pinCbContainer) {
            this.pinCbContainer = pinCbContainer;
        }

        @Override
        protected ResponseData doInBackground(BVSDK... params) {
            List<Pin> pendingPins = null;
            Throwable throwable = null;

            BVSDK bvsdk = params[0];
            Context appContext = bvsdk.getApplicationContext();
            OkHttpClient okHttpClient = bvsdk.getOkHttpClient();
            Gson gson = bvsdk.getGson();

            AdIdResult adIdResult = AdIdRequestTask.getAdId(appContext);
            String adId = adIdResult.getAdId();
            if (adIdResult.isNonTracking()) {
                // Not going to have any pin stuff, return now
                pendingPins = new ArrayList<>();
            } else {
                String pinRequestUrl = new PinRequest().getUrlString(adId);
                Request pinRequest = new Request.Builder().url(pinRequestUrl).build();
                Response pinResponse = null;
                try {
                    pinResponse = okHttpClient.newCall(pinRequest).execute();
                    if (!pinResponse.isSuccessful()) {
                        Logger.e(TAG, "Unexpected code: " + pinResponse);
                        throwable = new Exception("Unexpected code: " + pinResponse);
                    } else {
                        Pin[] pinsArr = gson.fromJson(pinResponse.body().charStream(), Pin[].class);
                        pendingPins = Arrays.asList(pinsArr);
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

            return new ResponseData(pendingPins, throwable);
        }

        @Override
        protected void onPostExecute(ResponseData responseData) {
            super.onPostExecute(responseData);
            List<Pin> pins = responseData.getPins();
            Throwable throwable = responseData.getThrowable();

            for (PinsCallback pinsCb : pinCbContainer.getListeners()) {
                if (pins == null) {
                    pinsCb.onFailure(throwable);
                } else {
                    pinsCb.onSuccess(pins);
                }
            }
            pinCbContainer.removeAll();
        }
    }
}
