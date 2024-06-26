/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Bazaarvoice Recommendations API for getting custom product recommendations
 */
public class BVRecommendations {

    private static final String TAG = BVRecommendations.class.getSimpleName();

    public BVRecommendations() {
        BVSDK bvsdk = BVSDK.getInstance();

        String apiKeyShopperAdvertising = bvsdk.getBvUserProvidedData().getBvConfig().getApiKeyShopperAdvertising();
        if (apiKeyShopperAdvertising == null || apiKeyShopperAdvertising.isEmpty()) {
            throw new IllegalStateException("BVRecommendations SDK requires a shopper advertising api key");
        }
    }

    public void getRecommendedProducts(RecommendationsRequest recommendationsRequest, BVRecommendationsCallback callback) {
        WeakReference<BVRecommendationsCallback> cbWeakRef = new WeakReference<>(callback);
        RecAdIdCallback recAdIdCallback = new RecAdIdCallback(this, recommendationsRequest, cbWeakRef);
        BVSDK bvsdk = BVSDK.getInstance();
        AdIdRequestTask adIdRequestTask = new AdIdRequestTask(bvsdk.getBvUserProvidedData().getAppContext(), recAdIdCallback);
        adIdRequestTask.execute();
    }

    private static final class RecAdIdCallback implements AdIdRequestTask.AdIdCallback {
        private RecommendationsRequest request;
        private WeakReference<BVRecommendationsCallback> bvRecCbWeakRef;
        private BVRecommendations recs;

        RecAdIdCallback(BVRecommendations recs, RecommendationsRequest request, WeakReference<BVRecommendationsCallback> bvRecCbWeakRef) {
            this.recs = recs;
            this.request = request;
            this.bvRecCbWeakRef = bvRecCbWeakRef;
        }

        @Override
        public void onAdInfoComplete(AdIdResult result) {
            if (result.getAdInfo() != null) {
                recs.fetchRecommendations(result.getAdId(), request, bvRecCbWeakRef);
            } else {
                BVRecommendationsCallback cb = bvRecCbWeakRef.get();
                if (cb != null) {
                    cb.onFailure(new Exception(result.getErrorMessage()));
                }
            }
        }
    }

    private void fetchRecommendations(String adId, RecommendationsRequest request, WeakReference<BVRecommendationsCallback> cbWeakRef) {
        URL requestUrl;
        try {
            BVSDK bvsdk = BVSDK.getInstance();
            String recRequestUrlStr = RecommendationsRequest.toUrlString(bvsdk, adId,  request);
            requestUrl = new URL(recRequestUrlStr);
        } catch (MalformedURLException e) {

            e.printStackTrace();
            BVRecommendationsCallback cb = cbWeakRef.get();
            if (cb != null) {
                cb.onFailure(e);
                cbWeakRef.clear();
            }
            return;
        }

        RequestData requestData = new RequestData(requestUrl);
        new GetRecommendationsTask(cbWeakRef, BVSDK.getInstance().getBvLogger()).execute(requestData);
    }

    /**
     * Callback used to asynchronously receive the Bazaarvoice product recommendations
     */
    public interface BVRecommendationsCallback {
        void onSuccess(BVRecommendationsResponse response);
        void onFailure(Throwable throwable);
    }

    public interface BVRecommendationsLoader {
        void loadRecommendations(RecommendationsRequest request, BVRecommendations.BVRecommendationsCallback callback);
    }

    private static final class RequestData {
        private URL requestUrl;

        RequestData(URL requestUrl) {
            this.requestUrl = requestUrl;
        }

        URL getRequestUrl() {
            return requestUrl;
        }
    }

    private static final class GetRecommendationsTask extends AsyncTask<RequestData, Void, BVRecommendationsResponse> {

        private final WeakReference<BVRecommendationsCallback> cbWeakRef;
        private final BVLogger bvLogger;

        GetRecommendationsTask(WeakReference<BVRecommendationsCallback> cbWeakRef, BVLogger bvLogger) {
            this.cbWeakRef = cbWeakRef;
            this.bvLogger = bvLogger;
        }

        @Override
        protected BVRecommendationsResponse doInBackground(RequestData... params) {
            RequestData requestData = params[0];
            OkHttpClient okHttpClient = BVSDK.getInstance().getBvWorkerData().getOkHttpClient();
            Gson gson = BVSDK.getInstance().getBvWorkerData().getGson();
            boolean didSucceed = false;
            Throwable errorThrowable = null;
            ShopperProfile shopperProfile = new ShopperProfile();

            bvLogger.v(TAG, "Getting recommendations: " + requestData.getRequestUrl());
            URL url = requestData.getRequestUrl();

            // Parse and validate the URL
            String scheme = url.getProtocol(); // Get the URL scheme (protocol)
            if (!scheme.equals("http") && !scheme.equals("https")) {
                throw new IllegalArgumentException("Only HTTP and HTTPS schemes are allowed");
            }


            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", BVSDK.getInstance().getBvWorkerData().getBvSdkUserAgent())
                    .build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (!response.isSuccessful()) {
                    errorThrowable = new Exception("Unsuccessful response for recommendations with error code: " + response.code());
                } else {
                    shopperProfile = gson.fromJson(response.body().charStream(), ShopperProfile.class);
                    didSucceed = true;
                }
            } catch (IOException e) {
                errorThrowable = new IOException("Request for recommendations failed", e);
            } catch (JsonIOException | JsonSyntaxException e) {
                errorThrowable = new Exception("Failed to parse recommendations");
            } catch (Exception e) {
                errorThrowable = new Exception("Exception while getting recommendations");
            } finally {
                if (response != null && response.body() != null) {
                    response.body().close();
                }
            }

            return new BVRecommendationsResponse(didSucceed, errorThrowable, shopperProfile);
        }

        @Override
        protected void onPostExecute(BVRecommendationsResponse bvRecommendationsResponse) {
            super.onPostExecute(bvRecommendationsResponse);
            BVRecommendationsCallback recommendationsCallback = cbWeakRef.get();
            if (recommendationsCallback == null) {
                bvLogger.w(TAG, "Your Recommendations callback was recycled.");
                return;
            }
            if (bvRecommendationsResponse.isDidSucceed()) {
                bvLogger.d(TAG, "Successfully received the following recommendations:\n" + bvRecommendationsResponse.getRecommendedProducts().toString());
                recommendationsCallback.onSuccess(bvRecommendationsResponse);
            } else {
                bvRecommendationsResponse.getErrorThrowable().printStackTrace();
                recommendationsCallback.onFailure(bvRecommendationsResponse.getErrorThrowable());
            }
            cbWeakRef.clear();
        }

    }

}
