/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.os.AsyncTask;
import android.view.ViewGroup;

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
    private String apiKeyShopperAdvertising;
    private String adID;

    private WeakReference<BVRecommendationsCallback> bvRecCbWeakRef;

    public BVRecommendations() {
        BVSDK bvsdk = BVSDK.getInstance();

        this.apiKeyShopperAdvertising = bvsdk.getApiKeyShopperAdvertising();
        if (apiKeyShopperAdvertising == null || apiKeyShopperAdvertising.isEmpty()) {
            throw new IllegalStateException("BVRecommendations SDK requires a shopper advertising api key");
        }
    }

    private void getRecommendedProducts(final int limit, final String productId, final String categoryId, final BVRecommendationsCallback callback) {
        BVSDK bvsdk = BVSDK.getInstance();
        bvRecCbWeakRef = new WeakReference<BVRecommendationsCallback>(callback);

        bvsdk.getAdvertisingIdClient().getAdInfo(new BVSDK.GetAdInfoCompleteAction() {
            @Override
            public void completionAction(AdInfo adInfo) {
                int validatedLimit = limit;
                if (limit < 0 || limit > 50) {
                    validatedLimit = 20;
                }

                adID = adInfo.getId();

                RecommendationParams params = new RecommendationParams();
                params.categoryId = categoryId;
                params.productId = productId;
                params.limit = validatedLimit;

                URL requestUrl;
                try {
                     requestUrl = new URL(getRecommendationsUrlString(params));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    BVRecommendationsCallback cb = bvRecCbWeakRef.get();
                    if (cb != null) {
                        cb.onFailure(e);
                    }
                    return;
                }

                RequestData requestData = new RequestData(requestUrl);
                new GetRecommendationsTask().execute(requestData);
            }
        });
    }

    /**
     *  @param limit max number of recommended products default is 20
     *              valid limits 1...50; will use default if out of range
     * @param categoryId used to filter recommended products within a
     *                   category. Mutually exclusive with productId. if
     *                   productId and categoryId are set, productId will
     *                   be used
     * @param callback callback to provide list of Products or handle errors
     */
    public void getRecommendedProductsWithCategoryId(final int limit, String categoryId, BVRecommendationsCallback callback) {
        getRecommendedProducts(limit, null, categoryId, callback);
    }

    /**
     *  @param limit max number of recommended products default is 20 valid
     *              limits 1...50; will use default if out of range
     * @param productId used to filter recommended products to similar products
     * @param callback callback to provide list of Products or handle errors
     */
    public void getRecommendedProductsWithProductId(final int limit, String productId, BVRecommendationsCallback callback) {
        getRecommendedProducts(limit, productId, null, callback);
    }

    /**
     *  @param limit max number of recommended products
     * @param callback callback to provide list of Products or handle errors
     */
    public void getRecommendedProducts(int limit, BVRecommendationsCallback callback) {
        getRecommendedProducts(limit, null, null, callback);
    }

    /**
     * Callback used to asynchronously receive the Bazaarvoice product recommendations
     */
    public interface BVRecommendationsCallback {
        void onSuccess(List<BVProduct> recommendedProducts);
        void onFailure(Throwable throwable);
    }

    private void validateBvViewGroup(ViewGroup bvViewGroup) {
        if (bvViewGroup == null) {
            throw new IllegalStateException("bvViewGroup must be non-null");
        }
        boolean isBvViewGroup = (bvViewGroup instanceof RecommendationsListView) || (bvViewGroup instanceof RecommendationsContainerView) || (bvViewGroup instanceof RecommendationsRecyclerView);
        if (!isBvViewGroup) {
            throw new IllegalStateException("bvViewGroup must be one of the BVSDK provided ViewGroups that is wrapping your BvViews");
        }
    }

    //Helper Classes
//*****************************************************************************

    private class RecommendationParams {
        protected int limit;
        protected String productId;
        protected String categoryId;
    }

    protected String getRecommendationsUrlString(RecommendationParams params) {
        BVSDK bvsdk = BVSDK.getInstance();

        /**
         * Request statistics.
         */
        String userIdentifier = adID;
        String similarityParams = "";
        //mutually exclusive productId and categoryId. productId before categoryId
        if (params.productId != null) {
            similarityParams = "&product=" + bvsdk.getClientId()+"/"+params.productId;
        } else if (params.categoryId != null) {
            similarityParams = "&category=" + bvsdk.getClientId()+"/"+params.categoryId;
        }

        String urlString = String.format("%s/recommendations/magpie_idfa_%s?passKey=%s%s&limit=%d",bvsdk.getShopperMarketingApiRootUrl(),userIdentifier, apiKeyShopperAdvertising,similarityParams,params.limit);
        if (bvsdk.getClientId() != null && bvsdk.getClientId().length() != 0) {
            urlString += "&client="+ bvsdk.getClientId();
        }
        return urlString;

    }

    private static final class RequestData {
        private URL requestUrl;

        public RequestData(URL requestUrl) {
            this.requestUrl = requestUrl;
        }

        public URL getRequestUrl() {
            return requestUrl;
        }
    }

    private static final class ResponseData {
        private boolean didSucceed;
        private Throwable errorThrowable;
        private List<BVProduct> recommendedProducts;

        public ResponseData(boolean didSucceed, Throwable errorThrowable, List<BVProduct> recommendedProducts) {
            this.didSucceed = didSucceed;
            this.errorThrowable = errorThrowable;
            this.recommendedProducts = recommendedProducts;
        }

        public boolean isDidSucceed() {
            return didSucceed;
        }

        public Throwable getErrorThrowable() {
            return errorThrowable;
        }

        public List<BVProduct> getRecommendedProducts() {
            return recommendedProducts;
        }
    }

    private class GetRecommendationsTask extends AsyncTask<RequestData, Void, ResponseData> {

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
            BVRecommendationsCallback recommendationsCallback = bvRecCbWeakRef.get();
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
        }

    }

}
