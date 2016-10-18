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
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Bazaarvoice Recommendations API for getting custom product recommendations
 */
public class BVRecommendations {

    private static final String TAG = BVRecommendations.class.getSimpleName();

    BVRecommendations() {
        BVSDK bvsdk = BVSDK.getInstance();

        String apiKeyShopperAdvertising = bvsdk.getApiKeys().getApiKeyShopperAdvertising();
        if (apiKeyShopperAdvertising == null || apiKeyShopperAdvertising.isEmpty()) {
            throw new IllegalStateException("BVRecommendations SDK requires a shopper advertising api key");
        }
    }

    public void getRecommendedProducts(RecommendationsRequest recommendationsRequest, BVRecommendationsCallback callback) {
        WeakReference<BVRecommendationsCallback> cbWeakRef = new WeakReference<BVRecommendationsCallback>(callback);
        RecAdIdCallback recAdIdCallback = new RecAdIdCallback(this, recommendationsRequest, cbWeakRef);
        BVSDK bvsdk = BVSDK.getInstance();
        AdIdRequestTask adIdRequestTask = bvsdk.getAdIdRequestTask(recAdIdCallback);
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
            String baseUrlStr = bvsdk.getShopperMarketingApiRootUrl();
            String apiKey = bvsdk.getApiKeys().getApiKeyShopperAdvertising();
            String clientId = bvsdk.getClientId();
            String recRequestUrlStr = RecommendationsRequest.toUrlString(baseUrlStr, adId, apiKey, clientId, request);
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
        new GetRecommendationsTask(cbWeakRef).execute(requestData);
    }

    /**
     * Callback used to asynchronously receive the Bazaarvoice product recommendations
     */
    public interface BVRecommendationsCallback {
        void onSuccess(List<BVProduct> recommendedProducts);
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

    private static final class ResponseData {
        private boolean didSucceed;
        private Throwable errorThrowable;
        private List<BVProduct> recommendedProducts;

        ResponseData(boolean didSucceed, Throwable errorThrowable, List<BVProduct> recommendedProducts) {
            this.didSucceed = didSucceed;
            this.errorThrowable = errorThrowable;
            this.recommendedProducts = recommendedProducts;
        }

        boolean isDidSucceed() {
            return didSucceed;
        }

        Throwable getErrorThrowable() {
            return errorThrowable;
        }

        public List<BVProduct> getRecommendedProducts() {
            return recommendedProducts;
        }
    }

    private static final class GetRecommendationsTask extends AsyncTask<RequestData, Void, ResponseData> {

        private final WeakReference<BVRecommendationsCallback> cbWeakRef;

        GetRecommendationsTask(WeakReference<BVRecommendationsCallback> cbWeakRef) {
            this.cbWeakRef = cbWeakRef;
        }

        @Override
        protected ResponseData doInBackground(RequestData... params) {
            RequestData requestData = params[0];
            OkHttpClient okHttpClient = BVSDK.getInstance().getOkHttpClient();
            Gson gson = BVSDK.getInstance().getGson();
            boolean didSucceed = false;
            Throwable errorThrowable = null;
            List<BVProduct> recommendedProducts = null;
            Request request = new Request.Builder()
                    .url(requestData.getRequestUrl())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", BVSDK.getInstance().getBvsdkUserAgent())
                    .build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (!response.isSuccessful()) {
                    errorThrowable = new Exception("Unsuccessful response for recommendations with error code: " + response.code());
                } else {
                    ShopperProfile shopperProfile = gson.fromJson(response.body().charStream(), ShopperProfile.class);
                    recommendedProducts = shopperProfile.getProfile().getRecommendedProducts();
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

            return new ResponseData(didSucceed, errorThrowable, recommendedProducts);
        }

        @Override
        protected void onPostExecute(ResponseData responseData) {
            super.onPostExecute(responseData);
            BVRecommendationsCallback recommendationsCallback = cbWeakRef.get();
            if (recommendationsCallback == null) {
                Logger.w(TAG, "Your Recommendations callback was recycled.");
                return;
            }
            if (responseData.isDidSucceed()) {
                Logger.d(TAG, "Succesfully received the following recommendations:\n" + responseData.getRecommendedProducts().toString());
                recommendationsCallback.onSuccess(responseData.getRecommendedProducts());
            } else {
                responseData.getErrorThrowable().printStackTrace();
                recommendationsCallback.onFailure(responseData.getErrorThrowable());
            }
            cbWeakRef.clear();
        }

    }

}
