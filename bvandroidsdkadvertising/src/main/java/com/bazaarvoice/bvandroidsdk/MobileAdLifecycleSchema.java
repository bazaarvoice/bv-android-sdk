/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

class MobileAdLifecycleSchema extends BvAnalyticsSchema {

    private static final String KEY_ERROR_MESSAGE = "errorMessage";
    private static final String KEY_CAMPAIGN_ID = "campaignId";
    private static final String KEY_CREATIVE_ID = "creativeId";
    private static final String eventClass = "Lifecycle";
    private static final String eventType = "MobileAd";
    private static final String source = "media";

    private MobileAdLifecycleSchema(Builder builder) {
        super(eventClass, eventType, source);
        addKeyVal(KEY_ERROR_MESSAGE, builder.errorMessage);
        addKeyVal(KEY_CAMPAIGN_ID, builder.campaignId);
        addKeyVal(KEY_CREATIVE_ID, builder.creativeId);

        addPartialSchema(builder.magpieMobileAppPartialSchema);
        addPartialSchema(builder.adLifecyclePartialSchema);
    }

    public static final class Builder {
        private String errorMessage;
        private String campaignId;
        private String creativeId;
        private MagpieMobileAppPartialSchema magpieMobileAppPartialSchema;
        private AdLifecyclePartialSchema adLifecyclePartialSchema;

        public Builder(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, AdLifecyclePartialSchema adLifecyclePartialSchema) {
            this.magpieMobileAppPartialSchema = magpieMobileAppPartialSchema;
            this.adLifecyclePartialSchema = adLifecyclePartialSchema;
        }

        public Builder errorMessage(String val) {
            errorMessage = val;
            return this;
        }

        public Builder campaignId(String val) {
            campaignId = val;
            return this;
        }

        public Builder creativeId(String val) {
            creativeId = val;
            return this;
        }

        public MobileAdLifecycleSchema build() {
            return new MobileAdLifecycleSchema(this);
        }
    }
}
