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
 * Curation Impression Analytics Schema builder
 */
class CurationsImpressionSchema extends UgcImpressionCanonicalSchema {

    private static final String bvProduct = "Curations";
    private static final String source = "native-mobile-sdk";

    private static final String contentType = "socialPost";
    private static final String CHANNEL_KEY = "syndicationSource";

    public CurationsImpressionSchema(CurationsFeedItem item, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema){
        super(magpieMobileAppPartialSchema, item.externalIdInQuery, String.valueOf(item.id), contentType, bvProduct, source);
        addKeyVal(CHANNEL_KEY, item.channel);
    }
}
