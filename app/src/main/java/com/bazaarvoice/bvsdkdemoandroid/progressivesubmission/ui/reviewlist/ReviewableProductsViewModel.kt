package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.ui.reviewlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bazaarvoice.bvandroidsdk.*
import com.bazaarvoice.bvsdkdemoandroid.DemoUserTokenFactory
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.cache.DemoSubmissionFormCache
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.persistance.DemoBVPersistableProductContent
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.persistance.DemoBVPersistableProductDao
import kotlinx.coroutines.*

class ReviewableProductsViewModel(private val demoApiProductId: String?,
                                  private val bvConversationsClient: BVConversationsClient,
                                  private val dao: DemoBVPersistableProductDao) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _products = MutableLiveData<List<DemoBVPersistableProductContent>>()
    val products: LiveData<List<DemoBVPersistableProductContent>>
        get() = _products

    private val _initiateProgressiveSubmissionResponse = MutableLiveData<InitiateSubmitResponse>()
    val initiateSubmitResponse: LiveData<InitiateSubmitResponse>
        get() = _initiateProgressiveSubmissionResponse

    fun fetchProductDetailInfo() {

        if (demoApiProductId != null && demoApiProductId.isNotEmpty()) {
            loadProductDataFromApi(demoApiProductId)
        } else {
            loadProductDataFromLocal()
        }

    }

    private fun initializeProgressionSubmission(productIds: List<String>) {
        val siteAuthenticationProvider = SiteAuthenticationProvider(DemoUserTokenFactory.generateMockUserToken())
        _products.value?.let {
            val request = InitiateSubmitRequest.Builder(productIds, "en_US")
                    .authenticationProvider(siteAuthenticationProvider)
                    .build()

            bvConversationsClient.prepareCall(request).loadAsync(object : ConversationsSubmissionCallback<InitiateSubmitResponse> {
                override fun onSuccess(response: InitiateSubmitResponse) {
                    _initiateProgressiveSubmissionResponse.value = response
                    response.data.productFormData.forEach {
                        DemoSubmissionFormCache.instance.putDataItem(it.key, it.value.formFields)
                    }
                }

                override fun onFailure(exception: ConversationsSubmissionException) {
                    Log.e("progressiveViewModel", "error ${exception.cause}")
                }

            })
        }

    }

    private fun loadProductDataFromApi(demoApiProductId: String) {
        val request = ProductDisplayPageRequest.Builder(demoApiProductId).build()
        bvConversationsClient.prepareCall(request).loadAsync(object : ConversationsDisplayCallback<ProductDisplayPageResponse> {
            override fun onSuccess(response: ProductDisplayPageResponse) {
                Log.d("progressiveViewModel", "Success")
                val product = response.results[0]
                val productDetails = DemoBVPersistableProductContent(
                        product.id,
                        product.displayName,
                        product.displayImageUrl,
                        product.averageRating
                )
                _products.value = listOf(productDetails)
                initializeProgressionSubmission(arrayListOf(product.id))
            }

            override fun onFailure(exception: ConversationsException) {
                Log.e("progressiveViewModel", "error ${exception.cause}")
            }

        })
    }

    private fun loadProductDataFromLocal() {
        uiScope.launch {
            var ids = emptyList<String>()
            var products = emptyList<DemoBVPersistableProductContent>()
            withContext(Dispatchers.IO) {
                products = dao.allProducts
                ids = products.let { productContent ->
                    productContent.map { it.id }
                }
            }

            _products.value = products
            initializeProgressionSubmission(ids)
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
