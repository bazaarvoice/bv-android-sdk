package com.bazaarvoice.bvandroidsdk;

import android.content.Context;

import com.iovation.mobile.android.FraudForceManager;

import androidx.annotation.NonNull;


public final class IovationFingerprint implements FingerprintProvider {
  private final Context appContext;
  private String fingerPrint;
  private boolean started = false;

  public IovationFingerprint(@NonNull BVSDK bvsdk) {
    this.appContext = bvsdk.getBvUserProvidedData().getAppContext();
  }

  @Override
  public String getFingerprint() throws VerifyError {
    if (!started) {
      FraudForceManager fraudForceManager = FraudForceManager.getInstance();
      fraudForceManager.initialize(appContext);
      fingerPrint = fraudForceManager.getBlackbox(appContext);
      started = true;
      BVSDK.getInstance().bvLogger.d("Iovation", "Started fraudForceManager");
    }
    return fingerPrint;
  }
}