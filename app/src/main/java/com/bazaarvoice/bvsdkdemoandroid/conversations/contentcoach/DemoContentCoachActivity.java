package com.bazaarvoice.bvsdkdemoandroid.conversations.contentcoach;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class DemoContentCoachActivity extends AppCompatActivity implements DemoContentCoachContract.View {

    private static final String EXTRA_PRODUCT_ID = "extra_product_id";

    @Inject
    BVConversationsClient bvConversationsClient;

    private DemoContentCoachContract.UserActionsListener presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_content_coach);
        ButterKnife.bind(this);
        DemoApp.getAppComponent(this).inject(this);
        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        presenter = new DemoContentCoachPresenter(this, productId, bvConversationsClient);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadReviewTokens(false);
    }

    public static void transitionTo(Context fromActivity, String productId) {
        Intent intent = new Intent(fromActivity, DemoContentCoachActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        fromActivity.startActivity(intent);
    }

    @Override
    public void showTokens(List<String> tokens) {
        LinearLayout container = findViewById(R.id.contentCoachTokensLayout);
        container.removeAllViews();
        for (String token : tokens) {
            TextView tv = new TextView(this);
            tv.setText("\u2022 " + token);
            tv.setTextSize(16f);
            tv.setPadding(0, 8, 0, 8);
            container.addView(tv);
        }
    }

    @Override
    public void showDialogWithMessage(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setNegativeButton("OK", null)
                .create()
                .show();
    }
}
