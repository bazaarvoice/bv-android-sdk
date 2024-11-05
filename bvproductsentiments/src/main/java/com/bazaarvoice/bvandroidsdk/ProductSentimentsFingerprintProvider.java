package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.WorkerThread;

public interface ProductSentimentsFingerprintProvider {
  ProductSentimentsFingerprintProvider EMPTY = new ProductSentimentsFingerprintProvider() {
    @Override
    public String getFingerprint() {
      return "";
    }
  };

  @WorkerThread
  String getFingerprint();
}
