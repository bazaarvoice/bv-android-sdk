/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.utils;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class DemoCache<DataItem> {

    private static final String TAG = DemoCache.class.getSimpleName();
    private long timeMillisAdded = 0l;
    private Map<String, DataItem> cachedMap = new HashMap<>();

    protected DemoCache() {}

    public void putData(@NonNull List<DataItem> dataItemList) {
        for (DataItem dataItem : dataItemList) {
            String key = getKey(dataItem);
            if (TextUtils.isEmpty(key)) {
                throw new IllegalStateException("Must implement a valid key to cache data");
            }
            if (cachedMap.containsKey(key)) {
                Log.w(TAG, "The Demo Cache already contains a value for the key " + key);
            }
            putDataItem(key, dataItem);
        }
    }

    public void putDataItem(@NonNull String key, @NonNull DataItem dataItem) {
        if (cachedMap.size() == 0) {
            timeMillisAdded = System.currentTimeMillis();
        }
        cachedMap.put(key, dataItem);
    }

    /**
     * Implementing this is required since it's abstract, but may return null
     * if you only intend to insert data into the cache using
     * {@link #putDataItem(String, Object)}, and provide a key yourself.
     *
     * @param dataItem Value that the key will be mapped to
     * @return A unique string that will act as the key to the dataItem
     */
    protected abstract String getKey(DataItem dataItem);

    public List<DataItem> getData() {
        evictionCheck();
        return new ArrayList<>(cachedMap.values());
    }

    public DataItem getDataItem(String key) {
        evictionCheck();
        return cachedMap.get(key);
    }

    private void evictionCheck() {
        if (shouldEvict()) {
            clear();
        }
    }

    protected long getTimeMillisAdded() {
        return timeMillisAdded;
    }

    /**
     * @return Whether to evict or not. Default is to evict every ten minutes
     */
    protected boolean shouldEvict() {
        long tenMinutesInMillis = TimeUnit.MINUTES.toMillis(10);
        long tenMinutesAgoInMillis = System.currentTimeMillis() - tenMinutesInMillis;
        boolean olderThan10Minutes = timeMillisAdded < tenMinutesAgoInMillis;
        return olderThan10Minutes;
    }

    public void clear() {
        cachedMap.clear();
    }
}
