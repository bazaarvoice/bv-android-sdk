package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.warnShouldNotBeEmpty;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.MobileAppLifecycleEvent.APP_STATE;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.MobileAppLifecycleEvent.BV_SDK_VERSION;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.MobileAppLifecycleEvent.MOBILE_APP_IDENTIFIER;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.MobileAppLifecycleEvent.MOBILE_APP_VERSION;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.MobileAppLifecycleEvent.MOBILE_DEVICE_NAME;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.MobileAppLifecycleEvent.MOBILE_OS;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.MobileAppLifecycleEvent.MOBILE_OS_VERSION;

class BVMobileAppLifecycleEvent extends BVMobileAnalyticsEvent {
  private final BVEventValues.AppState appState;

  BVMobileAppLifecycleEvent(@NonNull BVEventValues.AppState appState) {
    super(BVEventValues.BVEventClass.LIFECYCLE, BVEventValues.BVEventType.MOBILE_APP);
    warnShouldNotBeEmpty("appState", appState);
    this.appState = appState;
  }

  @Override
  public Map<String, Object> toRaw() {
    Map<String, Object> map = super.toRaw();
    BVMobileInfo bvMobileInfo = getBvMobileParams().getMobileInfo();
    mapPutSafe(map, APP_STATE, appState.getValue());
    mapPutSafe(map, MOBILE_OS, bvMobileInfo.getMobileOs());
    mapPutSafe(map, MOBILE_OS_VERSION, bvMobileInfo.getMobileOsVersion());
    mapPutSafe(map, MOBILE_DEVICE_NAME, bvMobileInfo.getMobileDeviceName());
    mapPutSafe(map, MOBILE_APP_IDENTIFIER, bvMobileInfo.getMobileAppIdentifier());
    mapPutSafe(map, MOBILE_APP_VERSION, bvMobileInfo.getMobileAppVersion());
    mapPutSafe(map, BV_SDK_VERSION, bvMobileInfo.getBvSdkVersion());
    return map;
  }
}
