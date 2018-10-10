package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.WorkerThread;

public interface FingerprintProvider {
  FingerprintProvider EMPTY = new FingerprintProvider() {
    @Override
    public String getFingerprint() {
      return "";
    }
  };

  @WorkerThread
  String getFingerprint();
}
