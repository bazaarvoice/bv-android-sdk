package com.bazaarvoice.bvsdkdemoandroid.products;

import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvsdkdemoandroid.mvp.DemoBasePresenter;
import com.bazaarvoice.bvsdkdemoandroid.mvp.DemoBaseView;

import java.util.List;

public interface DemoProductsContract {
  interface View extends DemoBaseView<DemoProductsContract.Presenter> {
    <ProductType extends BVDisplayableProductContent> void updateContent(List<ProductType> contentList);
    void showEmpty(boolean showing);
    void showEmptyMessage(String message);
    void showLoading(boolean showing);
    void setOnItemClickListener(DemoProductsContract.OnItemClickListener onItemClickListener);
    void showProductTapped(String name);
  }

  interface Presenter extends DemoBasePresenter {

  }

  interface OnItemClickListener {
    void onItemClicked(BVDisplayableProductContent productContent);
  }
}
