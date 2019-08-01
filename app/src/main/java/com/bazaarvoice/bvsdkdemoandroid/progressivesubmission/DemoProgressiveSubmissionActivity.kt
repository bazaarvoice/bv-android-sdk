package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bazaarvoice.bvsdkdemoandroid.R
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.ui.demoprogressivesubmission.DemoProgressiveInitiateSubmitFragment
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.ui.demoprogressivesubmission.DemoProgressiveSubmissionHandlerReviewFragment

class DemoProgressiveSubmissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_progressive_submission_activity)
        val type = intent?.extras?.getString(TYPE_KEY)
        val productId = intent?.extras?.getString(PRODUCT_ID_KEY) ?: "product1"
        if (savedInstanceState == null) {
            if (type == "initiate") {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, DemoProgressiveInitiateSubmitFragment.newInstance())
                        .commitNow()
            } else {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, DemoProgressiveSubmissionHandlerReviewFragment.newInstance(productId))
                        .commitNow()
            }
        }
    }

    companion object {
        private const val TYPE_KEY = "TYPE"
        private const val PRODUCT_ID_KEY = "PRODUCT_ID"

        @JvmStatic
        fun transitionTo(context: Context, type: String, productId: String?) {
            val intent = Intent(context, DemoProgressiveSubmissionActivity::class.java)
            intent.putExtra(TYPE_KEY, type)
            intent.putExtra(PRODUCT_ID_KEY, productId)
            context.startActivity(intent)
        }
    }

}
