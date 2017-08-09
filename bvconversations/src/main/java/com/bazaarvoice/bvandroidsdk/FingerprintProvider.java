package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.WorkerThread;

public interface FingerprintProvider {
  FingerprintProvider EMPTY = new FingerprintProvider() {
    @Override
    public String getFingerprint() {
      return "";
    }
  };

  @WorkerThread String getFingerprint();
}
