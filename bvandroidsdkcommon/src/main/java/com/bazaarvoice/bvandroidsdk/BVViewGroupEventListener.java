/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

interface BVViewGroupEventListener extends BVViewEventListener {
    /**
     * Implemented by Parent ViewGroup Objects to notify
     * Children inheriting this interface when the user
     * has scrolled this specific ViewGroup
     */
    void onViewGroupInteractedWith();
}
