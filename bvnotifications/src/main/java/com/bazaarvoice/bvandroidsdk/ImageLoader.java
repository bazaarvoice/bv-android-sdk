package com.bazaarvoice.bvandroidsdk;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

interface ImageLoader {
    @WorkerThread Bitmap loadImage(@NonNull String url);
}
