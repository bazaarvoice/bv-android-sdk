package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.cache

import com.bazaarvoice.bvandroidsdk.FormField
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoCache

class DemoSubmissionFormCache: DemoCache<List<FormField>>() {
    override fun getKey(dataItem: List<FormField>?): String {
        //we'll always have a key so this isn't required
        return ""
    }

    companion object {
        var instance: DemoSubmissionFormCache = DemoSubmissionFormCache()

    }

    override fun shouldEvict(): Boolean {
        return false // Never auto-evict products cache for demo
    }

}