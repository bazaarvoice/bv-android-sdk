package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.bazaarvoice.bvsdkdemoandroid.R

class DemoProgressiveSubmissionActivity : AppCompatActivity() {

    companion object {
         const val PRODUCT_ID_KEY = "PRODUCT_ID"

        @JvmStatic
        fun transitionTo(context: Context, productId: String?) {
            val intent = Intent(context, DemoProgressiveSubmissionActivity::class.java)
            intent.putExtra(PRODUCT_ID_KEY, productId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_progressive_submission_activity)
        val productId = intent.getStringExtra(PRODUCT_ID_KEY) ?: ""
        val bundle = Bundle()
        bundle.putString(PRODUCT_ID_KEY, productId)
        findNavController(R.id.nav_host_fragment)
                .setGraph(R.navigation.navigation_progressivesubmission, bundle)
    }


}


