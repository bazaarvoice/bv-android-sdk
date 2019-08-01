package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.ui.demoprogressivesubmission

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bazaarvoice.bvandroidsdk.*
import com.bazaarvoice.bvsdkdemoandroid.DemoApp
import com.bazaarvoice.bvsdkdemoandroid.DemoUserTokenFactory
import com.bazaarvoice.bvsdkdemoandroid.R
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoAssetsUtil
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils
import kotlinx.android.synthetic.main.demo_progressive_submission_review_fragment.*
import javax.inject.Inject

class DemoProgressiveSubmissionHandlerReviewFragment : Fragment(), FormSubmissionHandler {
    @Inject
    lateinit var bvConversationsClient: BVConversationsClient
    @Inject
    lateinit var demoAssetsUtil: DemoAssetsUtil
    private val submissionFields = HashMap<String, Any>()
    private lateinit var productId: String
    private var submissionSessionToken: String? = null

    companion object {
        fun newInstance(productId: String): DemoProgressiveSubmissionHandlerReviewFragment {
            val fragment = DemoProgressiveSubmissionHandlerReviewFragment()
            val arguments = Bundle()
            arguments.putString("PRODUCT_ID", productId)
            //todo get submissionSessionToken
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DemoApp.getAppComponent(context).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.demo_progressive_submission_review_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        formFields.formSubmissionHandler = this
        userRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            onReviewItemSubmitted("rating", rating.toInt().toString())
        }
        productId = arguments?.getString("PRODUCT_ID") ?: "product1"
        val productIds = listOf(productId)
        val siteAuthenticationProvider = SiteAuthenticationProvider(DemoUserTokenFactory.generateMockUserToken())
        val initiateSubmitRequest = InitiateSubmitRequest.Builder(productIds , "en_US")
                .authenticationProvider(siteAuthenticationProvider)
                .build()
        bvConversationsClient.prepareCall(initiateSubmitRequest).loadAsync(object : ConversationsSubmissionCallback<InitiateSubmitResponse> {
            override fun onSuccess(response: InitiateSubmitResponse) {
                Toast.makeText(context?.applicationContext, "Got Review Form", Toast.LENGTH_SHORT).show()
                val formData = DemoUtils.formResponseToString(response.data.productFormData[productId])
                review_form.text = formData
                for (formItem in response.data.productFormData[productId]?.formFields!!) {
                    formFields.setFormField(formItem)
                }
                submissionSessionToken = response.data.productFormData[productId]?.submissionSessionToken
            }

            override fun onFailure(exception: ConversationsSubmissionException) {
                Toast.makeText(context?.applicationContext, "Failed to get Product Form: " + exception?.message, Toast.LENGTH_SHORT).show()
            }

        })

        upload.setOnClickListener {
            //upload photo to photoUpload. Attach it to ReviewAndSubmit.
            val file = demoAssetsUtil.parseImageFileFromAssets("bv_sample_image.png")
            Log.d("CreatedFile", "Filed Create")
            val photoUpload = PhotoUpload(file, "Me and my purchase", PhotoUpload.ContentType.REVIEW)
            val photoUploadRequest = PhotoUploadRequest.Builder(photoUpload).build()
            bvConversationsClient.prepareCall(photoUploadRequest).loadAsync(object: ConversationsSubmissionCallback<PhotoUploadResponse>{
                override fun onSuccess(response: PhotoUploadResponse) {
                    Log.d("Got Photo", response.toString())
                    val photoUrl = response.photo.content.normalUrl
                    onReviewItemSubmitted("photourl_1", photoUrl)
                }

                override fun onFailure(exception: ConversationsSubmissionException) {
                    Toast.makeText(context?.applicationContext, "Photo Failed To Submit: ${exception?.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    override fun onReviewItemSubmitted(key: String, value: Any) {
        submissionFields[key] = value

        val siteAuthenticationProvider = SiteAuthenticationProvider(DemoUserTokenFactory.generateMockUserToken())
        val progressiveSubmissionReviewRequest = submissionSessionToken?.let {
            ProgressiveSubmitRequest.Builder( productId, it, "en_US")
                .submissionFields(submissionFields)
                .authenticationProvider(siteAuthenticationProvider)
                .agreedToTermsAndConditions(true)
                .build()
        }

        bvConversationsClient.prepareCall(progressiveSubmissionReviewRequest).loadAsync(object : ConversationsSubmissionCallback<ProgressiveSubmitResponse> {
            override fun onSuccess(response: ProgressiveSubmitResponse) {
                Toast.makeText(context?.applicationContext, "Review Submitted", Toast.LENGTH_SHORT).show()
                submissionSessionToken = response.data.submissionSessionToken
            }

            override fun onFailure(exception: ConversationsSubmissionException) {
                Toast.makeText(context?.applicationContext, "Review Failed To Submit: ${exception?.message}", Toast.LENGTH_SHORT).show()
                if (exception.fieldErrors != null && !exception.fieldErrors.isEmpty()) {
                    val message = StringBuilder()
                    exception.fieldErrors.forEach {
                        message.append("${it.field}: ${it.message}").append("\n")
                    }
                    AlertDialog.Builder(context).setTitle("Form Errors")
                            .setMessage(message)
                            .setPositiveButton("Ok") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create().show()

                }
            }

        })
    }

}

interface FormSubmissionHandler {
    fun onReviewItemSubmitted(key: String, value: Any)
}
