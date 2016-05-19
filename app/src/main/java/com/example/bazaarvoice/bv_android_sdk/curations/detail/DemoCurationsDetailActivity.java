package com.example.bazaarvoice.bv_android_sdk.curations.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DemoCurationsDetailActivity extends AppCompatActivity {

    public static final String CURRATIONS_UPDATE_KEY = "currationsupdatebundlekey";
    public static final String CURRATIONS_UPDATE_IDX_KEY = "currationsupdateidxbundlekey";
    private final int fragContainerId = R.id.curations_main_content;
    private int currentUpdateIdx = 0;

    private List<CurationsFeedItem> updates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curations_detail);
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<CurationsFeedItem>>() {
        }.getType();
        updates = gson.fromJson(getIntent().getStringExtra(CURRATIONS_UPDATE_KEY), listType);
        currentUpdateIdx = getIntent().getIntExtra(CURRATIONS_UPDATE_IDX_KEY,0);
        transitionToUpdate();
    }

    private void transitionToUpdate() {
        CurationsFeedItem curationsUpdate = updates.get(currentUpdateIdx);
        Fragment fragment = DemoCurationsDetailFragment.newInstance(curationsUpdate, (currentUpdateIdx != updates.size() -1)? currentUpdateIdx : Integer.MAX_VALUE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragContainerId, fragment);
        transaction.commit();
    }

    public void goBackUpdate(){

        currentUpdateIdx--;
        if (currentUpdateIdx < 0){
            currentUpdateIdx = 0;
        }else {
            transitionToUpdate();
        }
    }

    public void goForwardUpdate(){

        currentUpdateIdx++;
        if (currentUpdateIdx > updates.size() - 1){
            currentUpdateIdx = updates.size();
        }else {
            transitionToUpdate();
        }
    }

}
