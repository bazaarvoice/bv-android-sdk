/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Wrapper around {@link BVPixel} for forming and sending
 * Curations specific Analytics events
 */
public class CurationsAnalyticsManager {
  private final BVPixel bvPixel;
  private final BVEventValues.BVProductType bvProductType;

  public CurationsAnalyticsManager(BVSDK bvsdk) {
    this.bvPixel = bvsdk.getBvPixel();
    this.bvProductType = BVEventValues.BVProductType.CURATIONS;
  }

  /**
   * Event should be sent whenever a CurationsFeedItem appears on the screen. Should only be fired once per CurationsFeedItem.
   * @param curationsFeedItem CurationsFeedItem that has appeared on screen.
   */
  public void sendUGCImpressionEvent(CurationsFeedItem curationsFeedItem) {
    if (curationsFeedItem != null && curationsFeedItem.impressed){
      return;
    } else if (curationsFeedItem != null) {
      curationsFeedItem.impressed = true;
    } else {
      return;
    }

    String productId = curationsFeedItem.getExternalIdInQuery() != null ?
        curationsFeedItem.getExternalIdInQuery() : "none";
    String contentId = curationsFeedItem.getId() != null ?
        String.valueOf(curationsFeedItem.getId()) : "none";
    String categoryId = null;
    String brand = null;

    BVImpressionEvent event = new BVImpressionEvent(
        productId,
        contentId,
        bvProductType,
        BVEventValues.BVImpressionContentType.CURATIONS_FEED_ITEM,
        categoryId,
        brand);

    Map<String, Object> additionalParams = new HashMap<>();
    additionalParams.put("syndicationSource", curationsFeedItem.getSourceClient());

    event.setAdditionalParams(additionalParams);

    bvPixel.track(event);
  }

  /**
   * Event should be sent whenever a Curations ViewGroup appears on the screen. Should only be fired once per Curations ViewGroup.
   * @param widgetId ID given to the Curations ViewGroup used to distinguish events.
   * @param reportingGroup The type of Curations ViewGroup used to present Curations feed (RecyclerView, ListView ...)
   */
  public void sendBvViewGroupAddedToHierarchyEvent(String widgetId, ReportingGroup reportingGroup) {
    String productId = "none";
    String brand = null;

    BVFeatureUsedEvent event = new BVFeatureUsedEvent(
        productId,
        bvProductType,
        BVEventValues.BVFeatureUsedEventType.IN_VIEW,
        brand);

    Map<String, Object> additionalParams = new HashMap<>();
    additionalParams.put(BVEventKeys.FeatureUsedEvent.CONTAINER_ID, reportingGroup.toString());
    additionalParams.put(BVEventKeys.FeatureUsedEvent.DETAIL_1, widgetId);

    event.setAdditionalParams(additionalParams);

    bvPixel.track(event);
  }

  /**
   * Event should be sent whenever a CurationsView is tapped.
   * @param curationsFeedItem The CurationsFeedItem used to populate the CurationsView UI
   */
  public void sendUsedFeatureEventTapped(CurationsFeedItem curationsFeedItem) {
    if (curationsFeedItem == null) {
      return;
    }

    String brand = null;
    String channel = curationsFeedItem.getChannel();
    String productId = externalIdToProductId(curationsFeedItem.getExternalIdInQuery());

    BVFeatureUsedEvent event = new BVFeatureUsedEvent(
        productId,
        bvProductType,
        BVEventValues.BVFeatureUsedEventType.CONTENT_CLICK,
        brand);

    Map<String, Object> additionalParams = new HashMap<>();
    additionalParams.put(BVEventKeys.FeatureUsedEvent.DETAIL_1, channel);
    additionalParams.put(BVEventKeys.FeatureUsedEvent.INTERACTION, true);

    event.setAdditionalParams(additionalParams);

    bvPixel.track(event);
  }

  /**
   * Should be sent when a ViewGroup displaying Curations feed is scrolled. Typically this is fired once on the first interaction with a UI container and when scrolling stops.
   * @param externalId If an externalId was used in the CurationsFeedRequest when the Curations feed was loaded, it should be used in this event.
   */
  public void sendUsedFeatureEventScrolled(String externalId) {
    String productId = externalIdToProductId(externalId);
    String brand = null;

    BVFeatureUsedEvent event = new BVFeatureUsedEvent(
        productId,
        BVEventValues.BVProductType.CURATIONS,
        BVEventValues.BVFeatureUsedEventType.SCROLLED,
        brand);

    bvPixel.track(event);
  }

  /**
   * Event should be sent whenever a Curations ViewGroup is created, but not necessarily displayed on screen.
   * @param externalId If an externalId was used in the CurationsFeedRequest when the Curations feed was loaded, it should be used in this event.
   * @param reportingGroup The type of Curations ViewGroup used to present Curations feed (RecyclerView, ListView ...)
   */
  public void sendEmbeddedPageView(String externalId, ReportingGroup reportingGroup) {
    String productId = externalIdToProductId(externalId);
    String categoryId = null;

    BVPageViewEvent event = new BVPageViewEvent(
        productId,
        bvProductType,
        categoryId);

    Map<String, Object> additionalParams = new HashMap<>();
    additionalParams.put(BVEventKeys.PageViewEvent.REPORTING_GROUP, reportingGroup.toString());

    event.setAdditionalParams(additionalParams);

    bvPixel.track(event);
  }

  public void sendUploadPhotoFeatureEvent(String externalId) {
    String productId = externalIdToProductId(externalId);
    String brand = null;

    BVFeatureUsedEvent event = new BVFeatureUsedEvent(
        productId,
        bvProductType,
        BVEventValues.BVFeatureUsedEventType.PHOTO,
        brand);

    bvPixel.track(event);
  }

  @NonNull
  private String externalIdToProductId(@Nullable String externalId) {
    return externalId != null && !externalId.isEmpty() ? externalId : "none";
  }
}
