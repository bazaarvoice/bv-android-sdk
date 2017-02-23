/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.mapPutSafe;

/**
 * @deprecated TODO remove after full BVPixel swap is complete
 */
class ProfileCommonPartialSchema extends BvPartialSchema {

    private static final String KEY_PROFILE_ID = "profileId";
    private static final String KEY_BV_PRODUCT = "bvProduct";

    private static final String bvProduct = "ShopperMarketing";

    private String profileId;

    public ProfileCommonPartialSchema(String profileId) {
        this.profileId = profileId;
    }

    @Override
    public void addPartialData(Map<String, Object> dataMap) {
        mapPutSafe(dataMap, KEY_PROFILE_ID, profileId);
        mapPutSafe(dataMap, KEY_BV_PRODUCT, bvProduct);
    }
}
