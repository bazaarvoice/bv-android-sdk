package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.os.Build;

import com.bazaarvoice.bvandroidsdk_analytics.BuildConfig;

class BVMobileInfo {
  private final String mobileOs;
  private final String mobileOsVersion;
  private final String mobileDeviceName;
  private final String mobileAppIdentifier;
  private final String mobileAppVersion;
  private final String mobileAppCode;
  private final String bvSdkVersion;

  BVMobileInfo(Context appContext) {
    this.mobileOs = BVEventValues.MOBILE_OS;
    this.mobileOsVersion = Build.VERSION.RELEASE;
    this.mobileDeviceName = Build.MODEL;
    this.mobileAppIdentifier = BVAnalyticsUtils.getPackageName(appContext);
    this.mobileAppVersion = BVAnalyticsUtils.getVersionName(appContext);
    this.mobileAppCode = BVAnalyticsUtils.getVersionCode(appContext);
    this.bvSdkVersion = BuildConfig.BVSDK_VERSION_NAME;
  }

  public String getMobileOs() {
    return mobileOs;
  }

  public String getMobileOsVersion() {
    return mobileOsVersion;
  }

  public String getMobileDeviceName() {
    return mobileDeviceName;
  }

  public String getMobileAppIdentifier() {
    return mobileAppIdentifier;
  }

  public String getMobileAppVersion() {
    return mobileAppVersion;
  }

  public String getMobileAppCode() {
    return mobileAppCode;
  }

  public String getBvSdkVersion() {
    return bvSdkVersion;
  }
}
