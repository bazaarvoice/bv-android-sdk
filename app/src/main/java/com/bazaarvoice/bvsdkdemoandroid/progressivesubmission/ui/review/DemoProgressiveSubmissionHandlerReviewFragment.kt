package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.ui.review

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bazaarvoice.bvandroidsdk.*
import com.bazaarvoice.bvsdkdemoandroid.DemoApp
import com.bazaarvoice.bvsdkdemoandroid.DemoUserTokenFactory
import com.bazaarvoice.bvsdkdemoandroid.R
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.cache.DemoSubmissionFormCache
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.persistance.DemoBVPersistableProductDatabase
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoAssetsUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import javax.inject.Inject

class DemoProgressiveSubmissionHandlerReviewFragment : Fragment(), FormSubmissionHandler, ConversationsSubmissionCallback<ProgressiveSubmitResponse> {

    @Inject
    lateinit var bvConversationsClient: BVConversationsClient
    @Inject
    lateinit var demoAssetsUtil: DemoAssetsUtil
    @Inject
    lateinit var demoBVPersistableProductDatabase: DemoBVPersistableProductDatabase

    private val databaseJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + databaseJob)
    private val submissionFields = HashMap<String, Any>()
    private lateinit var productId: String
    private var submissionSessionToken: String? = null
    private val photoUploadCallback = object : ConversationsSubmissionCallback<PhotoUploadResponse> {
        override fun onSuccess(response: PhotoUploadResponse) {
            Log.d("Got Photo", response.toString())
            val photoUrl = response.photo.content.normalUrl
            onReviewItemSubmitted("photourl_1", photoUrl)
        }

        override fun onFailure(exception: ConversationsSubmissionException) {
            Toast.makeText(context?.applicationContext, "Photo Failed To Submit: ${exception?.message}", Toast.LENGTH_SHORT).show()
        }

    }

    private val videoUploadCallback = object : ConversationsSubmissionCallback<VideoUploadResponse> {
        override fun onSuccess(response: VideoUploadResponse) {
            Log.d("Got Video", response.toString())
            val photoUrl = response.video.content.normalUrl
            onReviewItemSubmitted("photourl_1", photoUrl)
        }
        override fun onFailure(exception: ConversationsSubmissionException) {
            Toast.makeText(context?.applicationContext, "Photo Failed To Submit: ${exception?.message}", Toast.LENGTH_SHORT).show()
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
        val formFieldsView = view.findViewById<ProgressiveSubmissionFormItemView>(R.id.formFieldsView)
        val userRating = view.findViewById<RatingBar>(R.id.userRating)
        val upload = view.findViewById<Button>(R.id.upload)
        val uploadVideo = view.findViewById<Button>(R.id.upload_video)

        val buttonComplete = view.findViewById<FloatingActionButton>(R.id.button_complete)

        formFieldsView.formSubmissionHandler = this
        userRating.setOnRatingBarChangeListener { _, rating, _ ->
            onReviewItemSubmitted("rating", rating.toInt().toString())
        }
        productId = arguments?.getString("productId") ?: "product1"
        submissionSessionToken = arguments?.getString("submissionSessionToken")
        val formFields = DemoSubmissionFormCache.instance.getDataItem(productId.toLowerCase())
        for (formItem in formFields) {
            formFieldsView.setFormField(formItem)
        }

        upload.setOnClickListener {
            //upload photo to photoUpload. Attach it to ReviewAndSubmit.
            val file = demoAssetsUtil.parseImageFileFromAssets("bv_sample_image.png")
            Log.d("CreatedFile", "Filed Create")
            val photoUpload = PhotoUpload(file, "Me and my purchase", PhotoUpload.ContentType.REVIEW)
            val photoUploadRequest = PhotoUploadRequest.Builder(photoUpload).build()
            bvConversationsClient.prepareCall(photoUploadRequest).loadAsync(photoUploadCallback)
        }
        uploadVideo.setOnClickListener {
            //upload video to photoVideo. Attach it to ReviewAndSubmit.
            val file = demoAssetsUtil.parseImageFileFromAssets("bv_sample_image.png")
            Log.d("CreatedFile", "Filed Create")
            val videoUpload = VideoUpload(file, "Me and my purchase", VideoUpload.ContentType.REVIEW)
            val videoUploadRequest = VideoUploadRequest.Builder(videoUpload).build()
            bvConversationsClient.prepareCall(videoUploadRequest).loadAsync(videoUploadCallback)
        }

        buttonComplete.setOnClickListener {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    demoBVPersistableProductDatabase.demoBVPersistableProductContentDao().delete(productId)
                    findNavController().navigate(R.id.action_demoProgressiveSubmissionHandlerReviewFragment_to_demoReviewableProductsFragment)
                }
            }
        }
    }

    override fun onReviewItemSubmitted(key: String, value: Any) {
        submissionFields[key] = value
        submissionSessionToken?.let {
            val progressiveSubmissionReviewRequest = buildProgressiveSubmissionRequest(it)
            bvConversationsClient.prepareCall(progressiveSubmissionReviewRequest)
                    .loadAsync(this)
        }

    }

    private fun buildProgressiveSubmissionRequest(submissionSessionToken: String): ProgressiveSubmitRequest {
        val siteAuthenticationProvider = SiteAuthenticationProvider(DemoUserTokenFactory.generateMockUserToken())
        return ProgressiveSubmitRequest.Builder(productId, submissionSessionToken, "en_US")
                .submissionFields(submissionFields)
                .authenticationProvider(siteAuthenticationProvider)
                .agreedToTermsAndConditions(true)
                .build()
    }

    override fun onSuccess(response: ProgressiveSubmitResponse) {
        Toast.makeText(context?.applicationContext, "Review Submitted", Toast.LENGTH_SHORT).show()
        submissionSessionToken = response.data.submissionSessionToken
    }


    override fun onFailure(exception: ConversationsSubmissionException) {
        Toast.makeText(context?.applicationContext, "Review Failed To Submit: ${exception?.message}", Toast.LENGTH_SHORT).show()
        if (exception.fieldErrors != null && exception.fieldErrors.isNotEmpty()) {
            val message = StringBuilder()
            exception.fieldErrors.forEach {
                message.append("${it.field}: ${it.message}").append("\n")
            }
            showErrorDialog(message.toString())
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(context).setTitle("Form Errors")
                .setMessage(message)
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
                .create().show()
    }

    override fun onDetach() {
        super.onDetach()
        databaseJob.cancel()
    }
}

interface FormSubmissionHandler {
    fun onReviewItemSubmitted(key: String, value: Any)
}
