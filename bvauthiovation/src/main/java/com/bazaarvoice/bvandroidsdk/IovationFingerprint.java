package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import com.iovation.mobile.android.FraudForceManager;
import com.iovation.mobile.android.FraudForceConfiguration;
import androidx.annotation.NonNull;

public final class IovationFingerprint implements FingerprintProvider {
  private final Context appContext;
  private String fingerPrint;
  private boolean started = false;
  private FraudForceManager fraudForceManager;

  public IovationFingerprint(@NonNull BVSDK bvsdk) {
    this.appContext = bvsdk.getBvUserProvidedData().getAppContext();
  }

  @Override
  public String getFingerprint() throws VerifyError {
    if (!started) {
      FraudForceConfiguration fraudForceConfiguration = new FraudForceConfiguration.Builder()
              .enableNetworkCalls(true)
              .build();

      fraudForceManager = FraudForceManager.INSTANCE;
      fraudForceManager.initialize(fraudForceConfiguration, appContext);
      fingerPrint = fraudForceManager.getBlackbox(appContext);
      started = true;
      BVSDK.getInstance().bvLogger.d("Iovation", "Started FraudForceManager");
    }
    return fingerPrint;
  }
}