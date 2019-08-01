package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.ui.demoprogressivesubmission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bazaarvoice.bvandroidsdk.*
import com.bazaarvoice.bvsdkdemoandroid.DemoApp
import com.bazaarvoice.bvsdkdemoandroid.DemoUserTokenFactory
import com.bazaarvoice.bvsdkdemoandroid.R
import javax.inject.Inject

class DemoProgressiveInitiateSubmitFragment: Fragment() {

    companion object {
        fun newInstance() = DemoProgressiveInitiateSubmitFragment()
    }

    @Inject
    lateinit var bvConversationsClient: BVConversationsClient
    private lateinit var viewAdapter: InitiateSubmitRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DemoApp.getAppComponent(context).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.demo_progressive_submission_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewManager = LinearLayoutManager(context)
        val recyclerView = view.findViewById<RecyclerView>(R.id.multiproductRecyclerView)
        viewAdapter = InitiateSubmitRecyclerViewAdapter()
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val productIds = arrayOf("product1", "product2", "product3", "product4", "product5", "product6", "product7", "product8", "product9", "product10")
        val siteAuthenticationProvider = SiteAuthenticationProvider(DemoUserTokenFactory.generateMockUserToken())
        val initiateSubmitRequest = InitiateSubmitRequest.Builder(productIds.asList(),"en_US")
                .authenticationProvider(siteAuthenticationProvider)
                .build()
        bvConversationsClient.prepareCall(initiateSubmitRequest).loadAsync(object : ConversationsSubmissionCallback<InitiateSubmitResponse> {
            override fun onSuccess(response: InitiateSubmitResponse){
                Toast.makeText(context?.applicationContext, "Got product form data", Toast.LENGTH_SHORT).show()
                viewAdapter.products = response.data.productFormData.values.toList()
            }
            override fun onFailure(exception: ConversationsSubmissionException) {
                Toast.makeText(context?.applicationContext, "Failed to get Products: " + exception?.message, Toast.LENGTH_SHORT).show()
            }

        } )

    }

}
