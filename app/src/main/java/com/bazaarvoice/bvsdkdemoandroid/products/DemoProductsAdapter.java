package com.bazaarvoice.bvsdkdemoandroid.products;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final Picasso picasso;
  private List<BVDisplayableProductContent> products = Collections.emptyList();
  private DemoProductsContract.OnItemClickListener onItemClickListener;

  public DemoProductsAdapter(Picasso picasso) {
    this.picasso = picasso;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View productView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_thumbnail, parent, false);
    RecyclerView.ViewHolder viewHolder = new ProductViewHolder(productView);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    BVDisplayableProductContent product = products.get(position);
    ProductViewHolder productViewHolder = (ProductViewHolder) holder;
    bindProduct(product, productViewHolder);
  }

  private void bindProduct(final BVDisplayableProductContent product, final ProductViewHolder productViewHolder) {
    picasso.load(product.getDisplayImageUrl())
        .error(R.drawable.ic_error_outline_black_24dp)
        .centerInside()
        .resizeDimen(R.dimen.snippet_4_3_width, R.dimen.snippet_4_3_height)
        .into(productViewHolder.productThumbnailImage);
    productViewHolder.productThumbnailTitle.setText(product.getDisplayName());
    productViewHolder.productThumbnailRating.setRating(product.getAverageRating());
    if (onItemClickListener != null) {
      productViewHolder.productContainer.setOnClickListener(v -> onItemClickListener.onItemClicked(product));
    }
  }

  @Override
  public int getItemCount() {
    return products.size();
  }

  public void setOnItemClickListener(DemoProductsContract.OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  @SuppressWarnings("unchecked")
  public <ProductType extends BVDisplayableProductContent> void updateContent(List<ProductType> products) {
    this.products = (List<BVDisplayableProductContent>) products;
    notifyDataSetChanged();
  }

  static class ProductViewHolder extends RecyclerView.ViewHolder {
    final ViewGroup productContainer;
    @BindView(R.id.productThumbnailImage) ImageView productThumbnailImage;
    @BindView(R.id.productThumbnailTitle) TextView productThumbnailTitle;
    @BindView(R.id.productThumbnailRating) RatingBar productThumbnailRating;

    public ProductViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      productContainer = (ViewGroup) view;
    }
  }
}
