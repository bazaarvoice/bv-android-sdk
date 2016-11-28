package com.bazaarvoice.bvandroidsdk;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.bazaarvoice.bvandroidsdk.internal.Utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

final class BVImageLoader implements ImageLoader {

    private OkHttpClient okHttpClient;

    BVImageLoader(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override @WorkerThread
    public Bitmap loadImage(@NonNull String url) {
        Request staticMapRequest = new Request.Builder()
                .url(url)
                .build();

        Bitmap bitmap = null;

        try {
            Response response = okHttpClient.newCall(staticMapRequest).execute();
            if (!response.isSuccessful()) {
                Logger.e(TAG, "Unexpected code " + response);
            } else {
                bitmap = Utils.decodeBitmapFromBytes(response.body().bytes(), 400, 260);
            }
            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}
