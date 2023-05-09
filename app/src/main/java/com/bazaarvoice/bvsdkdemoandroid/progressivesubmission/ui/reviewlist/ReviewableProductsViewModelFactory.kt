package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.ui.reviewlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bazaarvoice.bvandroidsdk.BVConversationsClient
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.persistance.DemoBVPersistableProductDao


class ReviewableProductsViewModelFactory(private val demoApiProductId: String?, private val bvConversationsClient: BVConversationsClient,
                                         private val demoBVPersistableProductDao: DemoBVPersistableProductDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewableProductsViewModel::class.java)) {
            return ReviewableProductsViewModel(demoApiProductId, bvConversationsClient, demoBVPersistableProductDao) as T
        } else {
            throw IllegalArgumentException("Invalid Type Provided")
        }
    }
}