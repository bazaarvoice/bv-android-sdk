package com.bazaarvoice.bvsdkdemoandroid.products;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.BVRecommendations;
import com.bazaarvoice.bvandroidsdk.BVRecommendationsResponse;
import com.bazaarvoice.bvandroidsdk.BulkProductRequest;
import com.bazaarvoice.bvandroidsdk.BulkProductResponse;
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsException;
import com.bazaarvoice.bvandroidsdk.EqualityOperator;
import com.bazaarvoice.bvandroidsdk.PDPContentType;
import com.bazaarvoice.bvandroidsdk.PageType;
import com.bazaarvoice.bvandroidsdk.ProductOptions;
import com.bazaarvoice.bvandroidsdk.RecommendationsRequest;
import com.bazaarvoice.bvandroidsdk.ReviewOptions;
import com.bazaarvoice.bvandroidsdk.SortOrder;
import com.bazaarvoice.bvsdkdemoandroid.DemoRouter;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.NonNull;

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
            .pageType(PageType.PRODUCT)
        .build();
    bvRecommendations.getRecommendedProducts(request, recommendationsCallback);
  }

  private final BVRecommendations.BVRecommendationsCallback recommendationsCallback = new BVRecommendations.BVRecommendationsCallback() {
    @Override
    public void onSuccess(BVRecommendationsResponse response) {
      DemoProductsCarouselPresenter.this.onSuccess(response.getRecommendedProducts());
    }

    @Override
    public void onFailure(Throwable throwable) {
      DemoProductsCarouselPresenter.this.onFailure(throwable);
    }
  };

  private void getConversationsProducts() {
    BulkProductRequest request = new BulkProductRequest.Builder()
        .addSort(ProductOptions.Sort.Id, SortOrder.ASC)
        .addReviewSort(ReviewOptions.Sort.Rating, SortOrder.DESC)
        .addCustomDisplayParameter("Sort", "AverageOverallRating:desc")
        .addCustomDisplayParameter("Filter", "IsActive:eq:true")
        .addCustomDisplayParameter("Filter", "IsDisabled:eq:false")
        .addCustomDisplayParameter("Limit", "20")
        .addFilter(ProductOptions.Filter.IsActive, EqualityOperator.EQ, "true")
        .addFilter(ProductOptions.Filter.IsDisabled, EqualityOperator.EQ, "false")
        .addIncludeStatistics(PDPContentType.Reviews)
        .build();
    bvConversationsClient.prepareCall(request).loadAsync(bulkProductCallback);
  }

  private final ConversationsDisplayCallback<BulkProductResponse> bulkProductCallback = new ConversationsDisplayCallback<BulkProductResponse>() {
    @Override
    public void onSuccess(@NonNull BulkProductResponse response) {
      DemoProductsCarouselPresenter.this.onSuccess(response.getResults());
    }

    @Override
    public void onFailure(@NonNull ConversationsException exception) {
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
