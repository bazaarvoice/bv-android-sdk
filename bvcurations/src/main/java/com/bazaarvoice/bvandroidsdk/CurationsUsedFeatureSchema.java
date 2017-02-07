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

/**
 * Curation UsedFeature Analytics Schema builder
 */
class CurationsUsedFeatureSchema extends UsedFeatureCanonicalSchema {

    private static final String source = "native-mobile-sdk";
    private static final String bvProduct = "Curations";
    private static final String CONTENT_ID_KEY = "detail2";

    public CurationsUsedFeatureSchema(Feature feature, String externalId, String widgetId, ReportingGroup reportingGroup, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema){
        super(source, feature.toString(), bvProduct);
        addComponent(reportingGroup.toString());
        addProductId(externalId);
        addDetail1(widgetId);
    }

    public CurationsUsedFeatureSchema(Feature feature, CurationsFeedItem item, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema){
        super(source, feature.toString(), bvProduct);
        addProductId(item.externalIdInQuery);
        addDetail1(item.channel);
        addKeyVal(CONTENT_ID_KEY, item.id);
    }
}
