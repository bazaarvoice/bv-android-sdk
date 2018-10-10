package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVEventValues.NONTRACKING_TOKEN;

class BVAdvertisingId {
  private final String advertisingId;

  /**
   * Must be constructed off of the UI Thread since this calls
   * {@link com.google.android.gms.ads.identifier.AdvertisingIdClient#getAdvertisingIdInfo(Context)}
   * which will throw an IllegalStateException if run on the UI Thread
   *
   * @param context
   */
  BVAdvertisingId(@NonNull Context context) {
    BvAdInfoFetcher bvAdInfoFetcher = new BvAdInfoFetcher(context);
    BvAdIdDecider bvAdIdDecider = new BvAdIdDecider(bvAdInfoFetcher);
    this.advertisingId = bvAdIdDecider.getAdId();
  }

  String getAdvertisingId() {
    return advertisingId;
  }

  static class BvAdInfoFetcher {
    private final Context appContext;

    public BvAdInfoFetcher(@NonNull Context context) {
      this.appContext = context.getApplicationContext();
    }

    @Nullable
    public AdvertisingIdClient.Info getAdInfo() {
      AdvertisingIdClient.Info adInfo = null;
      Exception exception = null;
      try {
        adInfo = AdvertisingIdClient.getAdvertisingIdInfo(appContext);
      } catch (IOException e) {
        // Unrecoverable error connecting to Google Play services (e.g.,
        // the old version of the service doesn't support getting AdvertisingId).
        exception = e;
      } catch (GooglePlayServicesNotAvailableException e) {
        // Google Play services is not available entirely.
        exception = e;
      } catch (GooglePlayServicesRepairableException e) {
        exception = e;
      }

      if (exception != null) {
        Log.e("BvAnalytics", "error finding advertising id", exception);
      }

      return adInfo;
    }
  }

  static class BvAdIdDecider {
    private final BvAdInfoFetcher bvAdInfoFetcher;

    public BvAdIdDecider(BvAdInfoFetcher bvAdInfoFetcher) {
      this.bvAdInfoFetcher = bvAdInfoFetcher;
    }

    @NonNull
    public String getAdId() {
      AdvertisingIdClient.Info adInfo = bvAdInfoFetcher.getAdInfo();

      if (adInfo == null) {
        return NONTRACKING_TOKEN;
      } else if (adInfo.isLimitAdTrackingEnabled()) {
        return NONTRACKING_TOKEN;
      } else {
        return adInfo.getId();
      }
    }
  }

  static final class Mapper implements BVAnalyticsMapper {
    private BVAdvertisingId bvAdvertisingId;

    public Mapper(@NonNull BVAdvertisingId bvAdvertisingId) {
      this.bvAdvertisingId = bvAdvertisingId;
    }

    @Override
    public Map<String, Object> toRaw() {
      Map<String, Object> map = new HashMap<>();
      BVAnalyticsUtils.mapPutSafe(map, BVEventKeys.MobileEvent.ADVERTISING_ID, bvAdvertisingId.getAdvertisingId());
      return null;
    }
  }
}
