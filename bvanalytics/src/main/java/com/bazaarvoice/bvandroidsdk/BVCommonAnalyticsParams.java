package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;

class BVCommonAnalyticsParams {
  private final String userAgent;
  private final String hashedIp;

  /**
   * @param context Required to fetch a key stored in SharedPrefs
   */
  BVCommonAnalyticsParams(@NonNull Context context) {
    this.userAgent = BVAnalyticsUtils.getUuid(context).toString();
    this.hashedIp = BVEventValues.HASHED_IP;
  }

  String getUserAgent() {
    return userAgent;
  }

  String getHashedIp() {
    return hashedIp;
  }

  static class Mapper implements BVAnalyticsMapper {
    private BVCommonAnalyticsParams commonAnalyticsParams;

    Mapper(@NonNull BVCommonAnalyticsParams commonAnalyticsParams) {
      this.commonAnalyticsParams = commonAnalyticsParams;
    }

    @Override
    public Map<String, Object> toRaw() {
      Map<String, Object> map = new HashMap<>();
      mapPutSafe(map, BVEventKeys.CommonAnalyticsParams.USER_AGENT, commonAnalyticsParams.getUserAgent());
      mapPutSafe(map, BVEventKeys.CommonAnalyticsParams.HASHED_IP, commonAnalyticsParams.getHashedIp());
      return map;
    }
  }
}
