package com.bazaarvoice.bvsdkdemoandroid.conversations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.MenuItem;

import com.bazaarvoice.bvsdkdemoandroid.R;

import androidx.appcompat.app.AppCompatActivity;

public class DemoRateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static void transitionTo(Context fromContext) {
        Intent intent = new Intent(fromContext, DemoRateActivity.class);
        fromContext.startActivity(intent);
    }
}
