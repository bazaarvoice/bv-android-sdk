package com.bazaarvoice.bvsdkdemoandroid.products;

import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoCache;

public class DemoDisplayableProductsCache extends DemoCache<BVDisplayableProductContent> {
  private static DemoDisplayableProductsCache instance;

  private DemoDisplayableProductsCache() {
    super();
  }

  public static DemoDisplayableProductsCache getInstance() {
    if (instance == null) {
      instance = new DemoDisplayableProductsCache();
    }
    return instance;
  }

  @Override
  protected boolean shouldEvict() {
    return false; // Never auto-evict products cache for demo
  }

  @Override
  protected String getKey(BVDisplayableProductContent productContent) {
    return productContent.getId();
  }

}
