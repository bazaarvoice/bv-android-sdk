package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.warnShouldNotBeEmpty;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.PersonalizationEvent.PROFILE_ID;

class BVPersonalizationEvent extends BVMobileAnalyticsEvent {
  private final String profileId;

  BVPersonalizationEvent(@NonNull String profileId) {
    super(BVEventValues.BVEventClass.PERSONALIZATION, BVEventValues.BVEventType.PROFILE);
    this.profileId = profileId;
  }

  @Override
  public Map<String, Object> toRaw() {
    Map<String, Object> map = super.toRaw();
    warnShouldNotBeEmpty("profileId", profileId);
    mapPutSafe(map, PROFILE_ID, profileId);
    mapPutSafe(map, "bvProduct", "ShopperMarketing");
    mapPutSafe(map, BVEventKeys.Event.SOURCE, "ProfileMobile"); // TODO: This seems weird, seems like possibly a type since it matches the type
    return map;
  }
}
