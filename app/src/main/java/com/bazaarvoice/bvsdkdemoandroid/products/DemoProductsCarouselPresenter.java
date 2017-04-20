package com.bazaarvoice.bvsdkdemoandroid.products;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.BVRecommendations;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.BulkProductRequest;
import com.bazaarvoice.bvandroidsdk.BulkProductResponse;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.EqualityOperator;
import com.bazaarvoice.bvandroidsdk.ProductOptions;
import com.bazaarvoice.bvandroidsdk.RecommendationsRequest;
import com.bazaarvoice.bvandroidsdk.ReviewOptions;
import com.bazaarvoice.bvandroidsdk.SortOrder;
import com.bazaarvoice.bvsdkdemoandroid.DemoRouter;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class DemoProductsCarouselPresenter implements DemoProductsContract.Presenter {
  private final DemoProductsContract.View view;
  private final DemoClient demoClient;
  private final BVRecommendations bvRecommendations;
  private final BVConversationsClient bvConversationsClient;
  private final DemoRouter demoRouter;

  @Inject
  public DemoProductsCarouselPresenter(
      @Named("ProductsCarouselView") DemoProductsContract.View view,
      DemoClient demoClient,
      BVRecommendations bvRecommendations,
      BVConversationsClient bvConversationsClient,
      DemoRouter demoRouter) {
    this.view = view;
    this.demoClient = demoClient;
    this.bvRecommendations = bvRecommendations;
    this.bvConversationsClient = bvConversationsClient;
    this.demoRouter = demoRouter;
    view.setOnItemClickListener(onItemClickListener);
  }

  private final DemoProductsContract.OnItemClickListener onItemClickListener = new DemoProductsContract.OnItemClickListener() {
    @Override
    public void onItemClicked(BVDisplayableProductContent productContent) {
      demoRouter.transitionToProductDetail(productContent.getId());
    }
  };

  @Override
  public void start() {
    view.showLoading(true);
    if (demoClient.hasShopperAds() || demoClient.isMockClient()) {
      getRecommendedProducts();
    } else {
      getConversationsProducts();
    }
  }

  private void getRecommendedProducts() {
    RecommendationsRequest request = new RecommendationsRequest.Builder(20)
        .build();
    bvRecommendations.getRecommendedProducts(request, recommendationsCallback);
  }

  private final BVRecommendations.BVRecommendationsCallback recommendationsCallback = new BVRecommendations.BVRecommendationsCallback() {
    @Override
    public void onSuccess(List<BVProduct> recommendedProducts) {
      DemoProductsCarouselPresenter.this.onSuccess(recommendedProducts);
    }

    @Override
    public void onFailure(Throwable throwable) {
      DemoProductsCarouselPresenter.this.onFailure(throwable);
    }
  };

  private void getConversationsProducts() {
    BulkProductRequest request = new BulkProductRequest.Builder()
        .addReviewSort(ReviewOptions.Sort.Rating, SortOrder.DESC)
        .addAdditionalField("Sort", "AverageOverallRating:desc")
        .addAdditionalField("Filter", "IsActive:eq:true")
        .addAdditionalField("Filter", "IsDisabled:eq:false")
        .addAdditionalField("Limit", "20")
        .addFilter(ProductOptions.Filter.TotalReviewCount, EqualityOperator.GTE, "35")
        .addFilter(ProductOptions.Filter.IsActive, EqualityOperator.EQ, "true")
        .addFilter(ProductOptions.Filter.IsDisabled, EqualityOperator.EQ, "false")
        .build();
    bvConversationsClient.prepareCall(request).loadAsync(conversationsCallback);
  }

  private final ConversationsCallback<BulkProductResponse> conversationsCallback = new ConversationsCallback<BulkProductResponse>() {
    @Override
    public void onSuccess(BulkProductResponse response) {
      if (response.getHasErrors()) {
        DemoProductsCarouselPresenter.this.onFailure(new IllegalStateException("PDP request has errors"));
      } else {
        DemoProductsCarouselPresenter.this.onSuccess(response.getResults());
      }
    }

    @Override
    public void onFailure(BazaarException exception) {
      DemoProductsCarouselPresenter.this.onFailure(exception);
    }
  };

  @SuppressWarnings("unchecked")
  private <ProductType extends BVDisplayableProductContent> void onSuccess(List<ProductType> products) {
    view.showLoading(false);

    if (products == null || products.size() == 0) {
      view.showEmpty(true);
      view.showEmptyMessage("No products found");
      return;
    }

    DemoDisplayableProductsCache.getInstance().putData((List<BVDisplayableProductContent>) products);
    view.updateContent(products);
  }

  private void onFailure(Throwable throwable) {
    view.showLoading(false);

    view.showEmpty(true);
    view.showEmptyMessage("Failed to get products");
    throwable.printStackTrace();
  }

  @Inject
  void setupListeners() {
    view.setPresenter(this);
  }
}
