/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

class ProfileMobilePersonalizationSchema extends BvAnalyticsSchema {

    private static final String eventClass = "Personalization";
    private static final String eventType = "ProfileMobile";
    private static final String source = "ProfileMobile";

    public ProfileMobilePersonalizationSchema(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, ProfileCommonPartialSchema profileCommonPartialSchema) {
        super(eventClass, eventType, source);
        addPartialSchema(magpieMobileAppPartialSchema);
        addPartialSchema(profileCommonPartialSchema);
    }

}
