/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

/**
 * @deprecated TODO remove after full BVPixel swap is complete
 *
 * Base class for Bazaarvoice Analytics Partial Schemas
 */
abstract class BvPartialSchema {

    public abstract void addPartialData(Map<String, Object> dataMap);

}
