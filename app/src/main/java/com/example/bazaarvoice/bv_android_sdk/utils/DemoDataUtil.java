/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.example.bazaarvoice.bv_android_sdk.utils;

import android.content.Context;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsFeedResponse;
import com.bazaarvoice.bvandroidsdk.ShopperProfile;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class DemoDataUtil {

    private static List<BVProduct> savedDemoRecProds;
    private static List<CurationsFeedItem> savedCurationsFeedItems;

    private static Context applicationContext;
    private static DemoDataUtil instance;

    private DemoDataUtil(Context context) {
        applicationContext = context.getApplicationContext();
    }

    public static DemoDataUtil getInstance(Context context) {
        if (instance == null) {
            instance = new DemoDataUtil(context);
        }

        return instance;
    }

    public List<BVProduct> getRecommendedProducts() {
        if (savedDemoRecProds != null) {
            return savedDemoRecProds;
        }

        Gson gson = new Gson();
        List<BVProduct> bvProducts = new ArrayList<>();
        try {
            InputStream inputStream = applicationContext.getAssets().open("recommendationsResult.json");
            Reader reader = new InputStreamReader(inputStream);
            ShopperProfile shopperProfile = gson.fromJson(reader, ShopperProfile.class);
            bvProducts = shopperProfile.getProfile().getRecommendedProducts();
            savedDemoRecProds = bvProducts;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bvProducts;
    }

    public List<CurationsFeedItem> getCurationsFeedItems() {
        if (savedCurationsFeedItems != null) {
            return savedCurationsFeedItems;
        }

        Gson gson = new Gson();
        List<CurationsFeedItem> curationsFeedItems = new ArrayList<>();
        try {
            InputStream inputStream = applicationContext.getAssets().open("curationsEnduranceCycles.json");
            Reader reader = new InputStreamReader(inputStream);
            CurationsFeedResponse curationsFeedResponse = gson.fromJson(reader, CurationsFeedResponse.class);
            curationsFeedItems = curationsFeedResponse.getUpdates();
            savedCurationsFeedItems = curationsFeedItems;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return curationsFeedItems;
    }
}
