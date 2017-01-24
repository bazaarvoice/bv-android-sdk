package com.bazaarvoice.bvsdkdemoandroid.curations.detail;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoDataUtil;

import java.util.List;

import javax.inject.Inject;

public class DemoCurationsDetailActivity extends AppCompatActivity implements DemoCurationsDetailContract.View {

    public static final String KEY_PRODUCT_ITEM = "key_product_id";
    public static final String KEY_CURATIONS_ITEM_ID = "key_curations_item_id";

    private ViewPager viewPager;
    private DemoCurationsDetailPagerAdapter pagerAdapter;
    private DemoCurationsDetailContract.UserActionsListener userActionsListener;

    @Inject DemoConfigUtils demoConfigUtils;
    @Inject DemoDataUtil demoDataUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curations_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DemoApp.get(this).getAppComponent().inject(this);
        String productId = getIntent().getStringExtra(KEY_PRODUCT_ITEM);
        String startCurationsItemId = getIntent().getStringExtra(KEY_CURATIONS_ITEM_ID);
        userActionsListener = new DemoCurationsDetailPresenter(this, demoConfigUtils, demoDataUtil, productId, startCurationsItemId);
        viewPager = (ViewPager) findViewById(R.id.curations_view_pager);
        pagerAdapter = new DemoCurationsDetailPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userActionsListener.loadCurationsFeed();
    }

    @Override
    public void showCurationsFeed(List<CurationsFeedItem> curationsFeedItems) {
        pagerAdapter.setCurationsFeedItems(curationsFeedItems);
    }

    @Override
    public void showCurationsFeedItemInPager(int index) {
        viewPager.setCurrentItem(index, true);
    }

    public void goBackUpdate() {
        viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
    }

    public void goForwardUpdate() {
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
    }

    @Override
    public void showNoCurations() {
        Toast.makeText(this, getString(R.string.no_curations_found), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingCurations(boolean isLoading) {
        // TODO: Implement better loading/empty screens for this Activity
    }

    @Override
    public void showCurationsMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
