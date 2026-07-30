package com.bazaarvoice.bvsdkdemoandroid.conversations.reviewsummary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.ReviewSummary;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class DemoReviewSummaryActivity extends AppCompatActivity implements DemoReviewSummaryDetailContract.View {
    private String productId;
    private static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private DemoReviewSummaryDetailContract.UserActionsListener reviewSummaryListener;
    @Inject
    BVConversationsClient bvConversationsClient;
    private LinearLayout reviewSummaryLayout;
    private TextView tvSummaryTitle;
    private TextView tvSummaryBody;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_review_summary);
        ButterKnife.bind(this);
        DemoApp.getAppComponent(this).inject(this);
        this.productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);

        // Sanitize input to prevent SSRF or URL injection
        if (TextUtils.isEmpty(productId) || !productId.matches("^[a-zA-Z0-9_.-]+$")) {
            showDialogWithMessage("Invalid Product ID");
            finish();
            return;
        }

        reviewSummaryListener = new DemoReviewSummaryPresenter(this, productId, bvConversationsClient);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (reviewSummaryListener != null) {
            reviewSummaryListener.loadReviewSummary(false);
        }
    }

    public static void transitionTo(Context fromActivity, String productId) {
        Intent intent = new Intent(fromActivity, DemoReviewSummaryActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        fromActivity.startActivity(intent);
    }
    @Override
    public void showReviewSummary(ReviewSummary reviewSummary) {
        reviewSummaryLayout = findViewById(R.id.reviewSummaryLayout);
        tvSummaryTitle = findViewById(R.id.tvSummaryTitle);
        tvSummaryBody = findViewById(R.id.tvSummaryBody);


        tvSummaryBody.setText(reviewSummary.getSummary());

    }

    @Override
    public void showDialogWithMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setNegativeButton("OK", null).create().show();
    }

}
