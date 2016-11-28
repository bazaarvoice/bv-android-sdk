/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * Implemented by a Bazaarvoice View extending {@link android.widget.FrameLayout}
 * that will contain some piece of UGC, Recommended Product,
 * Curations Feed Item, etc. that can be tied to these specific
 * actions.
 */
interface BVViewEventListener {
    /**
     * When this View is tapped
     */
    void onTap();

    /**
     * When this View added to the current View Hierarchy, but
     * may or may not be in the viewport
     */
    void onAddedToViewHierarchy();
}
