package com.bazaarvoice.bvandroidsdk;

import java.util.HashMap;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutAllSafe;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.mapPutSafe;
import static com.bazaarvoice.bvandroidsdk.BVAnalyticsUtils.warnShouldNotBeEmpty;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.Event.CLASS;
import static com.bazaarvoice.bvandroidsdk.BVEventKeys.Event.TYPE;

public abstract class BVAnalyticsEvent implements BVAnalyticsMapper {
  private final BVEventValues.BVEventClass eventClass;
  private final BVEventValues.BVEventType eventType;
  private final String customEventType;
  protected Map<String, Object> additionalParams;
  private BVCommonAnalyticsParams bvCommonAnalyticsParams;

  public BVAnalyticsEvent(BVEventValues.BVEventClass eventClass,
                          String customEventType) {
    this.eventClass = eventClass;
    warnShouldNotBeEmpty("eventType", customEventType);
    this.customEventType = customEventType;
    this.eventType = null;
    this.additionalParams = new HashMap<>();
  }

  public BVAnalyticsEvent(BVEventValues.BVEventClass eventClass,
                          BVEventValues.BVEventType eventType) {
    this.eventClass = eventClass;
    this.eventType = eventType;
    this.customEventType = null;
    this.additionalParams = new HashMap<>();
  }

  public void setAdditionalParams(Map<String, Object> additionalParams) {
    if (additionalParams == null) {
      return;
    }
    this.additionalParams = additionalParams;
  }

  protected void setBvCommonAnalyticsParams(BVCommonAnalyticsParams bvCommonAnalyticsParams) {
    this.bvCommonAnalyticsParams = bvCommonAnalyticsParams;
  }

  @Override
  public Map<String, Object> toRaw() {
    Map<String, Object> map = new HashMap<>();
    mapPutSafe(map, CLASS, eventClass.toString());
    mapPutSafe(map, TYPE, eventType == null ? customEventType : eventType.toString());
    mapPutAllSafe(map, additionalParams);
    warnShouldNotBeEmpty("bvCommonAnalyticsParams", bvCommonAnalyticsParams);
    BVCommonAnalyticsParams.Mapper commonParamsMapper = new BVCommonAnalyticsParams.Mapper(bvCommonAnalyticsParams);
    Map<String, Object> commonParamsMap = commonParamsMapper.toRaw();
    mapPutAllSafe(map, commonParamsMap);
    return map;
  }
}
