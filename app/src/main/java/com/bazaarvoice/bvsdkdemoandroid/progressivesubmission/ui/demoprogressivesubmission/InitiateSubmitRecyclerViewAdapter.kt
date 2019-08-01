package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.ui.demoprogressivesubmission

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bazaarvoice.bvandroidsdk.InitiateSubmitResponse
import com.bazaarvoice.bvsdkdemoandroid.DemoRouter
import com.bazaarvoice.bvsdkdemoandroid.R
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils

class InitiateSubmitRecyclerViewAdapter : RecyclerView.Adapter<IntiateSubmitRecyclerViewHolder>() {

    var products: List<InitiateSubmitResponse.InitiateSubmitResponseData> = emptyList()
        set(value) {
            this.notifyDataSetChanged()
            field = value
        }

    private lateinit var router: DemoRouter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntiateSubmitRecyclerViewHolder {
        router = DemoRouter(parent.context)
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_progressive_submission_init, parent, false)
        return IntiateSubmitRecyclerViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: IntiateSubmitRecyclerViewHolder, position: Int) {
        val submitResponseData = products[position]
        holder.itemView.setOnClickListener {
            router.transitionToProgressiveSubmissionActivity("Review", submitResponseData.review.productId)
        }
        holder.productId.text = submitResponseData.review.productId
        holder.productReview.text = submitResponseData.review.toString()
        holder.productReviewForm.text = DemoUtils.formResponseToString(submitResponseData)

    }

}

class IntiateSubmitRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val productId: TextView = itemView.findViewById(R.id.tvProductId)
    val productReview: TextView = itemView.findViewById(R.id.tvProductReviewSummary)
    val productReviewForm: TextView = itemView.findViewById(R.id.tvReviewForm)

}
