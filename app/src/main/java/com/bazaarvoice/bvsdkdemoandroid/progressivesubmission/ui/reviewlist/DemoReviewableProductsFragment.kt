package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.ui.reviewlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bazaarvoice.bvandroidsdk.BVConversationsClient
import com.bazaarvoice.bvandroidsdk.InitiateSubmitResponse
import com.bazaarvoice.bvsdkdemoandroid.DemoApp
import com.bazaarvoice.bvsdkdemoandroid.R
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.DemoProgressiveSubmissionActivity.Companion.PRODUCT_ID_KEY
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.persistance.DemoBVPersistableProductDatabase
import javax.inject.Inject

class DemoReviewableProductsFragment : Fragment(), ProgressiveSubmissionProductClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ReviewableProductsAdapter
    private var initiateSubmitResponse: InitiateSubmitResponse? = null
    @Inject
    lateinit var bvConversationsClient: BVConversationsClient
    @Inject
    lateinit var demoBVPersistableProductDatabase: DemoBVPersistableProductDatabase
    var apiDemoProductId = ""

    companion object {
        fun newInstance() = DemoReviewableProductsFragment()
    }

    private lateinit var viewModel: ReviewableProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiDemoProductId = arguments?.getString(PRODUCT_ID_KEY,"") ?: ""

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.reviewable_products_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DemoApp.getAppComponent(context!!).inject(this)

        val factory = ReviewableProductsViewModelFactory(apiDemoProductId, bvConversationsClient, demoBVPersistableProductDatabase.demoBVPersistableProductContentDao())

        viewAdapter = ReviewableProductsAdapter(this)

        recyclerView = view.findViewById(R.id.reviewable_products_list)
        recyclerView.adapter = viewAdapter

        viewModel = ViewModelProvider(this, factory).get(ReviewableProductsViewModel::class.java)

        viewModel.products.observe(viewLifecycleOwner, Observer {
            viewAdapter.items = it
            viewAdapter.notifyDataSetChanged()
        })

        viewModel.initiateSubmitResponse.observe(viewLifecycleOwner, Observer {
            initiateSubmitResponse = it
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchProductDetailInfo()
    }

    override fun onClick(view: View) {
        val position = recyclerView.getChildAdapterPosition(view)
        val productId = viewAdapter.items[position].id
        val initiateData = initiateSubmitResponse?.data?.productFormData?.get(productId.toLowerCase())
        val bundle = Bundle()
        bundle.putString("productId", productId)
        initiateData?.let {
            bundle.putString("submissionSessionToken", it.submissionSessionToken)
        }
        findNavController().navigate(R.id.action_demoReviewableProductsFragment_to_demoProgressiveSubmissionHandlerReviewFragment,
                bundle)
    }


}
