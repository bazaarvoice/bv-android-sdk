package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.ui.reviewlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bazaarvoice.bvsdkdemoandroid.R
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.persistance.DemoBVPersistableProductContent
import com.squareup.picasso.Picasso

class ReviewableProductsAdapter(private val listener: ProgressiveSubmissionProductClickListener) : RecyclerView.Adapter<ReviewableProductsViewHolder>() {

    var items = emptyList<DemoBVPersistableProductContent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewableProductsViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.reviewable_product_item, parent, false)
        layout.setOnClickListener {
            listener.onClick(it)
        }
        return ReviewableProductsViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ReviewableProductsViewHolder, position: Int) {
        val product = items[position]
        holder.productName.text = product.displayName
        holder.ratingBar.rating = product.averageRating
        Picasso.get()
                .load(product.displayImageUrl)
                .placeholder(R.drawable.placeholderimg)
                .into(holder.productImage)
    }
}

class ReviewableProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val productName: TextView = itemView.findViewById(R.id.product_name_text)
    val productImage: ImageView = itemView.findViewById(R.id.product_image)
    val ratingBar: RatingBar = itemView.findViewById(R.id.product_rating)

}

interface ProgressiveSubmissionProductClickListener {
    fun onClick(view: View)
}