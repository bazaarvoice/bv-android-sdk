package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 4/7/16.
 */
public class CurationsAnalyticsManager {
    private static AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();

    /**
     * Event should be sent whenever a CurationsFeedItem appears on the screen. Should only be fired once per CurationsFeedItem.
     * @param curationsFeedItem CurationsFeedItem that has appeared on screen.
     */
    public static void sendUGCImpressionEvent(CurationsFeedItem curationsFeedItem){

        if (curationsFeedItem.impressed){
            return;
        }

        curationsFeedItem.impressed = true;

        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        CurationsImpressionSchema curationsImpressionSchema = new CurationsImpressionSchema(curationsFeedItem, magpieMobileAppPartialSchema);

        analyticsManager.enqueueEvent(curationsImpressionSchema.getDataMap());
    }

    /**
     * Event should be sent whenever a Curations ViewGroup appears on the screen. Should only be fired once per Curations ViewGroup.
     * @param externalId If an externalId was used in the CurationsFeedRequest when the Curations feed was loaded, it should be used in this event.
     * @param widgetId ID given to the Curations ViewGroup used to distinguish events.
     * @param reportingGroup The type of Curations ViewGroup used to present Curations feed (RecyclerView, ListView ...)
     */
    public static void sendBvViewGroupAddedToHierarchyEvent(String externalId, String widgetId, ReportingGroup reportingGroup){
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        CurationsUsedFeatureSchema schema = new CurationsUsedFeatureSchema(Feature.INVIEW, externalId, widgetId, reportingGroup, magpieMobileAppPartialSchema);
        analyticsManager.enqueueEvent(schema.getDataMap());
    }

    /**
     * Event should be sent whenever a CurationsView is tapped.
     * @param curationsFeedItem The CurationsFeedItem used to populate the CurationsView UI
     */
    public static void sendUsedFeatureEventTapped(CurationsFeedItem curationsFeedItem){
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        CurationsUsedFeatureSchema schema = new CurationsUsedFeatureSchema(Feature.CONTENT_CLICK, curationsFeedItem, magpieMobileAppPartialSchema);
        analyticsManager.enqueueEvent(schema.getDataMap());
    }

    /**
     * Should be sent when a ViewGroup displaying Curations feed is scrolled. Typically this is fired once on the first interaction with a UI container and when scrolling stops.
     * @param externalId If an externalId was used in the CurationsFeedRequest when the Curations feed was loaded, it should be used in this event.
     * @param reportingGroup The type of Curations ViewGroup used to present Curations feed (RecyclerView, ListView ...)
     */
    public static void sendUsedFeatureEventScrolled(String externalId, ReportingGroup reportingGroup){
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        CurationsUsedFeatureSchema schema = new CurationsUsedFeatureSchema(Feature.SCROLLED, externalId, null, reportingGroup, magpieMobileAppPartialSchema);
        analyticsManager.enqueueEvent(schema.getDataMap());
    }

    /**
     * Event should be sent whenever a Curations ViewGroup is created, but not necessarily displayed on screen.
     * @param externalId If an externalId was used in the CurationsFeedRequest when the Curations feed was loaded, it should be used in this event.
     * @param reportingGroup The type of Curations ViewGroup used to present Curations feed (RecyclerView, ListView ...)
     */
    public static void sendEmbeddedPageView(String externalId, ReportingGroup reportingGroup){
        BVSDK bvsdk = BVSDK.getInstance();
        AnalyticsManager analyticsManager = bvsdk.getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        CurationsEmbeddedPageViewSchema schema = new CurationsEmbeddedPageViewSchema(magpieMobileAppPartialSchema, externalId, reportingGroup);
        analyticsManager.enqueueEvent(schema.getDataMap());
    }
}
