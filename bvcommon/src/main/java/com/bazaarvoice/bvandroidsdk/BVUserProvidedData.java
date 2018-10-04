package com.bazaarvoice.bvandroidsdk;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;

/**
 * Container for fields we cannot derive without the containing app
 */
class BVUserProvidedData {
  private final Application application;
  private final BVConfig bvConfig;
  private final BVMobileInfo bvMobileInfo;

  BVUserProvidedData(@NonNull Application application,
                     @NonNull BVConfig bvConfig,
                     @NonNull BVMobileInfo bvMobileInfo) {
    this.application = application;
    this.bvConfig = bvConfig;
    this.bvMobileInfo = bvMobileInfo;
  }

  public Context getAppContext() {
    return application.getApplicationContext();
  }

  public Application getApplication() {
    return application;
  }

  public BVConfig getBvConfig() {
    return bvConfig;
  }

  public BVMobileInfo getBvMobileInfo() {
    return bvMobileInfo;
  }
}
