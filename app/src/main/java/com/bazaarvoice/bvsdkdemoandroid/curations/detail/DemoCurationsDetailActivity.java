package com.bazaarvoice.bvsdkdemoandroid.curations.detail;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DemoCurationsDetailActivity extends AppCompatActivity {

    public static final String CURRATIONS_UPDATE_KEY = "currationsupdatebundlekey";
    public static final String CURRATIONS_UPDATE_IDX_KEY = "currationsupdateidxbundlekey";

    private ViewPager viewPager;
    private DemoCurationsDetailPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curations_detail);
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<CurationsFeedItem>>() {
        }.getType();

        List<CurationsFeedItem> curationsFeedItems = gson.fromJson(getIntent().getStringExtra(CURRATIONS_UPDATE_KEY), listType);

        viewPager = (ViewPager) findViewById(R.id.curations_view_pager);
        pagerAdapter = new DemoCurationsDetailPagerAdapter(getSupportFragmentManager(), curationsFeedItems);
        viewPager.setAdapter(pagerAdapter);

        int currentUpdateIdx = getIntent().getIntExtra(CURRATIONS_UPDATE_IDX_KEY, 0);
        viewPager.setCurrentItem(currentUpdateIdx, false);
    }

    public void goBackUpdate(){
        viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
    }

    public void goForwardUpdate(){
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
    }

}
