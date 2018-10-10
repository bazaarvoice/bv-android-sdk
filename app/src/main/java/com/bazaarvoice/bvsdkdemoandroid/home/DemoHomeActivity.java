package com.bazaarvoice.bvsdkdemoandroid.home;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bazaarvoice.bvsdkdemoandroid.DemoActivityModule;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.DemoRouter;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.products.DemoProductsCarouselPresenter;
import com.bazaarvoice.bvsdkdemoandroid.products.DemoProductsCarouselPresenterModule;
import com.bazaarvoice.bvsdkdemoandroid.products.DemoProductsView;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoLaunchIntentUtil;

import javax.inject.Inject;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoHomeActivity extends AppCompatActivity {
  @Inject DemoProductsCarouselPresenter presenter;
  @Inject DemoClientConfigUtils demoClientConfigUtils;
  @Inject DemoRouter demoRouter;
  @Inject DemoClient demoClient;
  @BindView(R.id.productsView) DemoProductsView productsView;
  @BindView(R.id.productsHeaderText) TextView productsHeaderText;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    getSupportActionBar().setDisplayShowTitleEnabled(true);
    DaggerDemoHomeComponent.builder()
        .demoAppComponent(DemoApp.getAppComponent(this))
        .demoActivityModule(new DemoActivityModule(this))
        .demoProductsCarouselPresenterModule(new DemoProductsCarouselPresenterModule(productsView))
        .build()
        .inject(this);
    setHeaderText();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.toolbar_actions, menu);
    if (!demoClientConfigUtils.otherSourcesExist()) {
      menu.findItem(R.id.settings_action).setVisible(false);
    }
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.settings_action:
        demoRouter.transitionToSettings(DemoLaunchIntentUtil.LaunchCode.NEW);
        break;
      case R.id.cart_action:
        demoRouter.transitionToCart();
        break;
    }
    return false;
  }

  private void setHeaderText() {
    @StringRes int strRes = demoClient.hasShopperAds() ?
        R.string.section_header_recommendations :
        R.string.section_header_conversations;
    productsHeaderText.setText(getString(strRes));
  }
}
