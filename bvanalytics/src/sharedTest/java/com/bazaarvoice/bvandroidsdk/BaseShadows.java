package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.net.Network;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.io.IOException;

final class BaseShadows {
  public static final String TEST_AD_ID = "00000000-0000-0000-0000-000000000000";

  @Implements(AdvertisingIdClient.class)
  public static class ShadowAdIdClientNoLimit {
    @Implementation
    public static AdvertisingIdClient.Info getAdvertisingIdInfo(Context context) throws IOException, IllegalStateException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
      return new AdvertisingIdClient.Info(TEST_AD_ID, false);
    }
  }

  @Implements(AdvertisingIdClient.class)
  public static class ShadowAdIdClientWithLimit {
    @Implementation
    public static AdvertisingIdClient.Info getAdvertisingIdInfo(Context context) throws IOException, IllegalStateException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
      return new AdvertisingIdClient.Info(TEST_AD_ID, true);
    }
  }

  @Implements(AdvertisingIdClient.class)
  public static class ShadowAdIdClientNoPlayServices {
    @Implementation
    public static AdvertisingIdClient.Info getAdvertisingIdInfo(Context context) throws IOException, IllegalStateException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
      throw new GooglePlayServicesNotAvailableException(0);
    }
  }

  /**
   * Here because https://github.com/robolectric/robolectric/issues/2223
   */
  @Implements(Network.class)
  public static class ShadowNetwork {

  }
}
