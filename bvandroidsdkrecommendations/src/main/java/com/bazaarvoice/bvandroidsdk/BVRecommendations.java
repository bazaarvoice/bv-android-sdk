/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Bazaarvoice Recommendations API for getting custom product recommendations
 */
public class BVRecommendations implements Recommendations{

    private String apiKeyShopperAdvertising;
    private String adID;
    private boolean isStaging = false;

    private WeakReference<BVRecommendationsCallback> bvRecommendationsCallback;

    public BVRecommendations() {
        BVSDK bvsdk = BVSDK.getInstance();

        this.apiKeyShopperAdvertising = bvsdk.getApiKeyShopperAdvertising();
        if (apiKeyShopperAdvertising == null || apiKeyShopperAdvertising.isEmpty()) {
            throw new IllegalStateException("BVRecommendations SDK requires a shopper advertising api key");
        }

        this.isStaging = Utils.isStagingEnvironment(bvsdk.getEnvironment());
    }

    private String getRootUrl() {
        return isStaging ? "https://my.network-stg.bazaarvoice.com" : "https://my.network.bazaarvoice.com";
    }

    void getRecommendedProducts(final int limit, final String productId, final String categoryId, final BVRecommendationsCallback callback) {
        BVSDK.getInstance().getAdvertisingIdClient().getAdInfo(new BVSDK.GetAdInfoCompleteAction() {
            @Override
            public void completionAction(AdInfo adInfo) {
                bvRecommendationsCallback = new WeakReference<BVRecommendationsCallback>(callback);
                int validatedLimit = limit;
                if (limit < 0 || limit > 50) {
                    validatedLimit = 20;
                }

                adID = adInfo.getId();

                RecommendationParams params = new RecommendationParams();
                params.categoryId = categoryId;
                params.productId = productId;
                params.limit = validatedLimit;

                new AsyncRecsGet().execute(getRecommendationsUrlString(params));


            }
        });
    }

    @Override
    public void getRecommendedProductsWithCategoryId(final int limit, String categoryId, BVRecommendationsCallback callback)
    {
        getRecommendedProducts(limit,null, categoryId, callback);
    }

    @Override
    public void getRecommendedProductsWithProductId(final int limit, String productId, BVRecommendationsCallback callback)
    {
        getRecommendedProducts(limit, productId, null, callback);
    }


    @Override
    public void getRecommendedProducts(int limit, BVRecommendationsCallback callback) {
        getRecommendedProducts(limit, null, null, callback);
    }

    //Helper Classes
//*****************************************************************************

    private class RecommendationParams
    {
        protected int limit;
        protected String productId;
        protected String categoryId;
    }

    protected String getRecommendationsUrlString( RecommendationParams params) {
        BVSDK bvsdk = BVSDK.getInstance();

        /**
         * Request statistics.
         */
        String userIdentifier = adID;
        String similarityParams = "";
        //mutually exclusive productId and categoryId. productId before categoryId
        if (params.productId != null)
        {
            similarityParams = "&product=" + bvsdk.getClientId()+"/"+params.productId;
        }else if (params.categoryId != null)
        {
            similarityParams = "&category=" + bvsdk.getClientId()+"/"+params.categoryId;
        }

        String urlString = String.format("%s/recommendations/magpie_idfa_%s?passKey=%s%s&limit=%d",getRootUrl(),userIdentifier, apiKeyShopperAdvertising,similarityParams,params.limit);
        if (bvsdk.getClientId() != null && bvsdk.getClientId().length() != 0)
        {
            urlString += "&client="+ bvsdk.getClientId();
        }
        return urlString;

    }

    private class AsyncRecsGet extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... args) {

            String response = "";
            HttpURLConnection connection = null;
            try {
                URL url = new URL(args[0]);

                connection = (HttpURLConnection) url.openConnection();

                // Allow Inputs
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.connect();


                int responseCode =connection.getResponseCode();

                if (responseCode >= 200 && responseCode < 300) {
                    response = readInputStreamToString(connection);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (bvRecommendationsCallback.get() != null) {
                    bvRecommendationsCallback.get().onFailure(e);
                }
            }finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            List<BVProduct> recommendedProducts = null;

            try {
                Gson gson = BVSDK.getInstance().getGson();
                ShopperProfile profile = gson.fromJson(result, ShopperProfile.class);
                recommendedProducts = profile.getProfile().getRecommendedProducts();
            } catch (Exception e) {
                e.printStackTrace();
                if (bvRecommendationsCallback.get() != null) {
                    bvRecommendationsCallback.get().onFailure(e);
                }
                return;
            }

            if (bvRecommendationsCallback.get() != null) {
                bvRecommendationsCallback.get().onSuccess(recommendedProducts);
            }
        }

        private String readInputStreamToString(HttpURLConnection connection) throws Exception {
            String result = null;
            StringBuffer sb = new StringBuffer();
            InputStream is = null;

            try {
                is = new BufferedInputStream(connection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                result = sb.toString();
            }
            catch (Exception e) {
                result = null;
                e.printStackTrace();
                throw e;
            }
            finally {
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
            }

            return result;
        }
    }
//*****************************************************************************

}
