package com.bazaarvoice.bvandroidsdk;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

interface ImageLoader {
    @WorkerThread
    Bitmap loadImage(@NonNull String url);
}
