package com.bazaarvoice.bvandroidsdk;

import android.content.Context;

import com.iovation.mobile.android.DevicePrint;

public final class IovationFingerprint implements FingerprintProvider {
  private final Context appContext;
  private String fingerPrint;
  private boolean started = false;

  public IovationFingerprint(BVSDK bvsdk) {
    this.appContext = bvsdk.getBvUserProvidedData().getAppContext();
  }

  @Override
  public String getFingerprint() throws VerifyError {
    if (!started) {
      DevicePrint.start(appContext);
      fingerPrint = DevicePrint.getBlackbox(appContext);
      started = true;
    }
    return fingerPrint;
  }
}