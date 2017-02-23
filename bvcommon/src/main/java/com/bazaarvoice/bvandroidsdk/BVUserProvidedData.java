package com.bazaarvoice.bvandroidsdk;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Container for fields we cannot derive without the containing app
 */
class BVUserProvidedData {
  private final Application application;
  private final String clientId;
  private final BVApiKeys bvApiKeys;
  private final BVMobileInfo bvMobileInfo;

  BVUserProvidedData(@NonNull Application application,
                     @NonNull String clientId,
                     @NonNull BVApiKeys bvApiKeys,
                     @NonNull BVMobileInfo bvMobileInfo) {
    this.application = application;
    this.clientId = clientId;
    this.bvApiKeys = bvApiKeys;
    this.bvMobileInfo = bvMobileInfo;
  }

  public Context getAppContext() {
    return application.getApplicationContext();
  }

  public Application getApplication() {
    return application;
  }

  public String getClientId() {
    return clientId;
  }

  public BVApiKeys getBvApiKeys() {
    return bvApiKeys;
  }

  public BVMobileInfo getBvMobileInfo() {
    return bvMobileInfo;
  }
}
