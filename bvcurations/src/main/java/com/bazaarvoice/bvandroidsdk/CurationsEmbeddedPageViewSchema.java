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
 * Analytic event for a Curations PageView
 */
class CurationsEmbeddedPageViewSchema extends EmbeddedPageViewSchema {

    private static final String bvProduct = "Curations";
    private static final String source = "native-mobile-sdk";
    private static final String productId = "productId";

    public CurationsEmbeddedPageViewSchema(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, String externalId, ReportingGroup reportingGroup)
    {
        super(magpieMobileAppPartialSchema, reportingGroup, bvProduct, source);
        addKeyVal(productId, externalId);
    }
}
